import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IIndexConfiguration } from 'app/shared/model/index-configuration.model';
import { AccountService } from 'app/core/auth/account.service';
import { IndexConfigurationService } from './index-configuration.service';

@Component({
  selector: 'jhi-index-configuration',
  templateUrl: './index-configuration.component.html'
})
export class IndexConfigurationComponent implements OnInit, OnDestroy {
  indexConfigurations: IIndexConfiguration[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected indexConfigurationService: IndexConfigurationService,
    protected eventManager: JhiEventManager,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.indexConfigurationService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<IIndexConfiguration[]>) => res.ok),
          map((res: HttpResponse<IIndexConfiguration[]>) => res.body)
        )
        .subscribe((res: IIndexConfiguration[]) => (this.indexConfigurations = res));
      return;
    }
    this.indexConfigurationService
      .query()
      .pipe(
        filter((res: HttpResponse<IIndexConfiguration[]>) => res.ok),
        map((res: HttpResponse<IIndexConfiguration[]>) => res.body)
      )
      .subscribe((res: IIndexConfiguration[]) => {
        this.indexConfigurations = res;
        this.currentSearch = '';
      });
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.loadAll();
  }

  clear() {
    this.currentSearch = '';
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInIndexConfigurations();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IIndexConfiguration) {
    return item.id;
  }

  registerChangeInIndexConfigurations() {
    this.eventSubscriber = this.eventManager.subscribe('indexConfigurationListModification', response => this.loadAll());
  }
}

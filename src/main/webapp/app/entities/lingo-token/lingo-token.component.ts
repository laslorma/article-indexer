import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ILingoToken } from 'app/shared/model/lingo-token.model';
import { AccountService } from 'app/core';
import { LingoTokenService } from './lingo-token.service';

@Component({
  selector: 'jhi-lingo-token',
  templateUrl: './lingo-token.component.html'
})
export class LingoTokenComponent implements OnInit, OnDestroy {
  lingoTokens: ILingoToken[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected lingoTokenService: LingoTokenService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ? this.activatedRoute.snapshot.params['search'] : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.lingoTokenService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<ILingoToken[]>) => res.ok),
          map((res: HttpResponse<ILingoToken[]>) => res.body)
        )
        .subscribe((res: ILingoToken[]) => (this.lingoTokens = res), (res: HttpErrorResponse) => this.onError(res.message));
      return;
    }
    this.lingoTokenService
      .query()
      .pipe(
        filter((res: HttpResponse<ILingoToken[]>) => res.ok),
        map((res: HttpResponse<ILingoToken[]>) => res.body)
      )
      .subscribe(
        (res: ILingoToken[]) => {
          this.lingoTokens = res;
          this.currentSearch = '';
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
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
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInLingoTokens();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ILingoToken) {
    return item.id;
  }

  registerChangeInLingoTokens() {
    this.eventSubscriber = this.eventManager.subscribe('lingoTokenListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}

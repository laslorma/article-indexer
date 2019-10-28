import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { INlpServerConf } from 'app/shared/model/nlp-server-conf.model';
import { AccountService } from 'app/core';
import { NlpServerConfService } from './nlp-server-conf.service';

@Component({
  selector: 'jhi-nlp-server-conf',
  templateUrl: './nlp-server-conf.component.html'
})
export class NlpServerConfComponent implements OnInit, OnDestroy {
  nlpServerConfs: INlpServerConf[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected nlpServerConfService: NlpServerConfService,
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
      this.nlpServerConfService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<INlpServerConf[]>) => res.ok),
          map((res: HttpResponse<INlpServerConf[]>) => res.body)
        )
        .subscribe((res: INlpServerConf[]) => (this.nlpServerConfs = res), (res: HttpErrorResponse) => this.onError(res.message));
      return;
    }
    this.nlpServerConfService
      .query()
      .pipe(
        filter((res: HttpResponse<INlpServerConf[]>) => res.ok),
        map((res: HttpResponse<INlpServerConf[]>) => res.body)
      )
      .subscribe(
        (res: INlpServerConf[]) => {
          this.nlpServerConfs = res;
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
    this.registerChangeInNlpServerConfs();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: INlpServerConf) {
    return item.id;
  }

  registerChangeInNlpServerConfs() {
    this.eventSubscriber = this.eventManager.subscribe('nlpServerConfListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}

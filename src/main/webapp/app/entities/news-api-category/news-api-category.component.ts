import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { INewsApiCategory } from 'app/shared/model/news-api-category.model';
import { AccountService } from 'app/core';
import { NewsApiCategoryService } from './news-api-category.service';

@Component({
  selector: 'jhi-news-api-category',
  templateUrl: './news-api-category.component.html'
})
export class NewsApiCategoryComponent implements OnInit, OnDestroy {
  newsApiCategories: INewsApiCategory[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected newsApiCategoryService: NewsApiCategoryService,
    protected jhiAlertService: JhiAlertService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ? this.activatedRoute.snapshot.params['search'] : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.newsApiCategoryService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<INewsApiCategory[]>) => res.ok),
          map((res: HttpResponse<INewsApiCategory[]>) => res.body)
        )
        .subscribe((res: INewsApiCategory[]) => (this.newsApiCategories = res), (res: HttpErrorResponse) => this.onError(res.message));
      return;
    }
    this.newsApiCategoryService
      .query()
      .pipe(
        filter((res: HttpResponse<INewsApiCategory[]>) => res.ok),
        map((res: HttpResponse<INewsApiCategory[]>) => res.body)
      )
      .subscribe(
        (res: INewsApiCategory[]) => {
          this.newsApiCategories = res;
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
    this.registerChangeInNewsApiCategories();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: INewsApiCategory) {
    return item.id;
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  registerChangeInNewsApiCategories() {
    this.eventSubscriber = this.eventManager.subscribe('newsApiCategoryListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}

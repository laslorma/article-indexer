import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { INewsApiCategory } from 'app/shared/model/news-api-category.model';
import { AccountService } from 'app/core/auth/account.service';
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
    protected dataUtils: JhiDataUtils,
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
      this.newsApiCategoryService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<INewsApiCategory[]>) => res.ok),
          map((res: HttpResponse<INewsApiCategory[]>) => res.body)
        )
        .subscribe((res: INewsApiCategory[]) => (this.newsApiCategories = res));
      return;
    }
    this.newsApiCategoryService
      .query()
      .pipe(
        filter((res: HttpResponse<INewsApiCategory[]>) => res.ok),
        map((res: HttpResponse<INewsApiCategory[]>) => res.body)
      )
      .subscribe((res: INewsApiCategory[]) => {
        this.newsApiCategories = res;
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
}

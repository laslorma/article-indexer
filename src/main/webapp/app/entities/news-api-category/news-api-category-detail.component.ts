import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { INewsApiCategory } from 'app/shared/model/news-api-category.model';

@Component({
  selector: 'jhi-news-api-category-detail',
  templateUrl: './news-api-category-detail.component.html'
})
export class NewsApiCategoryDetailComponent implements OnInit {
  newsApiCategory: INewsApiCategory;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ newsApiCategory }) => {
      this.newsApiCategory = newsApiCategory;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}

import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { IndexerSharedModule } from 'app/shared';
import {
  NewsApiCategoryComponent,
  NewsApiCategoryDetailComponent,
  NewsApiCategoryUpdateComponent,
  NewsApiCategoryDeletePopupComponent,
  NewsApiCategoryDeleteDialogComponent,
  newsApiCategoryRoute,
  newsApiCategoryPopupRoute
} from './';

const ENTITY_STATES = [...newsApiCategoryRoute, ...newsApiCategoryPopupRoute];

@NgModule({
  imports: [IndexerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    NewsApiCategoryComponent,
    NewsApiCategoryDetailComponent,
    NewsApiCategoryUpdateComponent,
    NewsApiCategoryDeleteDialogComponent,
    NewsApiCategoryDeletePopupComponent
  ],
  entryComponents: [
    NewsApiCategoryComponent,
    NewsApiCategoryUpdateComponent,
    NewsApiCategoryDeleteDialogComponent,
    NewsApiCategoryDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IndexerNewsApiCategoryModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

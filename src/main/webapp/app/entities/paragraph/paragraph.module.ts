import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { IndexerSharedModule } from 'app/shared';
import {
  ParagraphComponent,
  ParagraphDetailComponent,
  ParagraphUpdateComponent,
  ParagraphDeletePopupComponent,
  ParagraphDeleteDialogComponent,
  paragraphRoute,
  paragraphPopupRoute
} from './';

const ENTITY_STATES = [...paragraphRoute, ...paragraphPopupRoute];

@NgModule({
  imports: [IndexerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ParagraphComponent,
    ParagraphDetailComponent,
    ParagraphUpdateComponent,
    ParagraphDeleteDialogComponent,
    ParagraphDeletePopupComponent
  ],
  entryComponents: [ParagraphComponent, ParagraphUpdateComponent, ParagraphDeleteDialogComponent, ParagraphDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IndexerParagraphModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { IndexerSharedModule } from 'app/shared';
import {
  NlpServerConfComponent,
  NlpServerConfDetailComponent,
  NlpServerConfUpdateComponent,
  NlpServerConfDeletePopupComponent,
  NlpServerConfDeleteDialogComponent,
  nlpServerConfRoute,
  nlpServerConfPopupRoute
} from './';

const ENTITY_STATES = [...nlpServerConfRoute, ...nlpServerConfPopupRoute];

@NgModule({
  imports: [IndexerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    NlpServerConfComponent,
    NlpServerConfDetailComponent,
    NlpServerConfUpdateComponent,
    NlpServerConfDeleteDialogComponent,
    NlpServerConfDeletePopupComponent
  ],
  entryComponents: [
    NlpServerConfComponent,
    NlpServerConfUpdateComponent,
    NlpServerConfDeleteDialogComponent,
    NlpServerConfDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IndexerNlpServerConfModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { IndexerSharedModule } from 'app/shared';
import {
  IndexSessionComponent,
  IndexSessionDetailComponent,
  IndexSessionUpdateComponent,
  IndexSessionDeletePopupComponent,
  IndexSessionDeleteDialogComponent,
  indexSessionRoute,
  indexSessionPopupRoute
} from './';

const ENTITY_STATES = [...indexSessionRoute, ...indexSessionPopupRoute];

@NgModule({
  imports: [IndexerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    IndexSessionComponent,
    IndexSessionDetailComponent,
    IndexSessionUpdateComponent,
    IndexSessionDeleteDialogComponent,
    IndexSessionDeletePopupComponent
  ],
  entryComponents: [
    IndexSessionComponent,
    IndexSessionUpdateComponent,
    IndexSessionDeleteDialogComponent,
    IndexSessionDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IndexerIndexSessionModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

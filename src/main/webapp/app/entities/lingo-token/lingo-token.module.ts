import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { IndexerSharedModule } from 'app/shared';
import {
  LingoTokenComponent,
  LingoTokenDetailComponent,
  LingoTokenUpdateComponent,
  LingoTokenDeletePopupComponent,
  LingoTokenDeleteDialogComponent,
  lingoTokenRoute,
  lingoTokenPopupRoute
} from './';

const ENTITY_STATES = [...lingoTokenRoute, ...lingoTokenPopupRoute];

@NgModule({
  imports: [IndexerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    LingoTokenComponent,
    LingoTokenDetailComponent,
    LingoTokenUpdateComponent,
    LingoTokenDeleteDialogComponent,
    LingoTokenDeletePopupComponent
  ],
  entryComponents: [LingoTokenComponent, LingoTokenUpdateComponent, LingoTokenDeleteDialogComponent, LingoTokenDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IndexerLingoTokenModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

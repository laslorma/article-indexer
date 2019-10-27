import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { IndexerSharedModule } from 'app/shared';
import {
  PartComponent,
  PartDetailComponent,
  PartUpdateComponent,
  PartDeletePopupComponent,
  PartDeleteDialogComponent,
  partRoute,
  partPopupRoute
} from './';

const ENTITY_STATES = [...partRoute, ...partPopupRoute];

@NgModule({
  imports: [IndexerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [PartComponent, PartDetailComponent, PartUpdateComponent, PartDeleteDialogComponent, PartDeletePopupComponent],
  entryComponents: [PartComponent, PartUpdateComponent, PartDeleteDialogComponent, PartDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IndexerPartModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

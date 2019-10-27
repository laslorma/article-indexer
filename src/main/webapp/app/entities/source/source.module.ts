import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { IndexerSharedModule } from 'app/shared';
import {
  SourceComponent,
  SourceDetailComponent,
  SourceUpdateComponent,
  SourceDeletePopupComponent,
  SourceDeleteDialogComponent,
  sourceRoute,
  sourcePopupRoute
} from './';

const ENTITY_STATES = [...sourceRoute, ...sourcePopupRoute];

@NgModule({
  imports: [IndexerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [SourceComponent, SourceDetailComponent, SourceUpdateComponent, SourceDeleteDialogComponent, SourceDeletePopupComponent],
  entryComponents: [SourceComponent, SourceUpdateComponent, SourceDeleteDialogComponent, SourceDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IndexerSourceModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

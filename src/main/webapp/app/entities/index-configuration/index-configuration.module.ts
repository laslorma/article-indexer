import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { IndexerSharedModule } from 'app/shared';
import {
  IndexConfigurationComponent,
  IndexConfigurationDetailComponent,
  IndexConfigurationUpdateComponent,
  IndexConfigurationDeletePopupComponent,
  IndexConfigurationDeleteDialogComponent,
  indexConfigurationRoute,
  indexConfigurationPopupRoute
} from './';

const ENTITY_STATES = [...indexConfigurationRoute, ...indexConfigurationPopupRoute];

@NgModule({
  imports: [IndexerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    IndexConfigurationComponent,
    IndexConfigurationDetailComponent,
    IndexConfigurationUpdateComponent,
    IndexConfigurationDeleteDialogComponent,
    IndexConfigurationDeletePopupComponent
  ],
  entryComponents: [
    IndexConfigurationComponent,
    IndexConfigurationUpdateComponent,
    IndexConfigurationDeleteDialogComponent,
    IndexConfigurationDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IndexerIndexConfigurationModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

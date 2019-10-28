import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { IndexerSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [IndexerSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [IndexerSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IndexerSharedModule {
  static forRoot() {
    return {
      ngModule: IndexerSharedModule
    };
  }
}

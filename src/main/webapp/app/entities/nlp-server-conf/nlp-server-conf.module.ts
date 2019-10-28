import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IndexerSharedModule } from 'app/shared/shared.module';
import { NlpServerConfComponent } from './nlp-server-conf.component';
import { NlpServerConfDetailComponent } from './nlp-server-conf-detail.component';
import { NlpServerConfUpdateComponent } from './nlp-server-conf-update.component';
import { NlpServerConfDeletePopupComponent, NlpServerConfDeleteDialogComponent } from './nlp-server-conf-delete-dialog.component';
import { nlpServerConfRoute, nlpServerConfPopupRoute } from './nlp-server-conf.route';

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
  entryComponents: [NlpServerConfDeleteDialogComponent]
})
export class IndexerNlpServerConfModule {}

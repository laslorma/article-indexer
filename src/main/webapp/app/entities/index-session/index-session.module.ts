import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IndexerSharedModule } from 'app/shared/shared.module';
import { IndexSessionComponent } from './index-session.component';
import { IndexSessionDetailComponent } from './index-session-detail.component';
import { IndexSessionUpdateComponent } from './index-session-update.component';
import { IndexSessionDeletePopupComponent, IndexSessionDeleteDialogComponent } from './index-session-delete-dialog.component';
import { indexSessionRoute, indexSessionPopupRoute } from './index-session.route';

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
  entryComponents: [IndexSessionDeleteDialogComponent]
})
export class IndexerIndexSessionModule {}

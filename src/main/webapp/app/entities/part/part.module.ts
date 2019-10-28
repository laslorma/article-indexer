import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IndexerSharedModule } from 'app/shared/shared.module';
import { PartComponent } from './part.component';
import { PartDetailComponent } from './part-detail.component';
import { PartUpdateComponent } from './part-update.component';
import { PartDeletePopupComponent, PartDeleteDialogComponent } from './part-delete-dialog.component';
import { partRoute, partPopupRoute } from './part.route';

const ENTITY_STATES = [...partRoute, ...partPopupRoute];

@NgModule({
  imports: [IndexerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [PartComponent, PartDetailComponent, PartUpdateComponent, PartDeleteDialogComponent, PartDeletePopupComponent],
  entryComponents: [PartDeleteDialogComponent]
})
export class IndexerPartModule {}

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IndexerSharedModule } from 'app/shared/shared.module';
import { SourceComponent } from './source.component';
import { SourceDetailComponent } from './source-detail.component';
import { SourceUpdateComponent } from './source-update.component';
import { SourceDeletePopupComponent, SourceDeleteDialogComponent } from './source-delete-dialog.component';
import { sourceRoute, sourcePopupRoute } from './source.route';

const ENTITY_STATES = [...sourceRoute, ...sourcePopupRoute];

@NgModule({
  imports: [IndexerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [SourceComponent, SourceDetailComponent, SourceUpdateComponent, SourceDeleteDialogComponent, SourceDeletePopupComponent],
  entryComponents: [SourceDeleteDialogComponent]
})
export class IndexerSourceModule {}

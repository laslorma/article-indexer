import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IndexerSharedModule } from 'app/shared/shared.module';
import { LingoTokenComponent } from './lingo-token.component';
import { LingoTokenDetailComponent } from './lingo-token-detail.component';
import { LingoTokenUpdateComponent } from './lingo-token-update.component';
import { LingoTokenDeletePopupComponent, LingoTokenDeleteDialogComponent } from './lingo-token-delete-dialog.component';
import { lingoTokenRoute, lingoTokenPopupRoute } from './lingo-token.route';

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
  entryComponents: [LingoTokenDeleteDialogComponent]
})
export class IndexerLingoTokenModule {}

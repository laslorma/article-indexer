import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IndexerSharedModule } from 'app/shared/shared.module';
import { ParagraphComponent } from './paragraph.component';
import { ParagraphDetailComponent } from './paragraph-detail.component';
import { ParagraphUpdateComponent } from './paragraph-update.component';
import { ParagraphDeletePopupComponent, ParagraphDeleteDialogComponent } from './paragraph-delete-dialog.component';
import { paragraphRoute, paragraphPopupRoute } from './paragraph.route';

const ENTITY_STATES = [...paragraphRoute, ...paragraphPopupRoute];

@NgModule({
  imports: [IndexerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ParagraphComponent,
    ParagraphDetailComponent,
    ParagraphUpdateComponent,
    ParagraphDeleteDialogComponent,
    ParagraphDeletePopupComponent
  ],
  entryComponents: [ParagraphDeleteDialogComponent]
})
export class IndexerParagraphModule {}

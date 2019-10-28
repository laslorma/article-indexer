import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IndexerSharedModule } from 'app/shared/shared.module';
import { IndexConfigurationComponent } from './index-configuration.component';
import { IndexConfigurationDetailComponent } from './index-configuration-detail.component';
import { IndexConfigurationUpdateComponent } from './index-configuration-update.component';
import {
  IndexConfigurationDeletePopupComponent,
  IndexConfigurationDeleteDialogComponent
} from './index-configuration-delete-dialog.component';
import { indexConfigurationRoute, indexConfigurationPopupRoute } from './index-configuration.route';

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
  entryComponents: [IndexConfigurationDeleteDialogComponent]
})
export class IndexerIndexConfigurationModule {}

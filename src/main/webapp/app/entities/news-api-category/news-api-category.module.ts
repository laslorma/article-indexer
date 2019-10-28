import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IndexerSharedModule } from 'app/shared/shared.module';
import { NewsApiCategoryComponent } from './news-api-category.component';
import { NewsApiCategoryDetailComponent } from './news-api-category-detail.component';
import { NewsApiCategoryUpdateComponent } from './news-api-category-update.component';
import { NewsApiCategoryDeletePopupComponent, NewsApiCategoryDeleteDialogComponent } from './news-api-category-delete-dialog.component';
import { newsApiCategoryRoute, newsApiCategoryPopupRoute } from './news-api-category.route';

const ENTITY_STATES = [...newsApiCategoryRoute, ...newsApiCategoryPopupRoute];

@NgModule({
  imports: [IndexerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    NewsApiCategoryComponent,
    NewsApiCategoryDetailComponent,
    NewsApiCategoryUpdateComponent,
    NewsApiCategoryDeleteDialogComponent,
    NewsApiCategoryDeletePopupComponent
  ],
  entryComponents: [NewsApiCategoryDeleteDialogComponent]
})
export class IndexerNewsApiCategoryModule {}

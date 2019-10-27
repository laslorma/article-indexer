import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'article',
        loadChildren: () => import('./article/article.module').then(m => m.IndexerArticleModule)
      },
      {
        path: 'news-api-category',
        loadChildren: () => import('./news-api-category/news-api-category.module').then(m => m.IndexerNewsApiCategoryModule)
      },
      {
        path: 'paragraph',
        loadChildren: () => import('./paragraph/paragraph.module').then(m => m.IndexerParagraphModule)
      },
      {
        path: 'part',
        loadChildren: () => import('./part/part.module').then(m => m.IndexerPartModule)
      },
      {
        path: 'country',
        loadChildren: () => import('./country/country.module').then(m => m.IndexerCountryModule)
      },
      {
        path: 'index-session',
        loadChildren: () => import('./index-session/index-session.module').then(m => m.IndexerIndexSessionModule)
      },
      {
        path: 'source',
        loadChildren: () => import('./source/source.module').then(m => m.IndexerSourceModule)
      },
      {
        path: 'index-configuration',
        loadChildren: () => import('./index-configuration/index-configuration.module').then(m => m.IndexerIndexConfigurationModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IndexerEntityModule {}

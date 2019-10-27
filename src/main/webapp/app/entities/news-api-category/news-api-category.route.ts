import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { NewsApiCategory } from 'app/shared/model/news-api-category.model';
import { NewsApiCategoryService } from './news-api-category.service';
import { NewsApiCategoryComponent } from './news-api-category.component';
import { NewsApiCategoryDetailComponent } from './news-api-category-detail.component';
import { NewsApiCategoryUpdateComponent } from './news-api-category-update.component';
import { NewsApiCategoryDeletePopupComponent } from './news-api-category-delete-dialog.component';
import { INewsApiCategory } from 'app/shared/model/news-api-category.model';

@Injectable({ providedIn: 'root' })
export class NewsApiCategoryResolve implements Resolve<INewsApiCategory> {
  constructor(private service: NewsApiCategoryService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<INewsApiCategory> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<NewsApiCategory>) => response.ok),
        map((newsApiCategory: HttpResponse<NewsApiCategory>) => newsApiCategory.body)
      );
    }
    return of(new NewsApiCategory());
  }
}

export const newsApiCategoryRoute: Routes = [
  {
    path: '',
    component: NewsApiCategoryComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.newsApiCategory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: NewsApiCategoryDetailComponent,
    resolve: {
      newsApiCategory: NewsApiCategoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.newsApiCategory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: NewsApiCategoryUpdateComponent,
    resolve: {
      newsApiCategory: NewsApiCategoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.newsApiCategory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: NewsApiCategoryUpdateComponent,
    resolve: {
      newsApiCategory: NewsApiCategoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.newsApiCategory.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const newsApiCategoryPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: NewsApiCategoryDeletePopupComponent,
    resolve: {
      newsApiCategory: NewsApiCategoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.newsApiCategory.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

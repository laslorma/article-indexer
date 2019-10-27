import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IndexSession } from 'app/shared/model/index-session.model';
import { IndexSessionService } from './index-session.service';
import { IndexSessionComponent } from './index-session.component';
import { IndexSessionDetailComponent } from './index-session-detail.component';
import { IndexSessionUpdateComponent } from './index-session-update.component';
import { IndexSessionDeletePopupComponent } from './index-session-delete-dialog.component';
import { IIndexSession } from 'app/shared/model/index-session.model';

@Injectable({ providedIn: 'root' })
export class IndexSessionResolve implements Resolve<IIndexSession> {
  constructor(private service: IndexSessionService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IIndexSession> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<IndexSession>) => response.ok),
        map((indexSession: HttpResponse<IndexSession>) => indexSession.body)
      );
    }
    return of(new IndexSession());
  }
}

export const indexSessionRoute: Routes = [
  {
    path: '',
    component: IndexSessionComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'indexerApp.indexSession.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: IndexSessionDetailComponent,
    resolve: {
      indexSession: IndexSessionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.indexSession.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: IndexSessionUpdateComponent,
    resolve: {
      indexSession: IndexSessionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.indexSession.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: IndexSessionUpdateComponent,
    resolve: {
      indexSession: IndexSessionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.indexSession.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const indexSessionPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: IndexSessionDeletePopupComponent,
    resolve: {
      indexSession: IndexSessionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.indexSession.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

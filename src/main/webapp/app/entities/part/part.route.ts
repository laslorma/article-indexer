import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Part } from 'app/shared/model/part.model';
import { PartService } from './part.service';
import { PartComponent } from './part.component';
import { PartDetailComponent } from './part-detail.component';
import { PartUpdateComponent } from './part-update.component';
import { PartDeletePopupComponent } from './part-delete-dialog.component';
import { IPart } from 'app/shared/model/part.model';

@Injectable({ providedIn: 'root' })
export class PartResolve implements Resolve<IPart> {
  constructor(private service: PartService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPart> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Part>) => response.ok),
        map((part: HttpResponse<Part>) => part.body)
      );
    }
    return of(new Part());
  }
}

export const partRoute: Routes = [
  {
    path: '',
    component: PartComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'indexerApp.part.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PartDetailComponent,
    resolve: {
      part: PartResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.part.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PartUpdateComponent,
    resolve: {
      part: PartResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.part.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PartUpdateComponent,
    resolve: {
      part: PartResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.part.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const partPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PartDeletePopupComponent,
    resolve: {
      part: PartResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.part.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

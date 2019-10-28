import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { LingoToken } from 'app/shared/model/lingo-token.model';
import { LingoTokenService } from './lingo-token.service';
import { LingoTokenComponent } from './lingo-token.component';
import { LingoTokenDetailComponent } from './lingo-token-detail.component';
import { LingoTokenUpdateComponent } from './lingo-token-update.component';
import { LingoTokenDeletePopupComponent } from './lingo-token-delete-dialog.component';
import { ILingoToken } from 'app/shared/model/lingo-token.model';

@Injectable({ providedIn: 'root' })
export class LingoTokenResolve implements Resolve<ILingoToken> {
  constructor(private service: LingoTokenService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ILingoToken> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<LingoToken>) => response.ok),
        map((lingoToken: HttpResponse<LingoToken>) => lingoToken.body)
      );
    }
    return of(new LingoToken());
  }
}

export const lingoTokenRoute: Routes = [
  {
    path: '',
    component: LingoTokenComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.lingoToken.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LingoTokenDetailComponent,
    resolve: {
      lingoToken: LingoTokenResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.lingoToken.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LingoTokenUpdateComponent,
    resolve: {
      lingoToken: LingoTokenResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.lingoToken.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LingoTokenUpdateComponent,
    resolve: {
      lingoToken: LingoTokenResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.lingoToken.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const lingoTokenPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: LingoTokenDeletePopupComponent,
    resolve: {
      lingoToken: LingoTokenResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.lingoToken.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

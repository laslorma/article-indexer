import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { NlpServerConf } from 'app/shared/model/nlp-server-conf.model';
import { NlpServerConfService } from './nlp-server-conf.service';
import { NlpServerConfComponent } from './nlp-server-conf.component';
import { NlpServerConfDetailComponent } from './nlp-server-conf-detail.component';
import { NlpServerConfUpdateComponent } from './nlp-server-conf-update.component';
import { NlpServerConfDeletePopupComponent } from './nlp-server-conf-delete-dialog.component';
import { INlpServerConf } from 'app/shared/model/nlp-server-conf.model';

@Injectable({ providedIn: 'root' })
export class NlpServerConfResolve implements Resolve<INlpServerConf> {
  constructor(private service: NlpServerConfService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<INlpServerConf> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<NlpServerConf>) => response.ok),
        map((nlpServerConf: HttpResponse<NlpServerConf>) => nlpServerConf.body)
      );
    }
    return of(new NlpServerConf());
  }
}

export const nlpServerConfRoute: Routes = [
  {
    path: '',
    component: NlpServerConfComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.nlpServerConf.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: NlpServerConfDetailComponent,
    resolve: {
      nlpServerConf: NlpServerConfResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.nlpServerConf.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: NlpServerConfUpdateComponent,
    resolve: {
      nlpServerConf: NlpServerConfResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.nlpServerConf.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: NlpServerConfUpdateComponent,
    resolve: {
      nlpServerConf: NlpServerConfResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.nlpServerConf.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const nlpServerConfPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: NlpServerConfDeletePopupComponent,
    resolve: {
      nlpServerConf: NlpServerConfResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.nlpServerConf.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

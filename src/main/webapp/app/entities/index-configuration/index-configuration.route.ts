import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IndexConfiguration } from 'app/shared/model/index-configuration.model';
import { IndexConfigurationService } from './index-configuration.service';
import { IndexConfigurationComponent } from './index-configuration.component';
import { IndexConfigurationDetailComponent } from './index-configuration-detail.component';
import { IndexConfigurationUpdateComponent } from './index-configuration-update.component';
import { IndexConfigurationDeletePopupComponent } from './index-configuration-delete-dialog.component';
import { IIndexConfiguration } from 'app/shared/model/index-configuration.model';

@Injectable({ providedIn: 'root' })
export class IndexConfigurationResolve implements Resolve<IIndexConfiguration> {
  constructor(private service: IndexConfigurationService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IIndexConfiguration> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<IndexConfiguration>) => response.ok),
        map((indexConfiguration: HttpResponse<IndexConfiguration>) => indexConfiguration.body)
      );
    }
    return of(new IndexConfiguration());
  }
}

export const indexConfigurationRoute: Routes = [
  {
    path: '',
    component: IndexConfigurationComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.indexConfiguration.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: IndexConfigurationDetailComponent,
    resolve: {
      indexConfiguration: IndexConfigurationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.indexConfiguration.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: IndexConfigurationUpdateComponent,
    resolve: {
      indexConfiguration: IndexConfigurationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.indexConfiguration.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: IndexConfigurationUpdateComponent,
    resolve: {
      indexConfiguration: IndexConfigurationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.indexConfiguration.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const indexConfigurationPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: IndexConfigurationDeletePopupComponent,
    resolve: {
      indexConfiguration: IndexConfigurationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.indexConfiguration.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Paragraph } from 'app/shared/model/paragraph.model';
import { ParagraphService } from './paragraph.service';
import { ParagraphComponent } from './paragraph.component';
import { ParagraphDetailComponent } from './paragraph-detail.component';
import { ParagraphUpdateComponent } from './paragraph-update.component';
import { ParagraphDeletePopupComponent } from './paragraph-delete-dialog.component';
import { IParagraph } from 'app/shared/model/paragraph.model';

@Injectable({ providedIn: 'root' })
export class ParagraphResolve implements Resolve<IParagraph> {
  constructor(private service: ParagraphService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IParagraph> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Paragraph>) => response.ok),
        map((paragraph: HttpResponse<Paragraph>) => paragraph.body)
      );
    }
    return of(new Paragraph());
  }
}

export const paragraphRoute: Routes = [
  {
    path: '',
    component: ParagraphComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'indexerApp.paragraph.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ParagraphDetailComponent,
    resolve: {
      paragraph: ParagraphResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.paragraph.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ParagraphUpdateComponent,
    resolve: {
      paragraph: ParagraphResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.paragraph.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ParagraphUpdateComponent,
    resolve: {
      paragraph: ParagraphResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.paragraph.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const paragraphPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ParagraphDeletePopupComponent,
    resolve: {
      paragraph: ParagraphResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'indexerApp.paragraph.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

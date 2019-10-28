import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IIndexSession } from 'app/shared/model/index-session.model';

type EntityResponseType = HttpResponse<IIndexSession>;
type EntityArrayResponseType = HttpResponse<IIndexSession[]>;

@Injectable({ providedIn: 'root' })
export class IndexSessionService {
  public resourceUrl = SERVER_API_URL + 'api/index-sessions';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/index-sessions';

  constructor(protected http: HttpClient) {}

  create(indexSession: IIndexSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(indexSession);
    return this.http
      .post<IIndexSession>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(indexSession: IIndexSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(indexSession);
    return this.http
      .put<IIndexSession>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIndexSession>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIndexSession[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIndexSession[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(indexSession: IIndexSession): IIndexSession {
    const copy: IIndexSession = Object.assign({}, indexSession, {
      started: indexSession.started != null && indexSession.started.isValid() ? indexSession.started.toJSON() : null,
      ended: indexSession.ended != null && indexSession.ended.isValid() ? indexSession.ended.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.started = res.body.started != null ? moment(res.body.started) : null;
      res.body.ended = res.body.ended != null ? moment(res.body.ended) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((indexSession: IIndexSession) => {
        indexSession.started = indexSession.started != null ? moment(indexSession.started) : null;
        indexSession.ended = indexSession.ended != null ? moment(indexSession.ended) : null;
      });
    }
    return res;
  }
}

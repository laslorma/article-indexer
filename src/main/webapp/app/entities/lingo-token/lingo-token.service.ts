import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILingoToken } from 'app/shared/model/lingo-token.model';

type EntityResponseType = HttpResponse<ILingoToken>;
type EntityArrayResponseType = HttpResponse<ILingoToken[]>;

@Injectable({ providedIn: 'root' })
export class LingoTokenService {
  public resourceUrl = SERVER_API_URL + 'api/lingo-tokens';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/lingo-tokens';

  constructor(protected http: HttpClient) {}

  create(lingoToken: ILingoToken): Observable<EntityResponseType> {
    return this.http.post<ILingoToken>(this.resourceUrl, lingoToken, { observe: 'response' });
  }

  update(lingoToken: ILingoToken): Observable<EntityResponseType> {
    return this.http.put<ILingoToken>(this.resourceUrl, lingoToken, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILingoToken>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILingoToken[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILingoToken[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}

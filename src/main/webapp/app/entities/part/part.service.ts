import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPart } from 'app/shared/model/part.model';

type EntityResponseType = HttpResponse<IPart>;
type EntityArrayResponseType = HttpResponse<IPart[]>;

@Injectable({ providedIn: 'root' })
export class PartService {
  public resourceUrl = SERVER_API_URL + 'api/parts';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/parts';

  constructor(protected http: HttpClient) {}

  create(part: IPart): Observable<EntityResponseType> {
    return this.http.post<IPart>(this.resourceUrl, part, { observe: 'response' });
  }

  update(part: IPart): Observable<EntityResponseType> {
    return this.http.put<IPart>(this.resourceUrl, part, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPart>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPart[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPart[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}

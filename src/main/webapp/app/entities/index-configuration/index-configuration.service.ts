import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IIndexConfiguration } from 'app/shared/model/index-configuration.model';

type EntityResponseType = HttpResponse<IIndexConfiguration>;
type EntityArrayResponseType = HttpResponse<IIndexConfiguration[]>;

@Injectable({ providedIn: 'root' })
export class IndexConfigurationService {
  public resourceUrl = SERVER_API_URL + 'api/index-configurations';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/index-configurations';

  constructor(protected http: HttpClient) {}

  create(indexConfiguration: IIndexConfiguration): Observable<EntityResponseType> {
    return this.http.post<IIndexConfiguration>(this.resourceUrl, indexConfiguration, { observe: 'response' });
  }

  update(indexConfiguration: IIndexConfiguration): Observable<EntityResponseType> {
    return this.http.put<IIndexConfiguration>(this.resourceUrl, indexConfiguration, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIndexConfiguration>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIndexConfiguration[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIndexConfiguration[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}

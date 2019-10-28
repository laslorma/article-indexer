import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { INlpServerConf } from 'app/shared/model/nlp-server-conf.model';

type EntityResponseType = HttpResponse<INlpServerConf>;
type EntityArrayResponseType = HttpResponse<INlpServerConf[]>;

@Injectable({ providedIn: 'root' })
export class NlpServerConfService {
  public resourceUrl = SERVER_API_URL + 'api/nlp-server-confs';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/nlp-server-confs';

  constructor(protected http: HttpClient) {}

  create(nlpServerConf: INlpServerConf): Observable<EntityResponseType> {
    return this.http.post<INlpServerConf>(this.resourceUrl, nlpServerConf, { observe: 'response' });
  }

  update(nlpServerConf: INlpServerConf): Observable<EntityResponseType> {
    return this.http.put<INlpServerConf>(this.resourceUrl, nlpServerConf, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INlpServerConf>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INlpServerConf[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INlpServerConf[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}

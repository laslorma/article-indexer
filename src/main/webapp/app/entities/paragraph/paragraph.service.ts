import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IParagraph } from 'app/shared/model/paragraph.model';

type EntityResponseType = HttpResponse<IParagraph>;
type EntityArrayResponseType = HttpResponse<IParagraph[]>;

@Injectable({ providedIn: 'root' })
export class ParagraphService {
  public resourceUrl = SERVER_API_URL + 'api/paragraphs';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/paragraphs';

  constructor(protected http: HttpClient) {}

  create(paragraph: IParagraph): Observable<EntityResponseType> {
    return this.http.post<IParagraph>(this.resourceUrl, paragraph, { observe: 'response' });
  }

  update(paragraph: IParagraph): Observable<EntityResponseType> {
    return this.http.put<IParagraph>(this.resourceUrl, paragraph, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParagraph>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParagraph[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParagraph[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}

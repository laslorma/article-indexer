import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { INewsApiCategory } from 'app/shared/model/news-api-category.model';

type EntityResponseType = HttpResponse<INewsApiCategory>;
type EntityArrayResponseType = HttpResponse<INewsApiCategory[]>;

@Injectable({ providedIn: 'root' })
export class NewsApiCategoryService {
  public resourceUrl = SERVER_API_URL + 'api/news-api-categories';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/news-api-categories';

  constructor(protected http: HttpClient) {}

  create(newsApiCategory: INewsApiCategory): Observable<EntityResponseType> {
    return this.http.post<INewsApiCategory>(this.resourceUrl, newsApiCategory, { observe: 'response' });
  }

  update(newsApiCategory: INewsApiCategory): Observable<EntityResponseType> {
    return this.http.put<INewsApiCategory>(this.resourceUrl, newsApiCategory, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INewsApiCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INewsApiCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INewsApiCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}

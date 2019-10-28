import { IArticle } from 'app/shared/model/article.model';

export interface INewsApiCategory {
  id?: number;
  name?: string;
  imageContentType?: string;
  image?: any;
  active?: boolean;
  articles?: IArticle[];
}

export class NewsApiCategory implements INewsApiCategory {
  constructor(
    public id?: number,
    public name?: string,
    public imageContentType?: string,
    public image?: any,
    public active?: boolean,
    public articles?: IArticle[]
  ) {
    this.active = this.active || false;
  }
}

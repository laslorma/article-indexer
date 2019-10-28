import { IArticle } from 'app/shared/model/article.model';

export interface ISource {
  id?: number;
  sourceId?: string;
  name?: string;
  description?: string;
  url?: string;
  category?: string;
  language?: string;
  country?: string;
  active?: boolean;
  articles?: IArticle[];
}

export class Source implements ISource {
  constructor(
    public id?: number,
    public sourceId?: string,
    public name?: string,
    public description?: string,
    public url?: string,
    public category?: string,
    public language?: string,
    public country?: string,
    public active?: boolean,
    public articles?: IArticle[]
  ) {
    this.active = this.active || false;
  }
}

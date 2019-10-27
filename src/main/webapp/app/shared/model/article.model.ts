import { INewsApiCategory } from 'app/shared/model/news-api-category.model';
import { ISource } from 'app/shared/model/source.model';
import { IParagraph } from 'app/shared/model/paragraph.model';
import { IPart } from 'app/shared/model/part.model';

export interface IArticle {
  id?: number;
  author?: string;
  title?: string;
  description?: any;
  url?: string;
  urlToImage?: string;
  publishedAt?: string;
  category?: string;
  content?: any;
  countryCode?: string;
  languageCode?: string;
  sentiment?: string;
  textReadability?: string;
  numberOfParts?: number;
  newsApiCategory?: INewsApiCategory;
  source?: ISource;
  paragraphs?: IParagraph[];
  parts?: IPart[];
}

export class Article implements IArticle {
  constructor(
    public id?: number,
    public author?: string,
    public title?: string,
    public description?: any,
    public url?: string,
    public urlToImage?: string,
    public publishedAt?: string,
    public category?: string,
    public content?: any,
    public countryCode?: string,
    public languageCode?: string,
    public sentiment?: string,
    public textReadability?: string,
    public numberOfParts?: number,
    public newsApiCategory?: INewsApiCategory,
    public source?: ISource,
    public paragraphs?: IParagraph[],
    public parts?: IPart[]
  ) {}
}

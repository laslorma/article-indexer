import { IArticle } from 'app/shared/model/article.model';

export interface IPart {
  id?: number;
  text?: string;
  posibleOptions?: string;
  article?: IArticle;
}

export class Part implements IPart {
  constructor(public id?: number, public text?: string, public posibleOptions?: string, public article?: IArticle) {}
}

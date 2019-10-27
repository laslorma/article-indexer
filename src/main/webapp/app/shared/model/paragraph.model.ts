import { IArticle } from 'app/shared/model/article.model';

export interface IParagraph {
  id?: number;
  content?: any;
  originalCleanedContent?: any;
  totalWords?: number;
  header?: boolean;
  readability?: string;
  article?: IArticle;
}

export class Paragraph implements IParagraph {
  constructor(
    public id?: number,
    public content?: any,
    public originalCleanedContent?: any,
    public totalWords?: number,
    public header?: boolean,
    public readability?: string,
    public article?: IArticle
  ) {
    this.header = this.header || false;
  }
}

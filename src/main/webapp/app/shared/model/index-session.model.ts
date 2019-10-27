import { Moment } from 'moment';

export interface IIndexSession {
  id?: number;
  newsApiCalls?: number;
  fiveFilterApiCalls?: number;
  started?: Moment;
  ended?: Moment;
  duration?: number;
  totalArticles?: number;
  indexing?: boolean;
  articlesSaved?: number;
  errorMessage?: any;
  hadError?: boolean;
}

export class IndexSession implements IIndexSession {
  constructor(
    public id?: number,
    public newsApiCalls?: number,
    public fiveFilterApiCalls?: number,
    public started?: Moment,
    public ended?: Moment,
    public duration?: number,
    public totalArticles?: number,
    public indexing?: boolean,
    public articlesSaved?: number,
    public errorMessage?: any,
    public hadError?: boolean
  ) {
    this.indexing = this.indexing || false;
    this.hadError = this.hadError || false;
  }
}

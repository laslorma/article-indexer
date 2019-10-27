export interface IIndexConfiguration {
  id?: number;
  generateCorpuses?: boolean;
  corpusesOutputPath?: string;
  newsApiKey?: string;
  activateAllCategoriesAndCountries?: boolean;
}

export class IndexConfiguration implements IIndexConfiguration {
  constructor(
    public id?: number,
    public generateCorpuses?: boolean,
    public corpusesOutputPath?: string,
    public newsApiKey?: string,
    public activateAllCategoriesAndCountries?: boolean
  ) {
    this.generateCorpuses = this.generateCorpuses || false;
    this.activateAllCategoriesAndCountries = this.activateAllCategoriesAndCountries || false;
  }
}

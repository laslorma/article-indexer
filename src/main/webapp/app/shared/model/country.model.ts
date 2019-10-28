export interface ICountry {
  id?: number;
  code?: string;
  language?: string;
  name?: string;
  active?: boolean;
}

export class Country implements ICountry {
  constructor(public id?: number, public code?: string, public language?: string, public name?: string, public active?: boolean) {
    this.active = this.active || false;
  }
}

export interface INlpServerConf {
  id?: number;
  url?: string;
  port?: string;
}

export class NlpServerConf implements INlpServerConf {
  constructor(public id?: number, public url?: string, public port?: string) {}
}

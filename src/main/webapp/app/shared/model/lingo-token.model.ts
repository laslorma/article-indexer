export interface ILingoToken {
  id?: number;
  text?: string;
  blankText?: string;
  lingoOrder?: number;
  posTag?: string;
  lemma?: string;
  nerTag?: string;
}

export class LingoToken implements ILingoToken {
  constructor(
    public id?: number,
    public text?: string,
    public blankText?: string,
    public lingoOrder?: number,
    public posTag?: string,
    public lemma?: string,
    public nerTag?: string
  ) {}
}

export interface IContractPattern {
  id?: number;
  title?: string;
  patternId?: number;
}

export class ContractPattern implements IContractPattern {
  constructor(public id?: number, public title?: string, public patternId?: number) {}
}

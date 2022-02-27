import { IService } from '@/shared/model/service.model';
import { ITariff } from '@/shared/model/tariff.model';

export interface IMarketingResearch {
  id?: number;
  title?: string;
  service?: IService | null;
  tariff?: ITariff | null;
}

export class MarketingResearch implements IMarketingResearch {
  constructor(public id?: number, public title?: string, public service?: IService | null, public tariff?: ITariff | null) {}
}

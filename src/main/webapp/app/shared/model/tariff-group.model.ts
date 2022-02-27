import { ITariff } from '@/shared/model/tariff.model';

export interface ITariffGroup {
  id?: number;
  title?: string;
  tariffs?: ITariff[] | null;
}

export class TariffGroup implements ITariffGroup {
  constructor(public id?: number, public title?: string, public tariffs?: ITariff[] | null) {}
}

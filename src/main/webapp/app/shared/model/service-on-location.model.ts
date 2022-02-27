import { IService } from '@/shared/model/service.model';
import { ITariffGroup } from '@/shared/model/tariff-group.model';
import { IContractPattern } from '@/shared/model/contract-pattern.model';
import { ILocation } from '@/shared/model/location.model';

export interface IServiceOnLocation {
  id?: number;
  datefrom?: Date;
  dateTo?: Date | null;
  service?: IService | null;
  tariffGroup?: ITariffGroup | null;
  pattern?: IContractPattern | null;
  location?: ILocation | null;
}

export class ServiceOnLocation implements IServiceOnLocation {
  constructor(
    public id?: number,
    public datefrom?: Date,
    public dateTo?: Date | null,
    public service?: IService | null,
    public tariffGroup?: ITariffGroup | null,
    public pattern?: IContractPattern | null,
    public location?: ILocation | null
  ) {}
}

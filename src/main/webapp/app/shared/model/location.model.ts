import { IHouse } from '@/shared/model/house.model';
import { IServiceOnLocation } from '@/shared/model/service-on-location.model';
import { IPromotionOnLocation } from '@/shared/model/promotion-on-location.model';

export interface ILocation {
  id?: number;
  title?: string;
  houses?: IHouse[] | null;
  serviceOnLocations?: IServiceOnLocation[] | null;
  promotionOnLocations?: IPromotionOnLocation[] | null;
}

export class Location implements ILocation {
  constructor(
    public id?: number,
    public title?: string,
    public houses?: IHouse[] | null,
    public serviceOnLocations?: IServiceOnLocation[] | null,
    public promotionOnLocations?: IPromotionOnLocation[] | null
  ) {}
}

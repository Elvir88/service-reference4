import { IPacketDiscount } from '@/shared/model/packet-discount.model';
import { IMarketingResearch } from '@/shared/model/marketing-research.model';
import { ILocation } from '@/shared/model/location.model';

export interface IPromotionOnLocation {
  id?: number;
  title?: string;
  dateFrom?: Date;
  dateTo?: Date | null;
  packetDiscount?: IPacketDiscount | null;
  marketingResearch?: IMarketingResearch | null;
  location?: ILocation | null;
}

export class PromotionOnLocation implements IPromotionOnLocation {
  constructor(
    public id?: number,
    public title?: string,
    public dateFrom?: Date,
    public dateTo?: Date | null,
    public packetDiscount?: IPacketDiscount | null,
    public marketingResearch?: IMarketingResearch | null,
    public location?: ILocation | null
  ) {}
}

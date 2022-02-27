import { IServiceInPacketDiscount } from '@/shared/model/service-in-packet-discount.model';

export interface IPacketDiscount {
  id?: number;
  title?: string;
  cost?: number;
  serviceInPacketDiscounts?: IServiceInPacketDiscount[] | null;
}

export class PacketDiscount implements IPacketDiscount {
  constructor(
    public id?: number,
    public title?: string,
    public cost?: number,
    public serviceInPacketDiscounts?: IServiceInPacketDiscount[] | null
  ) {}
}

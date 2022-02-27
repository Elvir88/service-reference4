import { IService } from '@/shared/model/service.model';
import { ITariff } from '@/shared/model/tariff.model';
import { IPacketDiscount } from '@/shared/model/packet-discount.model';

export interface IServiceInPacketDiscount {
  id?: number;
  coefficient?: number;
  service?: IService | null;
  tariff?: ITariff | null;
  packetDiscount?: IPacketDiscount | null;
}

export class ServiceInPacketDiscount implements IServiceInPacketDiscount {
  constructor(
    public id?: number,
    public coefficient?: number,
    public service?: IService | null,
    public tariff?: ITariff | null,
    public packetDiscount?: IPacketDiscount | null
  ) {}
}

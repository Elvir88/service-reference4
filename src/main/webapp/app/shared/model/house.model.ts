import { ILocation } from '@/shared/model/location.model';

export interface IHouse {
  id?: number;
  houseId?: number;
  location?: ILocation | null;
}

export class House implements IHouse {
  constructor(public id?: number, public houseId?: number, public location?: ILocation | null) {}
}

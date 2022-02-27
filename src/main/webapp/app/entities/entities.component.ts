import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import HouseService from './house/house.service';
import LocationService from './location/location.service';
import ServiceService from './service/service.service';
import ServiceOnLocationService from './service-on-location/service-on-location.service';
import TariffService from './tariff/tariff.service';
import TariffGroupService from './tariff-group/tariff-group.service';
import PromotionOnLocationService from './promotion-on-location/promotion-on-location.service';
import PacketDiscountService from './packet-discount/packet-discount.service';
import MarketingResearchService from './marketing-research/marketing-research.service';
import ServiceInPacketDiscountService from './service-in-packet-discount/service-in-packet-discount.service';
import ContractPatternService from './contract-pattern/contract-pattern.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('houseService') private houseService = () => new HouseService();
  @Provide('locationService') private locationService = () => new LocationService();
  @Provide('serviceService') private serviceService = () => new ServiceService();
  @Provide('serviceOnLocationService') private serviceOnLocationService = () => new ServiceOnLocationService();
  @Provide('tariffService') private tariffService = () => new TariffService();
  @Provide('tariffGroupService') private tariffGroupService = () => new TariffGroupService();
  @Provide('promotionOnLocationService') private promotionOnLocationService = () => new PromotionOnLocationService();
  @Provide('packetDiscountService') private packetDiscountService = () => new PacketDiscountService();
  @Provide('marketingResearchService') private marketingResearchService = () => new MarketingResearchService();
  @Provide('serviceInPacketDiscountService') private serviceInPacketDiscountService = () => new ServiceInPacketDiscountService();
  @Provide('contractPatternService') private contractPatternService = () => new ContractPatternService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}

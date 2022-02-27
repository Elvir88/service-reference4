import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const House = () => import('@/entities/house/house.vue');
// prettier-ignore
const HouseUpdate = () => import('@/entities/house/house-update.vue');
// prettier-ignore
const HouseDetails = () => import('@/entities/house/house-details.vue');
// prettier-ignore
const Location = () => import('@/entities/location/location.vue');
// prettier-ignore
const LocationUpdate = () => import('@/entities/location/location-update.vue');
// prettier-ignore
const LocationDetails = () => import('@/entities/location/location-details.vue');
// prettier-ignore
const Service = () => import('@/entities/service/service.vue');
// prettier-ignore
const ServiceUpdate = () => import('@/entities/service/service-update.vue');
// prettier-ignore
const ServiceDetails = () => import('@/entities/service/service-details.vue');
// prettier-ignore
const ServiceOnLocation = () => import('@/entities/service-on-location/service-on-location.vue');
// prettier-ignore
const ServiceOnLocationUpdate = () => import('@/entities/service-on-location/service-on-location-update.vue');
// prettier-ignore
const ServiceOnLocationDetails = () => import('@/entities/service-on-location/service-on-location-details.vue');
// prettier-ignore
const Tariff = () => import('@/entities/tariff/tariff.vue');
// prettier-ignore
const TariffUpdate = () => import('@/entities/tariff/tariff-update.vue');
// prettier-ignore
const TariffDetails = () => import('@/entities/tariff/tariff-details.vue');
// prettier-ignore
const TariffGroup = () => import('@/entities/tariff-group/tariff-group.vue');
// prettier-ignore
const TariffGroupUpdate = () => import('@/entities/tariff-group/tariff-group-update.vue');
// prettier-ignore
const TariffGroupDetails = () => import('@/entities/tariff-group/tariff-group-details.vue');
// prettier-ignore
const PromotionOnLocation = () => import('@/entities/promotion-on-location/promotion-on-location.vue');
// prettier-ignore
const PromotionOnLocationUpdate = () => import('@/entities/promotion-on-location/promotion-on-location-update.vue');
// prettier-ignore
const PromotionOnLocationDetails = () => import('@/entities/promotion-on-location/promotion-on-location-details.vue');
// prettier-ignore
const PacketDiscount = () => import('@/entities/packet-discount/packet-discount.vue');
// prettier-ignore
const PacketDiscountUpdate = () => import('@/entities/packet-discount/packet-discount-update.vue');
// prettier-ignore
const PacketDiscountDetails = () => import('@/entities/packet-discount/packet-discount-details.vue');
// prettier-ignore
const MarketingResearch = () => import('@/entities/marketing-research/marketing-research.vue');
// prettier-ignore
const MarketingResearchUpdate = () => import('@/entities/marketing-research/marketing-research-update.vue');
// prettier-ignore
const MarketingResearchDetails = () => import('@/entities/marketing-research/marketing-research-details.vue');
// prettier-ignore
const ServiceInPacketDiscount = () => import('@/entities/service-in-packet-discount/service-in-packet-discount.vue');
// prettier-ignore
const ServiceInPacketDiscountUpdate = () => import('@/entities/service-in-packet-discount/service-in-packet-discount-update.vue');
// prettier-ignore
const ServiceInPacketDiscountDetails = () => import('@/entities/service-in-packet-discount/service-in-packet-discount-details.vue');
// prettier-ignore
const ContractPattern = () => import('@/entities/contract-pattern/contract-pattern.vue');
// prettier-ignore
const ContractPatternUpdate = () => import('@/entities/contract-pattern/contract-pattern-update.vue');
// prettier-ignore
const ContractPatternDetails = () => import('@/entities/contract-pattern/contract-pattern-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'house',
      name: 'House',
      component: House,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'house/new',
      name: 'HouseCreate',
      component: HouseUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'house/:houseId/edit',
      name: 'HouseEdit',
      component: HouseUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'house/:houseId/view',
      name: 'HouseView',
      component: HouseDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'location',
      name: 'Location',
      component: Location,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'location/new',
      name: 'LocationCreate',
      component: LocationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'location/:locationId/edit',
      name: 'LocationEdit',
      component: LocationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'location/:locationId/view',
      name: 'LocationView',
      component: LocationDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service',
      name: 'Service',
      component: Service,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service/new',
      name: 'ServiceCreate',
      component: ServiceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service/:serviceId/edit',
      name: 'ServiceEdit',
      component: ServiceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service/:serviceId/view',
      name: 'ServiceView',
      component: ServiceDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service-on-location',
      name: 'ServiceOnLocation',
      component: ServiceOnLocation,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service-on-location/new',
      name: 'ServiceOnLocationCreate',
      component: ServiceOnLocationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service-on-location/:serviceOnLocationId/edit',
      name: 'ServiceOnLocationEdit',
      component: ServiceOnLocationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service-on-location/:serviceOnLocationId/view',
      name: 'ServiceOnLocationView',
      component: ServiceOnLocationDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tariff',
      name: 'Tariff',
      component: Tariff,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tariff/new',
      name: 'TariffCreate',
      component: TariffUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tariff/:tariffId/edit',
      name: 'TariffEdit',
      component: TariffUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tariff/:tariffId/view',
      name: 'TariffView',
      component: TariffDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tariff-group',
      name: 'TariffGroup',
      component: TariffGroup,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tariff-group/new',
      name: 'TariffGroupCreate',
      component: TariffGroupUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tariff-group/:tariffGroupId/edit',
      name: 'TariffGroupEdit',
      component: TariffGroupUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tariff-group/:tariffGroupId/view',
      name: 'TariffGroupView',
      component: TariffGroupDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'promotion-on-location',
      name: 'PromotionOnLocation',
      component: PromotionOnLocation,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'promotion-on-location/new',
      name: 'PromotionOnLocationCreate',
      component: PromotionOnLocationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'promotion-on-location/:promotionOnLocationId/edit',
      name: 'PromotionOnLocationEdit',
      component: PromotionOnLocationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'promotion-on-location/:promotionOnLocationId/view',
      name: 'PromotionOnLocationView',
      component: PromotionOnLocationDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'packet-discount',
      name: 'PacketDiscount',
      component: PacketDiscount,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'packet-discount/new',
      name: 'PacketDiscountCreate',
      component: PacketDiscountUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'packet-discount/:packetDiscountId/edit',
      name: 'PacketDiscountEdit',
      component: PacketDiscountUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'packet-discount/:packetDiscountId/view',
      name: 'PacketDiscountView',
      component: PacketDiscountDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'marketing-research',
      name: 'MarketingResearch',
      component: MarketingResearch,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'marketing-research/new',
      name: 'MarketingResearchCreate',
      component: MarketingResearchUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'marketing-research/:marketingResearchId/edit',
      name: 'MarketingResearchEdit',
      component: MarketingResearchUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'marketing-research/:marketingResearchId/view',
      name: 'MarketingResearchView',
      component: MarketingResearchDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service-in-packet-discount',
      name: 'ServiceInPacketDiscount',
      component: ServiceInPacketDiscount,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service-in-packet-discount/new',
      name: 'ServiceInPacketDiscountCreate',
      component: ServiceInPacketDiscountUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service-in-packet-discount/:serviceInPacketDiscountId/edit',
      name: 'ServiceInPacketDiscountEdit',
      component: ServiceInPacketDiscountUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service-in-packet-discount/:serviceInPacketDiscountId/view',
      name: 'ServiceInPacketDiscountView',
      component: ServiceInPacketDiscountDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'contract-pattern',
      name: 'ContractPattern',
      component: ContractPattern,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'contract-pattern/new',
      name: 'ContractPatternCreate',
      component: ContractPatternUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'contract-pattern/:contractPatternId/edit',
      name: 'ContractPatternEdit',
      component: ContractPatternUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'contract-pattern/:contractPatternId/view',
      name: 'ContractPatternView',
      component: ContractPatternDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};

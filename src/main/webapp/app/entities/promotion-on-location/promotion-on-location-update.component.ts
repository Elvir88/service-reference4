import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import PacketDiscountService from '@/entities/packet-discount/packet-discount.service';
import { IPacketDiscount } from '@/shared/model/packet-discount.model';

import MarketingResearchService from '@/entities/marketing-research/marketing-research.service';
import { IMarketingResearch } from '@/shared/model/marketing-research.model';

import LocationService from '@/entities/location/location.service';
import { ILocation } from '@/shared/model/location.model';

import { IPromotionOnLocation, PromotionOnLocation } from '@/shared/model/promotion-on-location.model';
import PromotionOnLocationService from './promotion-on-location.service';

const validations: any = {
  promotionOnLocation: {
    title: {
      required,
    },
    dateFrom: {
      required,
    },
    dateTo: {},
  },
};

@Component({
  validations,
})
export default class PromotionOnLocationUpdate extends Vue {
  @Inject('promotionOnLocationService') private promotionOnLocationService: () => PromotionOnLocationService;
  @Inject('alertService') private alertService: () => AlertService;

  public promotionOnLocation: IPromotionOnLocation = new PromotionOnLocation();

  @Inject('packetDiscountService') private packetDiscountService: () => PacketDiscountService;

  public packetDiscounts: IPacketDiscount[] = [];

  @Inject('marketingResearchService') private marketingResearchService: () => MarketingResearchService;

  public marketingResearches: IMarketingResearch[] = [];

  @Inject('locationService') private locationService: () => LocationService;

  public locations: ILocation[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.promotionOnLocationId) {
        vm.retrievePromotionOnLocation(to.params.promotionOnLocationId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.promotionOnLocation.id) {
      this.promotionOnLocationService()
        .update(this.promotionOnLocation)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.promotionOnLocation.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.promotionOnLocationService()
        .create(this.promotionOnLocation)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.promotionOnLocation.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.promotionOnLocation[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.promotionOnLocation[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.promotionOnLocation[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.promotionOnLocation[field] = null;
    }
  }

  public retrievePromotionOnLocation(promotionOnLocationId): void {
    this.promotionOnLocationService()
      .find(promotionOnLocationId)
      .then(res => {
        res.dateFrom = new Date(res.dateFrom);
        res.dateTo = new Date(res.dateTo);
        this.promotionOnLocation = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.packetDiscountService()
      .retrieve()
      .then(res => {
        this.packetDiscounts = res.data;
      });
    this.marketingResearchService()
      .retrieve()
      .then(res => {
        this.marketingResearches = res.data;
      });
    this.locationService()
      .retrieve()
      .then(res => {
        this.locations = res.data;
      });
  }
}

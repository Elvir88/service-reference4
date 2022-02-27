import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import HouseService from '@/entities/house/house.service';
import { IHouse } from '@/shared/model/house.model';

import ServiceOnLocationService from '@/entities/service-on-location/service-on-location.service';
import { IServiceOnLocation } from '@/shared/model/service-on-location.model';

import PromotionOnLocationService from '@/entities/promotion-on-location/promotion-on-location.service';
import { IPromotionOnLocation } from '@/shared/model/promotion-on-location.model';

import { ILocation, Location } from '@/shared/model/location.model';
import LocationService from './location.service';

const validations: any = {
  location: {
    title: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class LocationUpdate extends Vue {
  @Inject('locationService') private locationService: () => LocationService;
  @Inject('alertService') private alertService: () => AlertService;

  public location: ILocation = new Location();

  @Inject('houseService') private houseService: () => HouseService;

  public houses: IHouse[] = [];

  @Inject('serviceOnLocationService') private serviceOnLocationService: () => ServiceOnLocationService;

  public serviceOnLocations: IServiceOnLocation[] = [];

  @Inject('promotionOnLocationService') private promotionOnLocationService: () => PromotionOnLocationService;

  public promotionOnLocations: IPromotionOnLocation[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.locationId) {
        vm.retrieveLocation(to.params.locationId);
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
    if (this.location.id) {
      this.locationService()
        .update(this.location)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.location.updated', { param: param.id });
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
      this.locationService()
        .create(this.location)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.location.created', { param: param.id });
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

  public retrieveLocation(locationId): void {
    this.locationService()
      .find(locationId)
      .then(res => {
        this.location = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.houseService()
      .retrieve()
      .then(res => {
        this.houses = res.data;
      });
    this.serviceOnLocationService()
      .retrieve()
      .then(res => {
        this.serviceOnLocations = res.data;
      });
    this.promotionOnLocationService()
      .retrieve()
      .then(res => {
        this.promotionOnLocations = res.data;
      });
  }
}

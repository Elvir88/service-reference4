import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import ServiceService from '@/entities/service/service.service';
import { IService } from '@/shared/model/service.model';

import TariffGroupService from '@/entities/tariff-group/tariff-group.service';
import { ITariffGroup } from '@/shared/model/tariff-group.model';

import ContractPatternService from '@/entities/contract-pattern/contract-pattern.service';
import { IContractPattern } from '@/shared/model/contract-pattern.model';

import LocationService from '@/entities/location/location.service';
import { ILocation } from '@/shared/model/location.model';

import { IServiceOnLocation, ServiceOnLocation } from '@/shared/model/service-on-location.model';
import ServiceOnLocationService from './service-on-location.service';

const validations: any = {
  serviceOnLocation: {
    datefrom: {
      required,
    },
    dateTo: {},
  },
};

@Component({
  validations,
})
export default class ServiceOnLocationUpdate extends Vue {
  @Inject('serviceOnLocationService') private serviceOnLocationService: () => ServiceOnLocationService;
  @Inject('alertService') private alertService: () => AlertService;

  public serviceOnLocation: IServiceOnLocation = new ServiceOnLocation();

  @Inject('serviceService') private serviceService: () => ServiceService;

  public services: IService[] = [];

  @Inject('tariffGroupService') private tariffGroupService: () => TariffGroupService;

  public tariffGroups: ITariffGroup[] = [];

  @Inject('contractPatternService') private contractPatternService: () => ContractPatternService;

  public contractPatterns: IContractPattern[] = [];

  @Inject('locationService') private locationService: () => LocationService;

  public locations: ILocation[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.serviceOnLocationId) {
        vm.retrieveServiceOnLocation(to.params.serviceOnLocationId);
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
    if (this.serviceOnLocation.id) {
      this.serviceOnLocationService()
        .update(this.serviceOnLocation)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.serviceOnLocation.updated', { param: param.id });
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
      this.serviceOnLocationService()
        .create(this.serviceOnLocation)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.serviceOnLocation.created', { param: param.id });
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
      this.serviceOnLocation[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.serviceOnLocation[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.serviceOnLocation[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.serviceOnLocation[field] = null;
    }
  }

  public retrieveServiceOnLocation(serviceOnLocationId): void {
    this.serviceOnLocationService()
      .find(serviceOnLocationId)
      .then(res => {
        res.datefrom = new Date(res.datefrom);
        res.dateTo = new Date(res.dateTo);
        this.serviceOnLocation = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.serviceService()
      .retrieve()
      .then(res => {
        this.services = res.data;
      });
    this.tariffGroupService()
      .retrieve()
      .then(res => {
        this.tariffGroups = res.data;
      });
    this.contractPatternService()
      .retrieve()
      .then(res => {
        this.contractPatterns = res.data;
      });
    this.locationService()
      .retrieve()
      .then(res => {
        this.locations = res.data;
      });
  }
}

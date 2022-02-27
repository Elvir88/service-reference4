import { Component, Vue, Inject } from 'vue-property-decorator';

import { decimal, required, minValue, maxValue } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import ServiceService from '@/entities/service/service.service';
import { IService } from '@/shared/model/service.model';

import TariffService from '@/entities/tariff/tariff.service';
import { ITariff } from '@/shared/model/tariff.model';

import PacketDiscountService from '@/entities/packet-discount/packet-discount.service';
import { IPacketDiscount } from '@/shared/model/packet-discount.model';

import { IServiceInPacketDiscount, ServiceInPacketDiscount } from '@/shared/model/service-in-packet-discount.model';
import ServiceInPacketDiscountService from './service-in-packet-discount.service';

const validations: any = {
  serviceInPacketDiscount: {
    coefficient: {
      required,
      decimal,
      min: minValue(0),
      max: maxValue(1),
    },
  },
};

@Component({
  validations,
})
export default class ServiceInPacketDiscountUpdate extends Vue {
  @Inject('serviceInPacketDiscountService') private serviceInPacketDiscountService: () => ServiceInPacketDiscountService;
  @Inject('alertService') private alertService: () => AlertService;

  public serviceInPacketDiscount: IServiceInPacketDiscount = new ServiceInPacketDiscount();

  @Inject('serviceService') private serviceService: () => ServiceService;

  public services: IService[] = [];

  @Inject('tariffService') private tariffService: () => TariffService;

  public tariffs: ITariff[] = [];

  @Inject('packetDiscountService') private packetDiscountService: () => PacketDiscountService;

  public packetDiscounts: IPacketDiscount[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.serviceInPacketDiscountId) {
        vm.retrieveServiceInPacketDiscount(to.params.serviceInPacketDiscountId);
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
    if (this.serviceInPacketDiscount.id) {
      this.serviceInPacketDiscountService()
        .update(this.serviceInPacketDiscount)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.serviceInPacketDiscount.updated', { param: param.id });
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
      this.serviceInPacketDiscountService()
        .create(this.serviceInPacketDiscount)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.serviceInPacketDiscount.created', { param: param.id });
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

  public retrieveServiceInPacketDiscount(serviceInPacketDiscountId): void {
    this.serviceInPacketDiscountService()
      .find(serviceInPacketDiscountId)
      .then(res => {
        this.serviceInPacketDiscount = res;
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
    this.tariffService()
      .retrieve()
      .then(res => {
        this.tariffs = res.data;
      });
    this.packetDiscountService()
      .retrieve()
      .then(res => {
        this.packetDiscounts = res.data;
      });
  }
}

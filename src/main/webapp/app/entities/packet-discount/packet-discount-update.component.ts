import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, decimal } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import ServiceInPacketDiscountService from '@/entities/service-in-packet-discount/service-in-packet-discount.service';
import { IServiceInPacketDiscount } from '@/shared/model/service-in-packet-discount.model';

import { IPacketDiscount, PacketDiscount } from '@/shared/model/packet-discount.model';
import PacketDiscountService from './packet-discount.service';

const validations: any = {
  packetDiscount: {
    title: {
      required,
    },
    cost: {
      required,
      decimal,
    },
  },
};

@Component({
  validations,
})
export default class PacketDiscountUpdate extends Vue {
  @Inject('packetDiscountService') private packetDiscountService: () => PacketDiscountService;
  @Inject('alertService') private alertService: () => AlertService;

  public packetDiscount: IPacketDiscount = new PacketDiscount();

  @Inject('serviceInPacketDiscountService') private serviceInPacketDiscountService: () => ServiceInPacketDiscountService;

  public serviceInPacketDiscounts: IServiceInPacketDiscount[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.packetDiscountId) {
        vm.retrievePacketDiscount(to.params.packetDiscountId);
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
    if (this.packetDiscount.id) {
      this.packetDiscountService()
        .update(this.packetDiscount)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.packetDiscount.updated', { param: param.id });
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
      this.packetDiscountService()
        .create(this.packetDiscount)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.packetDiscount.created', { param: param.id });
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

  public retrievePacketDiscount(packetDiscountId): void {
    this.packetDiscountService()
      .find(packetDiscountId)
      .then(res => {
        this.packetDiscount = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.serviceInPacketDiscountService()
      .retrieve()
      .then(res => {
        this.serviceInPacketDiscounts = res.data;
      });
  }
}

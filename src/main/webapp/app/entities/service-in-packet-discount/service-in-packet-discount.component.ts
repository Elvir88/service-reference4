import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IServiceInPacketDiscount } from '@/shared/model/service-in-packet-discount.model';

import ServiceInPacketDiscountService from './service-in-packet-discount.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class ServiceInPacketDiscount extends Vue {
  @Inject('serviceInPacketDiscountService') private serviceInPacketDiscountService: () => ServiceInPacketDiscountService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public serviceInPacketDiscounts: IServiceInPacketDiscount[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllServiceInPacketDiscounts();
  }

  public clear(): void {
    this.retrieveAllServiceInPacketDiscounts();
  }

  public retrieveAllServiceInPacketDiscounts(): void {
    this.isFetching = true;
    this.serviceInPacketDiscountService()
      .retrieve()
      .then(
        res => {
          this.serviceInPacketDiscounts = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IServiceInPacketDiscount): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeServiceInPacketDiscount(): void {
    this.serviceInPacketDiscountService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('serviceReference4App.serviceInPacketDiscount.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllServiceInPacketDiscounts();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}

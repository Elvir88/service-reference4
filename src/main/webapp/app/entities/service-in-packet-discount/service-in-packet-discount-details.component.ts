import { Component, Vue, Inject } from 'vue-property-decorator';

import { IServiceInPacketDiscount } from '@/shared/model/service-in-packet-discount.model';
import ServiceInPacketDiscountService from './service-in-packet-discount.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ServiceInPacketDiscountDetails extends Vue {
  @Inject('serviceInPacketDiscountService') private serviceInPacketDiscountService: () => ServiceInPacketDiscountService;
  @Inject('alertService') private alertService: () => AlertService;

  public serviceInPacketDiscount: IServiceInPacketDiscount = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.serviceInPacketDiscountId) {
        vm.retrieveServiceInPacketDiscount(to.params.serviceInPacketDiscountId);
      }
    });
  }

  public retrieveServiceInPacketDiscount(serviceInPacketDiscountId) {
    this.serviceInPacketDiscountService()
      .find(serviceInPacketDiscountId)
      .then(res => {
        this.serviceInPacketDiscount = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

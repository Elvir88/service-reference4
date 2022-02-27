import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPacketDiscount } from '@/shared/model/packet-discount.model';
import PacketDiscountService from './packet-discount.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class PacketDiscountDetails extends Vue {
  @Inject('packetDiscountService') private packetDiscountService: () => PacketDiscountService;
  @Inject('alertService') private alertService: () => AlertService;

  public packetDiscount: IPacketDiscount = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.packetDiscountId) {
        vm.retrievePacketDiscount(to.params.packetDiscountId);
      }
    });
  }

  public retrievePacketDiscount(packetDiscountId) {
    this.packetDiscountService()
      .find(packetDiscountId)
      .then(res => {
        this.packetDiscount = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

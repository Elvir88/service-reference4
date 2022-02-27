import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPromotionOnLocation } from '@/shared/model/promotion-on-location.model';
import PromotionOnLocationService from './promotion-on-location.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class PromotionOnLocationDetails extends Vue {
  @Inject('promotionOnLocationService') private promotionOnLocationService: () => PromotionOnLocationService;
  @Inject('alertService') private alertService: () => AlertService;

  public promotionOnLocation: IPromotionOnLocation = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.promotionOnLocationId) {
        vm.retrievePromotionOnLocation(to.params.promotionOnLocationId);
      }
    });
  }

  public retrievePromotionOnLocation(promotionOnLocationId) {
    this.promotionOnLocationService()
      .find(promotionOnLocationId)
      .then(res => {
        this.promotionOnLocation = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

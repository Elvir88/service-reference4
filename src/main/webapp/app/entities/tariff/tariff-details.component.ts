import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITariff } from '@/shared/model/tariff.model';
import TariffService from './tariff.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TariffDetails extends Vue {
  @Inject('tariffService') private tariffService: () => TariffService;
  @Inject('alertService') private alertService: () => AlertService;

  public tariff: ITariff = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tariffId) {
        vm.retrieveTariff(to.params.tariffId);
      }
    });
  }

  public retrieveTariff(tariffId) {
    this.tariffService()
      .find(tariffId)
      .then(res => {
        this.tariff = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

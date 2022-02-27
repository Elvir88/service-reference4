import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITariffGroup } from '@/shared/model/tariff-group.model';
import TariffGroupService from './tariff-group.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TariffGroupDetails extends Vue {
  @Inject('tariffGroupService') private tariffGroupService: () => TariffGroupService;
  @Inject('alertService') private alertService: () => AlertService;

  public tariffGroup: ITariffGroup = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tariffGroupId) {
        vm.retrieveTariffGroup(to.params.tariffGroupId);
      }
    });
  }

  public retrieveTariffGroup(tariffGroupId) {
    this.tariffGroupService()
      .find(tariffGroupId)
      .then(res => {
        this.tariffGroup = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

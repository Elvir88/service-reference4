import { Component, Vue, Inject } from 'vue-property-decorator';

import { IHouse } from '@/shared/model/house.model';
import HouseService from './house.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class HouseDetails extends Vue {
  @Inject('houseService') private houseService: () => HouseService;
  @Inject('alertService') private alertService: () => AlertService;

  public house: IHouse = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.houseId) {
        vm.retrieveHouse(to.params.houseId);
      }
    });
  }

  public retrieveHouse(houseId) {
    this.houseService()
      .find(houseId)
      .then(res => {
        this.house = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

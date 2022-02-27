import { Component, Vue, Inject } from 'vue-property-decorator';

import { IServiceOnLocation } from '@/shared/model/service-on-location.model';
import ServiceOnLocationService from './service-on-location.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ServiceOnLocationDetails extends Vue {
  @Inject('serviceOnLocationService') private serviceOnLocationService: () => ServiceOnLocationService;
  @Inject('alertService') private alertService: () => AlertService;

  public serviceOnLocation: IServiceOnLocation = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.serviceOnLocationId) {
        vm.retrieveServiceOnLocation(to.params.serviceOnLocationId);
      }
    });
  }

  public retrieveServiceOnLocation(serviceOnLocationId) {
    this.serviceOnLocationService()
      .find(serviceOnLocationId)
      .then(res => {
        this.serviceOnLocation = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

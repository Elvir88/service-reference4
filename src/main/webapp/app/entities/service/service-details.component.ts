import { Component, Vue, Inject } from 'vue-property-decorator';

import { IService } from '@/shared/model/service.model';
import ServiceService from './service.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ServiceDetails extends Vue {
  @Inject('serviceService') private serviceService: () => ServiceService;
  @Inject('alertService') private alertService: () => AlertService;

  public service: IService = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.serviceId) {
        vm.retrieveService(to.params.serviceId);
      }
    });
  }

  public retrieveService(serviceId) {
    this.serviceService()
      .find(serviceId)
      .then(res => {
        this.service = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

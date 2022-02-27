import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import { IService, Service } from '@/shared/model/service.model';
import ServiceService from './service.service';

const validations: any = {
  service: {
    serviceId: {
      required,
    },
    title: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class ServiceUpdate extends Vue {
  @Inject('serviceService') private serviceService: () => ServiceService;
  @Inject('alertService') private alertService: () => AlertService;

  public service: IService = new Service();

  public services: IService[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.serviceId) {
        vm.retrieveService(to.params.serviceId);
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
    if (this.service.id) {
      this.serviceService()
        .update(this.service)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.service.updated', { param: param.id });
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
      this.serviceService()
        .create(this.service)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.service.created', { param: param.id });
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

  public retrieveService(serviceId): void {
    this.serviceService()
      .find(serviceId)
      .then(res => {
        this.service = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.serviceService()
      .retrieve()
      .then(res => {
        this.services = res.data;
      });
  }
}

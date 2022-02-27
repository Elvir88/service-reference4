import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import TariffService from '@/entities/tariff/tariff.service';
import { ITariff } from '@/shared/model/tariff.model';

import { ITariffGroup, TariffGroup } from '@/shared/model/tariff-group.model';
import TariffGroupService from './tariff-group.service';

const validations: any = {
  tariffGroup: {
    title: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class TariffGroupUpdate extends Vue {
  @Inject('tariffGroupService') private tariffGroupService: () => TariffGroupService;
  @Inject('alertService') private alertService: () => AlertService;

  public tariffGroup: ITariffGroup = new TariffGroup();

  @Inject('tariffService') private tariffService: () => TariffService;

  public tariffs: ITariff[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tariffGroupId) {
        vm.retrieveTariffGroup(to.params.tariffGroupId);
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
    if (this.tariffGroup.id) {
      this.tariffGroupService()
        .update(this.tariffGroup)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.tariffGroup.updated', { param: param.id });
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
      this.tariffGroupService()
        .create(this.tariffGroup)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.tariffGroup.created', { param: param.id });
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

  public retrieveTariffGroup(tariffGroupId): void {
    this.tariffGroupService()
      .find(tariffGroupId)
      .then(res => {
        this.tariffGroup = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.tariffService()
      .retrieve()
      .then(res => {
        this.tariffs = res.data;
      });
  }
}

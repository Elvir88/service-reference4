import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, decimal } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import TariffGroupService from '@/entities/tariff-group/tariff-group.service';
import { ITariffGroup } from '@/shared/model/tariff-group.model';

import { ITariff, Tariff } from '@/shared/model/tariff.model';
import TariffService from './tariff.service';

const validations: any = {
  tariff: {
    title: {
      required,
    },
    cost: {
      required,
      decimal,
    },
  },
};

@Component({
  validations,
})
export default class TariffUpdate extends Vue {
  @Inject('tariffService') private tariffService: () => TariffService;
  @Inject('alertService') private alertService: () => AlertService;

  public tariff: ITariff = new Tariff();

  @Inject('tariffGroupService') private tariffGroupService: () => TariffGroupService;

  public tariffGroups: ITariffGroup[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tariffId) {
        vm.retrieveTariff(to.params.tariffId);
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
    this.tariff.tariffGroups = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.tariff.id) {
      this.tariffService()
        .update(this.tariff)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.tariff.updated', { param: param.id });
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
      this.tariffService()
        .create(this.tariff)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.tariff.created', { param: param.id });
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

  public retrieveTariff(tariffId): void {
    this.tariffService()
      .find(tariffId)
      .then(res => {
        this.tariff = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.tariffGroupService()
      .retrieve()
      .then(res => {
        this.tariffGroups = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}

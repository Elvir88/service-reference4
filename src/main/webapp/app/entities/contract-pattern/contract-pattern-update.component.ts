import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, numeric } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import { IContractPattern, ContractPattern } from '@/shared/model/contract-pattern.model';
import ContractPatternService from './contract-pattern.service';

const validations: any = {
  contractPattern: {
    title: {
      required,
    },
    patternId: {
      required,
      numeric,
    },
  },
};

@Component({
  validations,
})
export default class ContractPatternUpdate extends Vue {
  @Inject('contractPatternService') private contractPatternService: () => ContractPatternService;
  @Inject('alertService') private alertService: () => AlertService;

  public contractPattern: IContractPattern = new ContractPattern();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.contractPatternId) {
        vm.retrieveContractPattern(to.params.contractPatternId);
      }
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
    if (this.contractPattern.id) {
      this.contractPatternService()
        .update(this.contractPattern)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.contractPattern.updated', { param: param.id });
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
      this.contractPatternService()
        .create(this.contractPattern)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.contractPattern.created', { param: param.id });
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

  public retrieveContractPattern(contractPatternId): void {
    this.contractPatternService()
      .find(contractPatternId)
      .then(res => {
        this.contractPattern = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}

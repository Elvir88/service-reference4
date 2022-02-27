import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import ServiceService from '@/entities/service/service.service';
import { IService } from '@/shared/model/service.model';

import TariffService from '@/entities/tariff/tariff.service';
import { ITariff } from '@/shared/model/tariff.model';

import { IMarketingResearch, MarketingResearch } from '@/shared/model/marketing-research.model';
import MarketingResearchService from './marketing-research.service';

const validations: any = {
  marketingResearch: {
    title: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class MarketingResearchUpdate extends Vue {
  @Inject('marketingResearchService') private marketingResearchService: () => MarketingResearchService;
  @Inject('alertService') private alertService: () => AlertService;

  public marketingResearch: IMarketingResearch = new MarketingResearch();

  @Inject('serviceService') private serviceService: () => ServiceService;

  public services: IService[] = [];

  @Inject('tariffService') private tariffService: () => TariffService;

  public tariffs: ITariff[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.marketingResearchId) {
        vm.retrieveMarketingResearch(to.params.marketingResearchId);
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
    if (this.marketingResearch.id) {
      this.marketingResearchService()
        .update(this.marketingResearch)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.marketingResearch.updated', { param: param.id });
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
      this.marketingResearchService()
        .create(this.marketingResearch)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('serviceReference4App.marketingResearch.created', { param: param.id });
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

  public retrieveMarketingResearch(marketingResearchId): void {
    this.marketingResearchService()
      .find(marketingResearchId)
      .then(res => {
        this.marketingResearch = res;
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
    this.tariffService()
      .retrieve()
      .then(res => {
        this.tariffs = res.data;
      });
  }
}

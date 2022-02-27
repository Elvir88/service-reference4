import { Component, Vue, Inject } from 'vue-property-decorator';

import { IMarketingResearch } from '@/shared/model/marketing-research.model';
import MarketingResearchService from './marketing-research.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class MarketingResearchDetails extends Vue {
  @Inject('marketingResearchService') private marketingResearchService: () => MarketingResearchService;
  @Inject('alertService') private alertService: () => AlertService;

  public marketingResearch: IMarketingResearch = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.marketingResearchId) {
        vm.retrieveMarketingResearch(to.params.marketingResearchId);
      }
    });
  }

  public retrieveMarketingResearch(marketingResearchId) {
    this.marketingResearchService()
      .find(marketingResearchId)
      .then(res => {
        this.marketingResearch = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

import { Component, Vue, Inject } from 'vue-property-decorator';

import { IContractPattern } from '@/shared/model/contract-pattern.model';
import ContractPatternService from './contract-pattern.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ContractPatternDetails extends Vue {
  @Inject('contractPatternService') private contractPatternService: () => ContractPatternService;
  @Inject('alertService') private alertService: () => AlertService;

  public contractPattern: IContractPattern = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.contractPatternId) {
        vm.retrieveContractPattern(to.params.contractPatternId);
      }
    });
  }

  public retrieveContractPattern(contractPatternId) {
    this.contractPatternService()
      .find(contractPatternId)
      .then(res => {
        this.contractPattern = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

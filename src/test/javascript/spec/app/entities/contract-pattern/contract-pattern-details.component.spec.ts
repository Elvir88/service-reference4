/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ContractPatternDetailComponent from '@/entities/contract-pattern/contract-pattern-details.vue';
import ContractPatternClass from '@/entities/contract-pattern/contract-pattern-details.component';
import ContractPatternService from '@/entities/contract-pattern/contract-pattern.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('ContractPattern Management Detail Component', () => {
    let wrapper: Wrapper<ContractPatternClass>;
    let comp: ContractPatternClass;
    let contractPatternServiceStub: SinonStubbedInstance<ContractPatternService>;

    beforeEach(() => {
      contractPatternServiceStub = sinon.createStubInstance<ContractPatternService>(ContractPatternService);

      wrapper = shallowMount<ContractPatternClass>(ContractPatternDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { contractPatternService: () => contractPatternServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundContractPattern = { id: 123 };
        contractPatternServiceStub.find.resolves(foundContractPattern);

        // WHEN
        comp.retrieveContractPattern(123);
        await comp.$nextTick();

        // THEN
        expect(comp.contractPattern).toBe(foundContractPattern);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundContractPattern = { id: 123 };
        contractPatternServiceStub.find.resolves(foundContractPattern);

        // WHEN
        comp.beforeRouteEnter({ params: { contractPatternId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.contractPattern).toBe(foundContractPattern);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});

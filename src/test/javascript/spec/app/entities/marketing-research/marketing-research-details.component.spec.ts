/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import MarketingResearchDetailComponent from '@/entities/marketing-research/marketing-research-details.vue';
import MarketingResearchClass from '@/entities/marketing-research/marketing-research-details.component';
import MarketingResearchService from '@/entities/marketing-research/marketing-research.service';
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
  describe('MarketingResearch Management Detail Component', () => {
    let wrapper: Wrapper<MarketingResearchClass>;
    let comp: MarketingResearchClass;
    let marketingResearchServiceStub: SinonStubbedInstance<MarketingResearchService>;

    beforeEach(() => {
      marketingResearchServiceStub = sinon.createStubInstance<MarketingResearchService>(MarketingResearchService);

      wrapper = shallowMount<MarketingResearchClass>(MarketingResearchDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { marketingResearchService: () => marketingResearchServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundMarketingResearch = { id: 123 };
        marketingResearchServiceStub.find.resolves(foundMarketingResearch);

        // WHEN
        comp.retrieveMarketingResearch(123);
        await comp.$nextTick();

        // THEN
        expect(comp.marketingResearch).toBe(foundMarketingResearch);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundMarketingResearch = { id: 123 };
        marketingResearchServiceStub.find.resolves(foundMarketingResearch);

        // WHEN
        comp.beforeRouteEnter({ params: { marketingResearchId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.marketingResearch).toBe(foundMarketingResearch);
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

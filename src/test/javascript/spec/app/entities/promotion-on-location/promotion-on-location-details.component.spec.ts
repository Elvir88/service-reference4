/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PromotionOnLocationDetailComponent from '@/entities/promotion-on-location/promotion-on-location-details.vue';
import PromotionOnLocationClass from '@/entities/promotion-on-location/promotion-on-location-details.component';
import PromotionOnLocationService from '@/entities/promotion-on-location/promotion-on-location.service';
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
  describe('PromotionOnLocation Management Detail Component', () => {
    let wrapper: Wrapper<PromotionOnLocationClass>;
    let comp: PromotionOnLocationClass;
    let promotionOnLocationServiceStub: SinonStubbedInstance<PromotionOnLocationService>;

    beforeEach(() => {
      promotionOnLocationServiceStub = sinon.createStubInstance<PromotionOnLocationService>(PromotionOnLocationService);

      wrapper = shallowMount<PromotionOnLocationClass>(PromotionOnLocationDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { promotionOnLocationService: () => promotionOnLocationServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPromotionOnLocation = { id: 123 };
        promotionOnLocationServiceStub.find.resolves(foundPromotionOnLocation);

        // WHEN
        comp.retrievePromotionOnLocation(123);
        await comp.$nextTick();

        // THEN
        expect(comp.promotionOnLocation).toBe(foundPromotionOnLocation);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPromotionOnLocation = { id: 123 };
        promotionOnLocationServiceStub.find.resolves(foundPromotionOnLocation);

        // WHEN
        comp.beforeRouteEnter({ params: { promotionOnLocationId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.promotionOnLocation).toBe(foundPromotionOnLocation);
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

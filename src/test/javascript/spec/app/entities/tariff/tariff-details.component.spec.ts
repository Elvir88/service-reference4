/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TariffDetailComponent from '@/entities/tariff/tariff-details.vue';
import TariffClass from '@/entities/tariff/tariff-details.component';
import TariffService from '@/entities/tariff/tariff.service';
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
  describe('Tariff Management Detail Component', () => {
    let wrapper: Wrapper<TariffClass>;
    let comp: TariffClass;
    let tariffServiceStub: SinonStubbedInstance<TariffService>;

    beforeEach(() => {
      tariffServiceStub = sinon.createStubInstance<TariffService>(TariffService);

      wrapper = shallowMount<TariffClass>(TariffDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { tariffService: () => tariffServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTariff = { id: 123 };
        tariffServiceStub.find.resolves(foundTariff);

        // WHEN
        comp.retrieveTariff(123);
        await comp.$nextTick();

        // THEN
        expect(comp.tariff).toBe(foundTariff);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTariff = { id: 123 };
        tariffServiceStub.find.resolves(foundTariff);

        // WHEN
        comp.beforeRouteEnter({ params: { tariffId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.tariff).toBe(foundTariff);
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

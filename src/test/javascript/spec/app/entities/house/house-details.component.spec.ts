/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import HouseDetailComponent from '@/entities/house/house-details.vue';
import HouseClass from '@/entities/house/house-details.component';
import HouseService from '@/entities/house/house.service';
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
  describe('House Management Detail Component', () => {
    let wrapper: Wrapper<HouseClass>;
    let comp: HouseClass;
    let houseServiceStub: SinonStubbedInstance<HouseService>;

    beforeEach(() => {
      houseServiceStub = sinon.createStubInstance<HouseService>(HouseService);

      wrapper = shallowMount<HouseClass>(HouseDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { houseService: () => houseServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundHouse = { id: 123 };
        houseServiceStub.find.resolves(foundHouse);

        // WHEN
        comp.retrieveHouse(123);
        await comp.$nextTick();

        // THEN
        expect(comp.house).toBe(foundHouse);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundHouse = { id: 123 };
        houseServiceStub.find.resolves(foundHouse);

        // WHEN
        comp.beforeRouteEnter({ params: { houseId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.house).toBe(foundHouse);
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

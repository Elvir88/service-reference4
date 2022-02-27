/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ServiceOnLocationDetailComponent from '@/entities/service-on-location/service-on-location-details.vue';
import ServiceOnLocationClass from '@/entities/service-on-location/service-on-location-details.component';
import ServiceOnLocationService from '@/entities/service-on-location/service-on-location.service';
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
  describe('ServiceOnLocation Management Detail Component', () => {
    let wrapper: Wrapper<ServiceOnLocationClass>;
    let comp: ServiceOnLocationClass;
    let serviceOnLocationServiceStub: SinonStubbedInstance<ServiceOnLocationService>;

    beforeEach(() => {
      serviceOnLocationServiceStub = sinon.createStubInstance<ServiceOnLocationService>(ServiceOnLocationService);

      wrapper = shallowMount<ServiceOnLocationClass>(ServiceOnLocationDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { serviceOnLocationService: () => serviceOnLocationServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundServiceOnLocation = { id: 123 };
        serviceOnLocationServiceStub.find.resolves(foundServiceOnLocation);

        // WHEN
        comp.retrieveServiceOnLocation(123);
        await comp.$nextTick();

        // THEN
        expect(comp.serviceOnLocation).toBe(foundServiceOnLocation);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundServiceOnLocation = { id: 123 };
        serviceOnLocationServiceStub.find.resolves(foundServiceOnLocation);

        // WHEN
        comp.beforeRouteEnter({ params: { serviceOnLocationId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.serviceOnLocation).toBe(foundServiceOnLocation);
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

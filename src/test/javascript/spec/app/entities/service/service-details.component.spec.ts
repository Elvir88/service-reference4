/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ServiceDetailComponent from '@/entities/service/service-details.vue';
import ServiceClass from '@/entities/service/service-details.component';
import ServiceService from '@/entities/service/service.service';
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
  describe('Service Management Detail Component', () => {
    let wrapper: Wrapper<ServiceClass>;
    let comp: ServiceClass;
    let serviceServiceStub: SinonStubbedInstance<ServiceService>;

    beforeEach(() => {
      serviceServiceStub = sinon.createStubInstance<ServiceService>(ServiceService);

      wrapper = shallowMount<ServiceClass>(ServiceDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { serviceService: () => serviceServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundService = { id: 123 };
        serviceServiceStub.find.resolves(foundService);

        // WHEN
        comp.retrieveService(123);
        await comp.$nextTick();

        // THEN
        expect(comp.service).toBe(foundService);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundService = { id: 123 };
        serviceServiceStub.find.resolves(foundService);

        // WHEN
        comp.beforeRouteEnter({ params: { serviceId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.service).toBe(foundService);
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

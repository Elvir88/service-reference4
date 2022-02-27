/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ServiceInPacketDiscountDetailComponent from '@/entities/service-in-packet-discount/service-in-packet-discount-details.vue';
import ServiceInPacketDiscountClass from '@/entities/service-in-packet-discount/service-in-packet-discount-details.component';
import ServiceInPacketDiscountService from '@/entities/service-in-packet-discount/service-in-packet-discount.service';
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
  describe('ServiceInPacketDiscount Management Detail Component', () => {
    let wrapper: Wrapper<ServiceInPacketDiscountClass>;
    let comp: ServiceInPacketDiscountClass;
    let serviceInPacketDiscountServiceStub: SinonStubbedInstance<ServiceInPacketDiscountService>;

    beforeEach(() => {
      serviceInPacketDiscountServiceStub = sinon.createStubInstance<ServiceInPacketDiscountService>(ServiceInPacketDiscountService);

      wrapper = shallowMount<ServiceInPacketDiscountClass>(ServiceInPacketDiscountDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { serviceInPacketDiscountService: () => serviceInPacketDiscountServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundServiceInPacketDiscount = { id: 123 };
        serviceInPacketDiscountServiceStub.find.resolves(foundServiceInPacketDiscount);

        // WHEN
        comp.retrieveServiceInPacketDiscount(123);
        await comp.$nextTick();

        // THEN
        expect(comp.serviceInPacketDiscount).toBe(foundServiceInPacketDiscount);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundServiceInPacketDiscount = { id: 123 };
        serviceInPacketDiscountServiceStub.find.resolves(foundServiceInPacketDiscount);

        // WHEN
        comp.beforeRouteEnter({ params: { serviceInPacketDiscountId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.serviceInPacketDiscount).toBe(foundServiceInPacketDiscount);
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

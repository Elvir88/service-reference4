/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ServiceInPacketDiscountUpdateComponent from '@/entities/service-in-packet-discount/service-in-packet-discount-update.vue';
import ServiceInPacketDiscountClass from '@/entities/service-in-packet-discount/service-in-packet-discount-update.component';
import ServiceInPacketDiscountService from '@/entities/service-in-packet-discount/service-in-packet-discount.service';

import ServiceService from '@/entities/service/service.service';

import TariffService from '@/entities/tariff/tariff.service';

import PacketDiscountService from '@/entities/packet-discount/packet-discount.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('ServiceInPacketDiscount Management Update Component', () => {
    let wrapper: Wrapper<ServiceInPacketDiscountClass>;
    let comp: ServiceInPacketDiscountClass;
    let serviceInPacketDiscountServiceStub: SinonStubbedInstance<ServiceInPacketDiscountService>;

    beforeEach(() => {
      serviceInPacketDiscountServiceStub = sinon.createStubInstance<ServiceInPacketDiscountService>(ServiceInPacketDiscountService);

      wrapper = shallowMount<ServiceInPacketDiscountClass>(ServiceInPacketDiscountUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          serviceInPacketDiscountService: () => serviceInPacketDiscountServiceStub,
          alertService: () => new AlertService(),

          serviceService: () =>
            sinon.createStubInstance<ServiceService>(ServiceService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          tariffService: () =>
            sinon.createStubInstance<TariffService>(TariffService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          packetDiscountService: () =>
            sinon.createStubInstance<PacketDiscountService>(PacketDiscountService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.serviceInPacketDiscount = entity;
        serviceInPacketDiscountServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(serviceInPacketDiscountServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.serviceInPacketDiscount = entity;
        serviceInPacketDiscountServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(serviceInPacketDiscountServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundServiceInPacketDiscount = { id: 123 };
        serviceInPacketDiscountServiceStub.find.resolves(foundServiceInPacketDiscount);
        serviceInPacketDiscountServiceStub.retrieve.resolves([foundServiceInPacketDiscount]);

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

/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import PacketDiscountUpdateComponent from '@/entities/packet-discount/packet-discount-update.vue';
import PacketDiscountClass from '@/entities/packet-discount/packet-discount-update.component';
import PacketDiscountService from '@/entities/packet-discount/packet-discount.service';

import ServiceInPacketDiscountService from '@/entities/service-in-packet-discount/service-in-packet-discount.service';
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
  describe('PacketDiscount Management Update Component', () => {
    let wrapper: Wrapper<PacketDiscountClass>;
    let comp: PacketDiscountClass;
    let packetDiscountServiceStub: SinonStubbedInstance<PacketDiscountService>;

    beforeEach(() => {
      packetDiscountServiceStub = sinon.createStubInstance<PacketDiscountService>(PacketDiscountService);

      wrapper = shallowMount<PacketDiscountClass>(PacketDiscountUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          packetDiscountService: () => packetDiscountServiceStub,
          alertService: () => new AlertService(),

          serviceInPacketDiscountService: () =>
            sinon.createStubInstance<ServiceInPacketDiscountService>(ServiceInPacketDiscountService, {
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
        comp.packetDiscount = entity;
        packetDiscountServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(packetDiscountServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.packetDiscount = entity;
        packetDiscountServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(packetDiscountServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPacketDiscount = { id: 123 };
        packetDiscountServiceStub.find.resolves(foundPacketDiscount);
        packetDiscountServiceStub.retrieve.resolves([foundPacketDiscount]);

        // WHEN
        comp.beforeRouteEnter({ params: { packetDiscountId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.packetDiscount).toBe(foundPacketDiscount);
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

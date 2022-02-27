/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PacketDiscountDetailComponent from '@/entities/packet-discount/packet-discount-details.vue';
import PacketDiscountClass from '@/entities/packet-discount/packet-discount-details.component';
import PacketDiscountService from '@/entities/packet-discount/packet-discount.service';
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
  describe('PacketDiscount Management Detail Component', () => {
    let wrapper: Wrapper<PacketDiscountClass>;
    let comp: PacketDiscountClass;
    let packetDiscountServiceStub: SinonStubbedInstance<PacketDiscountService>;

    beforeEach(() => {
      packetDiscountServiceStub = sinon.createStubInstance<PacketDiscountService>(PacketDiscountService);

      wrapper = shallowMount<PacketDiscountClass>(PacketDiscountDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { packetDiscountService: () => packetDiscountServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPacketDiscount = { id: 123 };
        packetDiscountServiceStub.find.resolves(foundPacketDiscount);

        // WHEN
        comp.retrievePacketDiscount(123);
        await comp.$nextTick();

        // THEN
        expect(comp.packetDiscount).toBe(foundPacketDiscount);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPacketDiscount = { id: 123 };
        packetDiscountServiceStub.find.resolves(foundPacketDiscount);

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

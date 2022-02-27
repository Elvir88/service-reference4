/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ServiceInPacketDiscountComponent from '@/entities/service-in-packet-discount/service-in-packet-discount.vue';
import ServiceInPacketDiscountClass from '@/entities/service-in-packet-discount/service-in-packet-discount.component';
import ServiceInPacketDiscountService from '@/entities/service-in-packet-discount/service-in-packet-discount.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('ServiceInPacketDiscount Management Component', () => {
    let wrapper: Wrapper<ServiceInPacketDiscountClass>;
    let comp: ServiceInPacketDiscountClass;
    let serviceInPacketDiscountServiceStub: SinonStubbedInstance<ServiceInPacketDiscountService>;

    beforeEach(() => {
      serviceInPacketDiscountServiceStub = sinon.createStubInstance<ServiceInPacketDiscountService>(ServiceInPacketDiscountService);
      serviceInPacketDiscountServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ServiceInPacketDiscountClass>(ServiceInPacketDiscountComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          serviceInPacketDiscountService: () => serviceInPacketDiscountServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      serviceInPacketDiscountServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllServiceInPacketDiscounts();
      await comp.$nextTick();

      // THEN
      expect(serviceInPacketDiscountServiceStub.retrieve.called).toBeTruthy();
      expect(comp.serviceInPacketDiscounts[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      serviceInPacketDiscountServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(serviceInPacketDiscountServiceStub.retrieve.callCount).toEqual(1);

      comp.removeServiceInPacketDiscount();
      await comp.$nextTick();

      // THEN
      expect(serviceInPacketDiscountServiceStub.delete.called).toBeTruthy();
      expect(serviceInPacketDiscountServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});

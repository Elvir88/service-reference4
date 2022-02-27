/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import ServiceOnLocationUpdateComponent from '@/entities/service-on-location/service-on-location-update.vue';
import ServiceOnLocationClass from '@/entities/service-on-location/service-on-location-update.component';
import ServiceOnLocationService from '@/entities/service-on-location/service-on-location.service';

import ServiceService from '@/entities/service/service.service';

import TariffGroupService from '@/entities/tariff-group/tariff-group.service';

import ContractPatternService from '@/entities/contract-pattern/contract-pattern.service';

import LocationService from '@/entities/location/location.service';
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
  describe('ServiceOnLocation Management Update Component', () => {
    let wrapper: Wrapper<ServiceOnLocationClass>;
    let comp: ServiceOnLocationClass;
    let serviceOnLocationServiceStub: SinonStubbedInstance<ServiceOnLocationService>;

    beforeEach(() => {
      serviceOnLocationServiceStub = sinon.createStubInstance<ServiceOnLocationService>(ServiceOnLocationService);

      wrapper = shallowMount<ServiceOnLocationClass>(ServiceOnLocationUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          serviceOnLocationService: () => serviceOnLocationServiceStub,
          alertService: () => new AlertService(),

          serviceService: () =>
            sinon.createStubInstance<ServiceService>(ServiceService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          tariffGroupService: () =>
            sinon.createStubInstance<TariffGroupService>(TariffGroupService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          contractPatternService: () =>
            sinon.createStubInstance<ContractPatternService>(ContractPatternService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          locationService: () =>
            sinon.createStubInstance<LocationService>(LocationService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('load', () => {
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.serviceOnLocation = entity;
        serviceOnLocationServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(serviceOnLocationServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.serviceOnLocation = entity;
        serviceOnLocationServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(serviceOnLocationServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundServiceOnLocation = { id: 123 };
        serviceOnLocationServiceStub.find.resolves(foundServiceOnLocation);
        serviceOnLocationServiceStub.retrieve.resolves([foundServiceOnLocation]);

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

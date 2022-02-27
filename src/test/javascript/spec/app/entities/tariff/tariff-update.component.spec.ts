/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TariffUpdateComponent from '@/entities/tariff/tariff-update.vue';
import TariffClass from '@/entities/tariff/tariff-update.component';
import TariffService from '@/entities/tariff/tariff.service';

import TariffGroupService from '@/entities/tariff-group/tariff-group.service';
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
  describe('Tariff Management Update Component', () => {
    let wrapper: Wrapper<TariffClass>;
    let comp: TariffClass;
    let tariffServiceStub: SinonStubbedInstance<TariffService>;

    beforeEach(() => {
      tariffServiceStub = sinon.createStubInstance<TariffService>(TariffService);

      wrapper = shallowMount<TariffClass>(TariffUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          tariffService: () => tariffServiceStub,
          alertService: () => new AlertService(),

          tariffGroupService: () =>
            sinon.createStubInstance<TariffGroupService>(TariffGroupService, {
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
        comp.tariff = entity;
        tariffServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tariffServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.tariff = entity;
        tariffServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tariffServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTariff = { id: 123 };
        tariffServiceStub.find.resolves(foundTariff);
        tariffServiceStub.retrieve.resolves([foundTariff]);

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

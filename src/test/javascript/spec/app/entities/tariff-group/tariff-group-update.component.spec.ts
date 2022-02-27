/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TariffGroupUpdateComponent from '@/entities/tariff-group/tariff-group-update.vue';
import TariffGroupClass from '@/entities/tariff-group/tariff-group-update.component';
import TariffGroupService from '@/entities/tariff-group/tariff-group.service';

import TariffService from '@/entities/tariff/tariff.service';
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
  describe('TariffGroup Management Update Component', () => {
    let wrapper: Wrapper<TariffGroupClass>;
    let comp: TariffGroupClass;
    let tariffGroupServiceStub: SinonStubbedInstance<TariffGroupService>;

    beforeEach(() => {
      tariffGroupServiceStub = sinon.createStubInstance<TariffGroupService>(TariffGroupService);

      wrapper = shallowMount<TariffGroupClass>(TariffGroupUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          tariffGroupService: () => tariffGroupServiceStub,
          alertService: () => new AlertService(),

          tariffService: () =>
            sinon.createStubInstance<TariffService>(TariffService, {
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
        comp.tariffGroup = entity;
        tariffGroupServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tariffGroupServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.tariffGroup = entity;
        tariffGroupServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tariffGroupServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTariffGroup = { id: 123 };
        tariffGroupServiceStub.find.resolves(foundTariffGroup);
        tariffGroupServiceStub.retrieve.resolves([foundTariffGroup]);

        // WHEN
        comp.beforeRouteEnter({ params: { tariffGroupId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.tariffGroup).toBe(foundTariffGroup);
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

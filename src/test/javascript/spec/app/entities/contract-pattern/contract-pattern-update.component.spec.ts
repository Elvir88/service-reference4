/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ContractPatternUpdateComponent from '@/entities/contract-pattern/contract-pattern-update.vue';
import ContractPatternClass from '@/entities/contract-pattern/contract-pattern-update.component';
import ContractPatternService from '@/entities/contract-pattern/contract-pattern.service';

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
  describe('ContractPattern Management Update Component', () => {
    let wrapper: Wrapper<ContractPatternClass>;
    let comp: ContractPatternClass;
    let contractPatternServiceStub: SinonStubbedInstance<ContractPatternService>;

    beforeEach(() => {
      contractPatternServiceStub = sinon.createStubInstance<ContractPatternService>(ContractPatternService);

      wrapper = shallowMount<ContractPatternClass>(ContractPatternUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          contractPatternService: () => contractPatternServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.contractPattern = entity;
        contractPatternServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(contractPatternServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.contractPattern = entity;
        contractPatternServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(contractPatternServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundContractPattern = { id: 123 };
        contractPatternServiceStub.find.resolves(foundContractPattern);
        contractPatternServiceStub.retrieve.resolves([foundContractPattern]);

        // WHEN
        comp.beforeRouteEnter({ params: { contractPatternId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.contractPattern).toBe(foundContractPattern);
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

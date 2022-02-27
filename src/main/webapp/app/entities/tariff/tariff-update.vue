<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="serviceReference4App.tariff.home.createOrEditLabel"
          data-cy="TariffCreateUpdateHeading"
          v-text="$t('serviceReference4App.tariff.home.createOrEditLabel')"
        >
          Create or edit a Tariff
        </h2>
        <div>
          <div class="form-group" v-if="tariff.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="tariff.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.tariff.title')" for="tariff-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="tariff-title"
              data-cy="title"
              :class="{ valid: !$v.tariff.title.$invalid, invalid: $v.tariff.title.$invalid }"
              v-model="$v.tariff.title.$model"
              required
            />
            <div v-if="$v.tariff.title.$anyDirty && $v.tariff.title.$invalid">
              <small class="form-text text-danger" v-if="!$v.tariff.title.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.tariff.cost')" for="tariff-cost">Cost</label>
            <input
              type="number"
              class="form-control"
              name="cost"
              id="tariff-cost"
              data-cy="cost"
              :class="{ valid: !$v.tariff.cost.$invalid, invalid: $v.tariff.cost.$invalid }"
              v-model.number="$v.tariff.cost.$model"
              required
            />
            <div v-if="$v.tariff.cost.$anyDirty && $v.tariff.cost.$invalid">
              <small class="form-text text-danger" v-if="!$v.tariff.cost.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.tariff.cost.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label v-text="$t('serviceReference4App.tariff.tariffGroup')" for="tariff-tariffGroup">Tariff Group</label>
            <select
              class="form-control"
              id="tariff-tariffGroups"
              data-cy="tariffGroup"
              multiple
              name="tariffGroup"
              v-if="tariff.tariffGroups !== undefined"
              v-model="tariff.tariffGroups"
            >
              <option
                v-bind:value="getSelected(tariff.tariffGroups, tariffGroupOption)"
                v-for="tariffGroupOption in tariffGroups"
                :key="tariffGroupOption.id"
              >
                {{ tariffGroupOption.title }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.tariff.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./tariff-update.component.ts"></script>

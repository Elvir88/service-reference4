<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="serviceReference4App.tariffGroup.home.createOrEditLabel"
          data-cy="TariffGroupCreateUpdateHeading"
          v-text="$t('serviceReference4App.tariffGroup.home.createOrEditLabel')"
        >
          Create or edit a TariffGroup
        </h2>
        <div>
          <div class="form-group" v-if="tariffGroup.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="tariffGroup.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.tariffGroup.title')" for="tariff-group-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="tariff-group-title"
              data-cy="title"
              :class="{ valid: !$v.tariffGroup.title.$invalid, invalid: $v.tariffGroup.title.$invalid }"
              v-model="$v.tariffGroup.title.$model"
              required
            />
            <div v-if="$v.tariffGroup.title.$anyDirty && $v.tariffGroup.title.$invalid">
              <small class="form-text text-danger" v-if="!$v.tariffGroup.title.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
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
            :disabled="$v.tariffGroup.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./tariff-group-update.component.ts"></script>

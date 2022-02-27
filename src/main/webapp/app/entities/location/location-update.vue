<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="serviceReference4App.location.home.createOrEditLabel"
          data-cy="LocationCreateUpdateHeading"
          v-text="$t('serviceReference4App.location.home.createOrEditLabel')"
        >
          Create or edit a Location
        </h2>
        <div>
          <div class="form-group" v-if="location.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="location.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.location.title')" for="location-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="location-title"
              data-cy="title"
              :class="{ valid: !$v.location.title.$invalid, invalid: $v.location.title.$invalid }"
              v-model="$v.location.title.$model"
              required
            />
            <div v-if="$v.location.title.$anyDirty && $v.location.title.$invalid">
              <small class="form-text text-danger" v-if="!$v.location.title.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.location.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./location-update.component.ts"></script>

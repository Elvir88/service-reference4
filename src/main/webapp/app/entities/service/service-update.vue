<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="serviceReference4App.service.home.createOrEditLabel"
          data-cy="ServiceCreateUpdateHeading"
          v-text="$t('serviceReference4App.service.home.createOrEditLabel')"
        >
          Create or edit a Service
        </h2>
        <div>
          <div class="form-group" v-if="service.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="service.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.service.serviceId')" for="service-serviceId"
              >Service Id</label
            >
            <input
              type="text"
              class="form-control"
              name="serviceId"
              id="service-serviceId"
              data-cy="serviceId"
              :class="{ valid: !$v.service.serviceId.$invalid, invalid: $v.service.serviceId.$invalid }"
              v-model="$v.service.serviceId.$model"
              required
            />
            <div v-if="$v.service.serviceId.$anyDirty && $v.service.serviceId.$invalid">
              <small class="form-text text-danger" v-if="!$v.service.serviceId.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.service.title')" for="service-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="service-title"
              data-cy="title"
              :class="{ valid: !$v.service.title.$invalid, invalid: $v.service.title.$invalid }"
              v-model="$v.service.title.$model"
              required
            />
            <div v-if="$v.service.title.$anyDirty && $v.service.title.$invalid">
              <small class="form-text text-danger" v-if="!$v.service.title.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.service.parent')" for="service-parent">Parent</label>
            <select class="form-control" id="service-parent" data-cy="parent" name="parent" v-model="service.parent">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="service.parent && serviceOption.id === service.parent.id ? service.parent : serviceOption"
                v-for="serviceOption in services"
                :key="serviceOption.id"
              >
                {{ serviceOption.title }}
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
            :disabled="$v.service.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./service-update.component.ts"></script>

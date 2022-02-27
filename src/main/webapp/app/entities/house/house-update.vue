<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="serviceReference4App.house.home.createOrEditLabel"
          data-cy="HouseCreateUpdateHeading"
          v-text="$t('serviceReference4App.house.home.createOrEditLabel')"
        >
          Create or edit a House
        </h2>
        <div>
          <div class="form-group" v-if="house.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="house.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.house.houseId')" for="house-houseId">House Id</label>
            <input
              type="number"
              class="form-control"
              name="houseId"
              id="house-houseId"
              data-cy="houseId"
              :class="{ valid: !$v.house.houseId.$invalid, invalid: $v.house.houseId.$invalid }"
              v-model.number="$v.house.houseId.$model"
              required
            />
            <div v-if="$v.house.houseId.$anyDirty && $v.house.houseId.$invalid">
              <small class="form-text text-danger" v-if="!$v.house.houseId.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.house.houseId.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.house.location')" for="house-location">Location</label>
            <select class="form-control" id="house-location" data-cy="location" name="location" v-model="house.location">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="house.location && locationOption.id === house.location.id ? house.location : locationOption"
                v-for="locationOption in locations"
                :key="locationOption.id"
              >
                {{ locationOption.title }}
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
            :disabled="$v.house.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./house-update.component.ts"></script>

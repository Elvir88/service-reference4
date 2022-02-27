<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="serviceReference4App.serviceOnLocation.home.createOrEditLabel"
          data-cy="ServiceOnLocationCreateUpdateHeading"
          v-text="$t('serviceReference4App.serviceOnLocation.home.createOrEditLabel')"
        >
          Create or edit a ServiceOnLocation
        </h2>
        <div>
          <div class="form-group" v-if="serviceOnLocation.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="serviceOnLocation.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('serviceReference4App.serviceOnLocation.datefrom')"
              for="service-on-location-datefrom"
              >Datefrom</label
            >
            <div class="d-flex">
              <input
                id="service-on-location-datefrom"
                data-cy="datefrom"
                type="datetime-local"
                class="form-control"
                name="datefrom"
                :class="{ valid: !$v.serviceOnLocation.datefrom.$invalid, invalid: $v.serviceOnLocation.datefrom.$invalid }"
                required
                :value="convertDateTimeFromServer($v.serviceOnLocation.datefrom.$model)"
                @change="updateInstantField('datefrom', $event)"
              />
            </div>
            <div v-if="$v.serviceOnLocation.datefrom.$anyDirty && $v.serviceOnLocation.datefrom.$invalid">
              <small class="form-text text-danger" v-if="!$v.serviceOnLocation.datefrom.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.serviceOnLocation.datefrom.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.serviceOnLocation.dateTo')" for="service-on-location-dateTo"
              >Date To</label
            >
            <div class="d-flex">
              <input
                id="service-on-location-dateTo"
                data-cy="dateTo"
                type="datetime-local"
                class="form-control"
                name="dateTo"
                :class="{ valid: !$v.serviceOnLocation.dateTo.$invalid, invalid: $v.serviceOnLocation.dateTo.$invalid }"
                :value="convertDateTimeFromServer($v.serviceOnLocation.dateTo.$model)"
                @change="updateInstantField('dateTo', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('serviceReference4App.serviceOnLocation.service')"
              for="service-on-location-service"
              >Service</label
            >
            <select
              class="form-control"
              id="service-on-location-service"
              data-cy="service"
              name="service"
              v-model="serviceOnLocation.service"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  serviceOnLocation.service && serviceOption.id === serviceOnLocation.service.id ? serviceOnLocation.service : serviceOption
                "
                v-for="serviceOption in services"
                :key="serviceOption.id"
              >
                {{ serviceOption.title }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('serviceReference4App.serviceOnLocation.tariffGroup')"
              for="service-on-location-tariffGroup"
              >Tariff Group</label
            >
            <select
              class="form-control"
              id="service-on-location-tariffGroup"
              data-cy="tariffGroup"
              name="tariffGroup"
              v-model="serviceOnLocation.tariffGroup"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  serviceOnLocation.tariffGroup && tariffGroupOption.id === serviceOnLocation.tariffGroup.id
                    ? serviceOnLocation.tariffGroup
                    : tariffGroupOption
                "
                v-for="tariffGroupOption in tariffGroups"
                :key="tariffGroupOption.id"
              >
                {{ tariffGroupOption.title }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('serviceReference4App.serviceOnLocation.pattern')"
              for="service-on-location-pattern"
              >Pattern</label
            >
            <select
              class="form-control"
              id="service-on-location-pattern"
              data-cy="pattern"
              name="pattern"
              v-model="serviceOnLocation.pattern"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  serviceOnLocation.pattern && contractPatternOption.id === serviceOnLocation.pattern.id
                    ? serviceOnLocation.pattern
                    : contractPatternOption
                "
                v-for="contractPatternOption in contractPatterns"
                :key="contractPatternOption.id"
              >
                {{ contractPatternOption.title }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('serviceReference4App.serviceOnLocation.location')"
              for="service-on-location-location"
              >Location</label
            >
            <select
              class="form-control"
              id="service-on-location-location"
              data-cy="location"
              name="location"
              v-model="serviceOnLocation.location"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  serviceOnLocation.location && locationOption.id === serviceOnLocation.location.id
                    ? serviceOnLocation.location
                    : locationOption
                "
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
            :disabled="$v.serviceOnLocation.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./service-on-location-update.component.ts"></script>

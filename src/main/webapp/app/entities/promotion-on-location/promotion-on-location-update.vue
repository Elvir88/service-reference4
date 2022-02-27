<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="serviceReference4App.promotionOnLocation.home.createOrEditLabel"
          data-cy="PromotionOnLocationCreateUpdateHeading"
          v-text="$t('serviceReference4App.promotionOnLocation.home.createOrEditLabel')"
        >
          Create or edit a PromotionOnLocation
        </h2>
        <div>
          <div class="form-group" v-if="promotionOnLocation.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="promotionOnLocation.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('serviceReference4App.promotionOnLocation.title')"
              for="promotion-on-location-title"
              >Title</label
            >
            <input
              type="text"
              class="form-control"
              name="title"
              id="promotion-on-location-title"
              data-cy="title"
              :class="{ valid: !$v.promotionOnLocation.title.$invalid, invalid: $v.promotionOnLocation.title.$invalid }"
              v-model="$v.promotionOnLocation.title.$model"
              required
            />
            <div v-if="$v.promotionOnLocation.title.$anyDirty && $v.promotionOnLocation.title.$invalid">
              <small class="form-text text-danger" v-if="!$v.promotionOnLocation.title.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('serviceReference4App.promotionOnLocation.dateFrom')"
              for="promotion-on-location-dateFrom"
              >Date From</label
            >
            <div class="d-flex">
              <input
                id="promotion-on-location-dateFrom"
                data-cy="dateFrom"
                type="datetime-local"
                class="form-control"
                name="dateFrom"
                :class="{ valid: !$v.promotionOnLocation.dateFrom.$invalid, invalid: $v.promotionOnLocation.dateFrom.$invalid }"
                required
                :value="convertDateTimeFromServer($v.promotionOnLocation.dateFrom.$model)"
                @change="updateInstantField('dateFrom', $event)"
              />
            </div>
            <div v-if="$v.promotionOnLocation.dateFrom.$anyDirty && $v.promotionOnLocation.dateFrom.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.promotionOnLocation.dateFrom.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.promotionOnLocation.dateFrom.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('serviceReference4App.promotionOnLocation.dateTo')"
              for="promotion-on-location-dateTo"
              >Date To</label
            >
            <div class="d-flex">
              <input
                id="promotion-on-location-dateTo"
                data-cy="dateTo"
                type="datetime-local"
                class="form-control"
                name="dateTo"
                :class="{ valid: !$v.promotionOnLocation.dateTo.$invalid, invalid: $v.promotionOnLocation.dateTo.$invalid }"
                :value="convertDateTimeFromServer($v.promotionOnLocation.dateTo.$model)"
                @change="updateInstantField('dateTo', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('serviceReference4App.promotionOnLocation.packetDiscount')"
              for="promotion-on-location-packetDiscount"
              >Packet Discount</label
            >
            <select
              class="form-control"
              id="promotion-on-location-packetDiscount"
              data-cy="packetDiscount"
              name="packetDiscount"
              v-model="promotionOnLocation.packetDiscount"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  promotionOnLocation.packetDiscount && packetDiscountOption.id === promotionOnLocation.packetDiscount.id
                    ? promotionOnLocation.packetDiscount
                    : packetDiscountOption
                "
                v-for="packetDiscountOption in packetDiscounts"
                :key="packetDiscountOption.id"
              >
                {{ packetDiscountOption.title }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('serviceReference4App.promotionOnLocation.marketingResearch')"
              for="promotion-on-location-marketingResearch"
              >Marketing Research</label
            >
            <select
              class="form-control"
              id="promotion-on-location-marketingResearch"
              data-cy="marketingResearch"
              name="marketingResearch"
              v-model="promotionOnLocation.marketingResearch"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  promotionOnLocation.marketingResearch && marketingResearchOption.id === promotionOnLocation.marketingResearch.id
                    ? promotionOnLocation.marketingResearch
                    : marketingResearchOption
                "
                v-for="marketingResearchOption in marketingResearches"
                :key="marketingResearchOption.id"
              >
                {{ marketingResearchOption.title }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('serviceReference4App.promotionOnLocation.location')"
              for="promotion-on-location-location"
              >Location</label
            >
            <select
              class="form-control"
              id="promotion-on-location-location"
              data-cy="location"
              name="location"
              v-model="promotionOnLocation.location"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  promotionOnLocation.location && locationOption.id === promotionOnLocation.location.id
                    ? promotionOnLocation.location
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
            :disabled="$v.promotionOnLocation.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./promotion-on-location-update.component.ts"></script>

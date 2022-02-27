<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="serviceReference4App.marketingResearch.home.createOrEditLabel"
          data-cy="MarketingResearchCreateUpdateHeading"
          v-text="$t('serviceReference4App.marketingResearch.home.createOrEditLabel')"
        >
          Create or edit a MarketingResearch
        </h2>
        <div>
          <div class="form-group" v-if="marketingResearch.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="marketingResearch.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.marketingResearch.title')" for="marketing-research-title"
              >Title</label
            >
            <input
              type="text"
              class="form-control"
              name="title"
              id="marketing-research-title"
              data-cy="title"
              :class="{ valid: !$v.marketingResearch.title.$invalid, invalid: $v.marketingResearch.title.$invalid }"
              v-model="$v.marketingResearch.title.$model"
              required
            />
            <div v-if="$v.marketingResearch.title.$anyDirty && $v.marketingResearch.title.$invalid">
              <small class="form-text text-danger" v-if="!$v.marketingResearch.title.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.marketingResearch.service')" for="marketing-research-service"
              >Service</label
            >
            <select
              class="form-control"
              id="marketing-research-service"
              data-cy="service"
              name="service"
              v-model="marketingResearch.service"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  marketingResearch.service && serviceOption.id === marketingResearch.service.id ? marketingResearch.service : serviceOption
                "
                v-for="serviceOption in services"
                :key="serviceOption.id"
              >
                {{ serviceOption.title }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.marketingResearch.tariff')" for="marketing-research-tariff"
              >Tariff</label
            >
            <select class="form-control" id="marketing-research-tariff" data-cy="tariff" name="tariff" v-model="marketingResearch.tariff">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  marketingResearch.tariff && tariffOption.id === marketingResearch.tariff.id ? marketingResearch.tariff : tariffOption
                "
                v-for="tariffOption in tariffs"
                :key="tariffOption.id"
              >
                {{ tariffOption.title }}
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
            :disabled="$v.marketingResearch.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./marketing-research-update.component.ts"></script>

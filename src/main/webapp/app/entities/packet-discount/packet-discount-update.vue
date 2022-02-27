<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="serviceReference4App.packetDiscount.home.createOrEditLabel"
          data-cy="PacketDiscountCreateUpdateHeading"
          v-text="$t('serviceReference4App.packetDiscount.home.createOrEditLabel')"
        >
          Create or edit a PacketDiscount
        </h2>
        <div>
          <div class="form-group" v-if="packetDiscount.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="packetDiscount.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.packetDiscount.title')" for="packet-discount-title"
              >Title</label
            >
            <input
              type="text"
              class="form-control"
              name="title"
              id="packet-discount-title"
              data-cy="title"
              :class="{ valid: !$v.packetDiscount.title.$invalid, invalid: $v.packetDiscount.title.$invalid }"
              v-model="$v.packetDiscount.title.$model"
              required
            />
            <div v-if="$v.packetDiscount.title.$anyDirty && $v.packetDiscount.title.$invalid">
              <small class="form-text text-danger" v-if="!$v.packetDiscount.title.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('serviceReference4App.packetDiscount.cost')" for="packet-discount-cost"
              >Cost</label
            >
            <input
              type="number"
              class="form-control"
              name="cost"
              id="packet-discount-cost"
              data-cy="cost"
              :class="{ valid: !$v.packetDiscount.cost.$invalid, invalid: $v.packetDiscount.cost.$invalid }"
              v-model.number="$v.packetDiscount.cost.$model"
              required
            />
            <div v-if="$v.packetDiscount.cost.$anyDirty && $v.packetDiscount.cost.$invalid">
              <small class="form-text text-danger" v-if="!$v.packetDiscount.cost.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.packetDiscount.cost.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
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
            :disabled="$v.packetDiscount.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./packet-discount-update.component.ts"></script>

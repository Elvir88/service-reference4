<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="serviceReference4App.serviceInPacketDiscount.home.createOrEditLabel"
          data-cy="ServiceInPacketDiscountCreateUpdateHeading"
          v-text="$t('serviceReference4App.serviceInPacketDiscount.home.createOrEditLabel')"
        >
          Create or edit a ServiceInPacketDiscount
        </h2>
        <div>
          <div class="form-group" v-if="serviceInPacketDiscount.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="serviceInPacketDiscount.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('serviceReference4App.serviceInPacketDiscount.coefficient')"
              for="service-in-packet-discount-coefficient"
              >Coefficient</label
            >
            <input
              type="number"
              class="form-control"
              name="coefficient"
              id="service-in-packet-discount-coefficient"
              data-cy="coefficient"
              :class="{ valid: !$v.serviceInPacketDiscount.coefficient.$invalid, invalid: $v.serviceInPacketDiscount.coefficient.$invalid }"
              v-model.number="$v.serviceInPacketDiscount.coefficient.$model"
              required
            />
            <div v-if="$v.serviceInPacketDiscount.coefficient.$anyDirty && $v.serviceInPacketDiscount.coefficient.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.serviceInPacketDiscount.coefficient.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.serviceInPacketDiscount.coefficient.min"
                v-text="$t('entity.validation.min', { min: 0 })"
              >
                This field should be at least 0.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.serviceInPacketDiscount.coefficient.max"
                v-text="$t('entity.validation.max', { max: 1 })"
              >
                This field cannot be longer than 1 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.serviceInPacketDiscount.coefficient.numeric"
                v-text="$t('entity.validation.number')"
              >
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('serviceReference4App.serviceInPacketDiscount.service')"
              for="service-in-packet-discount-service"
              >Service</label
            >
            <select
              class="form-control"
              id="service-in-packet-discount-service"
              data-cy="service"
              name="service"
              v-model="serviceInPacketDiscount.service"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  serviceInPacketDiscount.service && serviceOption.id === serviceInPacketDiscount.service.id
                    ? serviceInPacketDiscount.service
                    : serviceOption
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
              v-text="$t('serviceReference4App.serviceInPacketDiscount.tariff')"
              for="service-in-packet-discount-tariff"
              >Tariff</label
            >
            <select
              class="form-control"
              id="service-in-packet-discount-tariff"
              data-cy="tariff"
              name="tariff"
              v-model="serviceInPacketDiscount.tariff"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  serviceInPacketDiscount.tariff && tariffOption.id === serviceInPacketDiscount.tariff.id
                    ? serviceInPacketDiscount.tariff
                    : tariffOption
                "
                v-for="tariffOption in tariffs"
                :key="tariffOption.id"
              >
                {{ tariffOption.title }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('serviceReference4App.serviceInPacketDiscount.packetDiscount')"
              for="service-in-packet-discount-packetDiscount"
              >Packet Discount</label
            >
            <select
              class="form-control"
              id="service-in-packet-discount-packetDiscount"
              data-cy="packetDiscount"
              name="packetDiscount"
              v-model="serviceInPacketDiscount.packetDiscount"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  serviceInPacketDiscount.packetDiscount && packetDiscountOption.id === serviceInPacketDiscount.packetDiscount.id
                    ? serviceInPacketDiscount.packetDiscount
                    : packetDiscountOption
                "
                v-for="packetDiscountOption in packetDiscounts"
                :key="packetDiscountOption.id"
              >
                {{ packetDiscountOption.title }}
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
            :disabled="$v.serviceInPacketDiscount.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./service-in-packet-discount-update.component.ts"></script>

<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="serviceInPacketDiscount">
        <h2 class="jh-entity-heading" data-cy="serviceInPacketDiscountDetailsHeading">
          <span v-text="$t('serviceReference4App.serviceInPacketDiscount.detail.title')">ServiceInPacketDiscount</span>
          {{ serviceInPacketDiscount.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('serviceReference4App.serviceInPacketDiscount.coefficient')">Coefficient</span>
          </dt>
          <dd>
            <span>{{ serviceInPacketDiscount.coefficient }}</span>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.serviceInPacketDiscount.service')">Service</span>
          </dt>
          <dd>
            <div v-if="serviceInPacketDiscount.service">
              <router-link :to="{ name: 'ServiceView', params: { serviceId: serviceInPacketDiscount.service.id } }">{{
                serviceInPacketDiscount.service.title
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.serviceInPacketDiscount.tariff')">Tariff</span>
          </dt>
          <dd>
            <div v-if="serviceInPacketDiscount.tariff">
              <router-link :to="{ name: 'TariffView', params: { tariffId: serviceInPacketDiscount.tariff.id } }">{{
                serviceInPacketDiscount.tariff.title
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.serviceInPacketDiscount.packetDiscount')">Packet Discount</span>
          </dt>
          <dd>
            <div v-if="serviceInPacketDiscount.packetDiscount">
              <router-link :to="{ name: 'PacketDiscountView', params: { packetDiscountId: serviceInPacketDiscount.packetDiscount.id } }">{{
                serviceInPacketDiscount.packetDiscount.title
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="serviceInPacketDiscount.id"
          :to="{ name: 'ServiceInPacketDiscountEdit', params: { serviceInPacketDiscountId: serviceInPacketDiscount.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./service-in-packet-discount-details.component.ts"></script>

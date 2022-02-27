<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="packetDiscount">
        <h2 class="jh-entity-heading" data-cy="packetDiscountDetailsHeading">
          <span v-text="$t('serviceReference4App.packetDiscount.detail.title')">PacketDiscount</span> {{ packetDiscount.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('serviceReference4App.packetDiscount.title')">Title</span>
          </dt>
          <dd>
            <span>{{ packetDiscount.title }}</span>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.packetDiscount.cost')">Cost</span>
          </dt>
          <dd>
            <span>{{ packetDiscount.cost }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="packetDiscount.id"
          :to="{ name: 'PacketDiscountEdit', params: { packetDiscountId: packetDiscount.id } }"
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

<script lang="ts" src="./packet-discount-details.component.ts"></script>

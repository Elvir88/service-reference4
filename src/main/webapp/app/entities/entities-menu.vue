<template>
  <div>
    <b-dropdown-item to="/house">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.house')">House</span>
    </b-dropdown-item>
    <b-dropdown-item to="/location">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.location')">Location</span>
    </b-dropdown-item>
    <b-dropdown-item to="/service">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.service')">Service</span>
    </b-dropdown-item>
    <b-dropdown-item to="/service-on-location">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.serviceOnLocation')">Service On Location</span>
    </b-dropdown-item>
    <b-dropdown-item to="/tariff">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.tariff')">Tariff</span>
    </b-dropdown-item>
    <b-dropdown-item to="/tariff-group">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.tariffGroup')">Tariff Group</span>
    </b-dropdown-item>
    <b-dropdown-item to="/promotion-on-location">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.promotionOnLocation')">Promotion On Location</span>
    </b-dropdown-item>
    <b-dropdown-item to="/packet-discount">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.packetDiscount')">Packet Discount</span>
    </b-dropdown-item>
    <b-dropdown-item to="/marketing-research">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.marketingResearch')">Marketing Research</span>
    </b-dropdown-item>
    <b-dropdown-item to="/service-in-packet-discount">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.serviceInPacketDiscount')">Service In Packet Discount</span>
    </b-dropdown-item>
    <b-dropdown-item to="/contract-pattern">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.contractPattern')">Contract Pattern</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>

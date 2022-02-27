<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="serviceOnLocation">
        <h2 class="jh-entity-heading" data-cy="serviceOnLocationDetailsHeading">
          <span v-text="$t('serviceReference4App.serviceOnLocation.detail.title')">ServiceOnLocation</span> {{ serviceOnLocation.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('serviceReference4App.serviceOnLocation.datefrom')">Datefrom</span>
          </dt>
          <dd>
            <span v-if="serviceOnLocation.datefrom">{{ $d(Date.parse(serviceOnLocation.datefrom), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.serviceOnLocation.dateTo')">Date To</span>
          </dt>
          <dd>
            <span v-if="serviceOnLocation.dateTo">{{ $d(Date.parse(serviceOnLocation.dateTo), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.serviceOnLocation.service')">Service</span>
          </dt>
          <dd>
            <div v-if="serviceOnLocation.service">
              <router-link :to="{ name: 'ServiceView', params: { serviceId: serviceOnLocation.service.id } }">{{
                serviceOnLocation.service.title
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.serviceOnLocation.tariffGroup')">Tariff Group</span>
          </dt>
          <dd>
            <div v-if="serviceOnLocation.tariffGroup">
              <router-link :to="{ name: 'TariffGroupView', params: { tariffGroupId: serviceOnLocation.tariffGroup.id } }">{{
                serviceOnLocation.tariffGroup.title
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.serviceOnLocation.pattern')">Pattern</span>
          </dt>
          <dd>
            <div v-if="serviceOnLocation.pattern">
              <router-link :to="{ name: 'ContractPatternView', params: { contractPatternId: serviceOnLocation.pattern.id } }">{{
                serviceOnLocation.pattern.title
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.serviceOnLocation.location')">Location</span>
          </dt>
          <dd>
            <div v-if="serviceOnLocation.location">
              <router-link :to="{ name: 'LocationView', params: { locationId: serviceOnLocation.location.id } }">{{
                serviceOnLocation.location.title
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="serviceOnLocation.id"
          :to="{ name: 'ServiceOnLocationEdit', params: { serviceOnLocationId: serviceOnLocation.id } }"
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

<script lang="ts" src="./service-on-location-details.component.ts"></script>

<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="house">
        <h2 class="jh-entity-heading" data-cy="houseDetailsHeading">
          <span v-text="$t('serviceReference4App.house.detail.title')">House</span> {{ house.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('serviceReference4App.house.houseId')">House Id</span>
          </dt>
          <dd>
            <span>{{ house.houseId }}</span>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.house.location')">Location</span>
          </dt>
          <dd>
            <div v-if="house.location">
              <router-link :to="{ name: 'LocationView', params: { locationId: house.location.id } }">{{
                house.location.title
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="house.id" :to="{ name: 'HouseEdit', params: { houseId: house.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./house-details.component.ts"></script>

<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="marketingResearch">
        <h2 class="jh-entity-heading" data-cy="marketingResearchDetailsHeading">
          <span v-text="$t('serviceReference4App.marketingResearch.detail.title')">MarketingResearch</span> {{ marketingResearch.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('serviceReference4App.marketingResearch.title')">Title</span>
          </dt>
          <dd>
            <span>{{ marketingResearch.title }}</span>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.marketingResearch.service')">Service</span>
          </dt>
          <dd>
            <div v-if="marketingResearch.service">
              <router-link :to="{ name: 'ServiceView', params: { serviceId: marketingResearch.service.id } }">{{
                marketingResearch.service.title
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.marketingResearch.tariff')">Tariff</span>
          </dt>
          <dd>
            <div v-if="marketingResearch.tariff">
              <router-link :to="{ name: 'TariffView', params: { tariffId: marketingResearch.tariff.id } }">{{
                marketingResearch.tariff.title
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="marketingResearch.id"
          :to="{ name: 'MarketingResearchEdit', params: { marketingResearchId: marketingResearch.id } }"
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

<script lang="ts" src="./marketing-research-details.component.ts"></script>

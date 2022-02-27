<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="promotionOnLocation">
        <h2 class="jh-entity-heading" data-cy="promotionOnLocationDetailsHeading">
          <span v-text="$t('serviceReference4App.promotionOnLocation.detail.title')">PromotionOnLocation</span> {{ promotionOnLocation.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('serviceReference4App.promotionOnLocation.title')">Title</span>
          </dt>
          <dd>
            <span>{{ promotionOnLocation.title }}</span>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.promotionOnLocation.dateFrom')">Date From</span>
          </dt>
          <dd>
            <span v-if="promotionOnLocation.dateFrom">{{ $d(Date.parse(promotionOnLocation.dateFrom), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.promotionOnLocation.dateTo')">Date To</span>
          </dt>
          <dd>
            <span v-if="promotionOnLocation.dateTo">{{ $d(Date.parse(promotionOnLocation.dateTo), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.promotionOnLocation.packetDiscount')">Packet Discount</span>
          </dt>
          <dd>
            <div v-if="promotionOnLocation.packetDiscount">
              <router-link :to="{ name: 'PacketDiscountView', params: { packetDiscountId: promotionOnLocation.packetDiscount.id } }">{{
                promotionOnLocation.packetDiscount.title
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.promotionOnLocation.marketingResearch')">Marketing Research</span>
          </dt>
          <dd>
            <div v-if="promotionOnLocation.marketingResearch">
              <router-link
                :to="{ name: 'MarketingResearchView', params: { marketingResearchId: promotionOnLocation.marketingResearch.id } }"
                >{{ promotionOnLocation.marketingResearch.title }}</router-link
              >
            </div>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.promotionOnLocation.location')">Location</span>
          </dt>
          <dd>
            <div v-if="promotionOnLocation.location">
              <router-link :to="{ name: 'LocationView', params: { locationId: promotionOnLocation.location.id } }">{{
                promotionOnLocation.location.title
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="promotionOnLocation.id"
          :to="{ name: 'PromotionOnLocationEdit', params: { promotionOnLocationId: promotionOnLocation.id } }"
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

<script lang="ts" src="./promotion-on-location-details.component.ts"></script>

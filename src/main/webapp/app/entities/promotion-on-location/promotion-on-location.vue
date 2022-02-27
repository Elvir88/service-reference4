<template>
  <div>
    <h2 id="page-heading" data-cy="PromotionOnLocationHeading">
      <span v-text="$t('serviceReference4App.promotionOnLocation.home.title')" id="promotion-on-location-heading"
        >Promotion On Locations</span
      >
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('serviceReference4App.promotionOnLocation.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'PromotionOnLocationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-promotion-on-location"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('serviceReference4App.promotionOnLocation.home.createLabel')"> Create a new Promotion On Location </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && promotionOnLocations && promotionOnLocations.length === 0">
      <span v-text="$t('serviceReference4App.promotionOnLocation.home.notFound')">No promotionOnLocations found</span>
    </div>
    <div class="table-responsive" v-if="promotionOnLocations && promotionOnLocations.length > 0">
      <table class="table table-striped" aria-describedby="promotionOnLocations">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('title')">
              <span v-text="$t('serviceReference4App.promotionOnLocation.title')">Title</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('dateFrom')">
              <span v-text="$t('serviceReference4App.promotionOnLocation.dateFrom')">Date From</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dateFrom'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('dateTo')">
              <span v-text="$t('serviceReference4App.promotionOnLocation.dateTo')">Date To</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dateTo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('packetDiscount.title')">
              <span v-text="$t('serviceReference4App.promotionOnLocation.packetDiscount')">Packet Discount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'packetDiscount.title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('marketingResearch.title')">
              <span v-text="$t('serviceReference4App.promotionOnLocation.marketingResearch')">Marketing Research</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'marketingResearch.title'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('location.title')">
              <span v-text="$t('serviceReference4App.promotionOnLocation.location')">Location</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'location.title'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="promotionOnLocation in promotionOnLocations" :key="promotionOnLocation.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PromotionOnLocationView', params: { promotionOnLocationId: promotionOnLocation.id } }">{{
                promotionOnLocation.id
              }}</router-link>
            </td>
            <td>{{ promotionOnLocation.title }}</td>
            <td>{{ promotionOnLocation.dateFrom ? $d(Date.parse(promotionOnLocation.dateFrom), 'short') : '' }}</td>
            <td>{{ promotionOnLocation.dateTo ? $d(Date.parse(promotionOnLocation.dateTo), 'short') : '' }}</td>
            <td>
              <div v-if="promotionOnLocation.packetDiscount">
                <router-link :to="{ name: 'PacketDiscountView', params: { packetDiscountId: promotionOnLocation.packetDiscount.id } }">{{
                  promotionOnLocation.packetDiscount.title
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="promotionOnLocation.marketingResearch">
                <router-link
                  :to="{ name: 'MarketingResearchView', params: { marketingResearchId: promotionOnLocation.marketingResearch.id } }"
                  >{{ promotionOnLocation.marketingResearch.title }}</router-link
                >
              </div>
            </td>
            <td>
              <div v-if="promotionOnLocation.location">
                <router-link :to="{ name: 'LocationView', params: { locationId: promotionOnLocation.location.id } }">{{
                  promotionOnLocation.location.title
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PromotionOnLocationView', params: { promotionOnLocationId: promotionOnLocation.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PromotionOnLocationEdit', params: { promotionOnLocationId: promotionOnLocation.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(promotionOnLocation)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span
          id="serviceReference4App.promotionOnLocation.delete.question"
          data-cy="promotionOnLocationDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p
          id="jhi-delete-promotionOnLocation-heading"
          v-text="$t('serviceReference4App.promotionOnLocation.delete.question', { id: removeId })"
        >
          Are you sure you want to delete this Promotion On Location?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-promotionOnLocation"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removePromotionOnLocation()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="promotionOnLocations && promotionOnLocations.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./promotion-on-location.component.ts"></script>

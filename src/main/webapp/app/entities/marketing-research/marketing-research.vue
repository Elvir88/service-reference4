<template>
  <div>
    <h2 id="page-heading" data-cy="MarketingResearchHeading">
      <span v-text="$t('serviceReference4App.marketingResearch.home.title')" id="marketing-research-heading">Marketing Researches</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('serviceReference4App.marketingResearch.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'MarketingResearchCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-marketing-research"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('serviceReference4App.marketingResearch.home.createLabel')"> Create a new Marketing Research </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && marketingResearches && marketingResearches.length === 0">
      <span v-text="$t('serviceReference4App.marketingResearch.home.notFound')">No marketingResearches found</span>
    </div>
    <div class="table-responsive" v-if="marketingResearches && marketingResearches.length > 0">
      <table class="table table-striped" aria-describedby="marketingResearches">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('title')">
              <span v-text="$t('serviceReference4App.marketingResearch.title')">Title</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('service.title')">
              <span v-text="$t('serviceReference4App.marketingResearch.service')">Service</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'service.title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('tariff.title')">
              <span v-text="$t('serviceReference4App.marketingResearch.tariff')">Tariff</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tariff.title'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="marketingResearch in marketingResearches" :key="marketingResearch.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MarketingResearchView', params: { marketingResearchId: marketingResearch.id } }">{{
                marketingResearch.id
              }}</router-link>
            </td>
            <td>{{ marketingResearch.title }}</td>
            <td>
              <div v-if="marketingResearch.service">
                <router-link :to="{ name: 'ServiceView', params: { serviceId: marketingResearch.service.id } }">{{
                  marketingResearch.service.title
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="marketingResearch.tariff">
                <router-link :to="{ name: 'TariffView', params: { tariffId: marketingResearch.tariff.id } }">{{
                  marketingResearch.tariff.title
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'MarketingResearchView', params: { marketingResearchId: marketingResearch.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'MarketingResearchEdit', params: { marketingResearchId: marketingResearch.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(marketingResearch)"
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
          id="serviceReference4App.marketingResearch.delete.question"
          data-cy="marketingResearchDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p
          id="jhi-delete-marketingResearch-heading"
          v-text="$t('serviceReference4App.marketingResearch.delete.question', { id: removeId })"
        >
          Are you sure you want to delete this Marketing Research?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-marketingResearch"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeMarketingResearch()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="marketingResearches && marketingResearches.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./marketing-research.component.ts"></script>

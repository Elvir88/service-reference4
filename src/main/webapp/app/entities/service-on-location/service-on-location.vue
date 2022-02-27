<template>
  <div>
    <h2 id="page-heading" data-cy="ServiceOnLocationHeading">
      <span v-text="$t('serviceReference4App.serviceOnLocation.home.title')" id="service-on-location-heading">Service On Locations</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('serviceReference4App.serviceOnLocation.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ServiceOnLocationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-service-on-location"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('serviceReference4App.serviceOnLocation.home.createLabel')"> Create a new Service On Location </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && serviceOnLocations && serviceOnLocations.length === 0">
      <span v-text="$t('serviceReference4App.serviceOnLocation.home.notFound')">No serviceOnLocations found</span>
    </div>
    <div class="table-responsive" v-if="serviceOnLocations && serviceOnLocations.length > 0">
      <table class="table table-striped" aria-describedby="serviceOnLocations">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('datefrom')">
              <span v-text="$t('serviceReference4App.serviceOnLocation.datefrom')">Datefrom</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'datefrom'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('dateTo')">
              <span v-text="$t('serviceReference4App.serviceOnLocation.dateTo')">Date To</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dateTo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('service.title')">
              <span v-text="$t('serviceReference4App.serviceOnLocation.service')">Service</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'service.title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('tariffGroup.title')">
              <span v-text="$t('serviceReference4App.serviceOnLocation.tariffGroup')">Tariff Group</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tariffGroup.title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('pattern.title')">
              <span v-text="$t('serviceReference4App.serviceOnLocation.pattern')">Pattern</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'pattern.title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('location.title')">
              <span v-text="$t('serviceReference4App.serviceOnLocation.location')">Location</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'location.title'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="serviceOnLocation in serviceOnLocations" :key="serviceOnLocation.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ServiceOnLocationView', params: { serviceOnLocationId: serviceOnLocation.id } }">{{
                serviceOnLocation.id
              }}</router-link>
            </td>
            <td>{{ serviceOnLocation.datefrom ? $d(Date.parse(serviceOnLocation.datefrom), 'short') : '' }}</td>
            <td>{{ serviceOnLocation.dateTo ? $d(Date.parse(serviceOnLocation.dateTo), 'short') : '' }}</td>
            <td>
              <div v-if="serviceOnLocation.service">
                <router-link :to="{ name: 'ServiceView', params: { serviceId: serviceOnLocation.service.id } }">{{
                  serviceOnLocation.service.title
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="serviceOnLocation.tariffGroup">
                <router-link :to="{ name: 'TariffGroupView', params: { tariffGroupId: serviceOnLocation.tariffGroup.id } }">{{
                  serviceOnLocation.tariffGroup.title
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="serviceOnLocation.pattern">
                <router-link :to="{ name: 'ContractPatternView', params: { contractPatternId: serviceOnLocation.pattern.id } }">{{
                  serviceOnLocation.pattern.title
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="serviceOnLocation.location">
                <router-link :to="{ name: 'LocationView', params: { locationId: serviceOnLocation.location.id } }">{{
                  serviceOnLocation.location.title
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ServiceOnLocationView', params: { serviceOnLocationId: serviceOnLocation.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ServiceOnLocationEdit', params: { serviceOnLocationId: serviceOnLocation.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(serviceOnLocation)"
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
          id="serviceReference4App.serviceOnLocation.delete.question"
          data-cy="serviceOnLocationDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p
          id="jhi-delete-serviceOnLocation-heading"
          v-text="$t('serviceReference4App.serviceOnLocation.delete.question', { id: removeId })"
        >
          Are you sure you want to delete this Service On Location?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-serviceOnLocation"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeServiceOnLocation()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="serviceOnLocations && serviceOnLocations.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./service-on-location.component.ts"></script>

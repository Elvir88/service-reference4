<template>
  <div>
    <h2 id="page-heading" data-cy="HouseHeading">
      <span v-text="$t('serviceReference4App.house.home.title')" id="house-heading">Houses</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('serviceReference4App.house.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'HouseCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-house"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('serviceReference4App.house.home.createLabel')"> Create a new House </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && houses && houses.length === 0">
      <span v-text="$t('serviceReference4App.house.home.notFound')">No houses found</span>
    </div>
    <div class="table-responsive" v-if="houses && houses.length > 0">
      <table class="table table-striped" aria-describedby="houses">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('houseId')">
              <span v-text="$t('serviceReference4App.house.houseId')">House Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'houseId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('location.title')">
              <span v-text="$t('serviceReference4App.house.location')">Location</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'location.title'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="house in houses" :key="house.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'HouseView', params: { houseId: house.id } }">{{ house.id }}</router-link>
            </td>
            <td>{{ house.houseId }}</td>
            <td>
              <div v-if="house.location">
                <router-link :to="{ name: 'LocationView', params: { locationId: house.location.id } }">{{
                  house.location.title
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'HouseView', params: { houseId: house.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'HouseEdit', params: { houseId: house.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(house)"
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
        ><span id="serviceReference4App.house.delete.question" data-cy="houseDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-house-heading" v-text="$t('serviceReference4App.house.delete.question', { id: removeId })">
          Are you sure you want to delete this House?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-house"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeHouse()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="houses && houses.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./house.component.ts"></script>

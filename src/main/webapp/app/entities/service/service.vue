<template>
  <div>
    <h2 id="page-heading" data-cy="ServiceHeading">
      <span v-text="$t('serviceReference4App.service.home.title')" id="service-heading">Services</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('serviceReference4App.service.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ServiceCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-service"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('serviceReference4App.service.home.createLabel')"> Create a new Service </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && services && services.length === 0">
      <span v-text="$t('serviceReference4App.service.home.notFound')">No services found</span>
    </div>
    <div class="table-responsive" v-if="services && services.length > 0">
      <table class="table table-striped" aria-describedby="services">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('serviceId')">
              <span v-text="$t('serviceReference4App.service.serviceId')">Service Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'serviceId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('title')">
              <span v-text="$t('serviceReference4App.service.title')">Title</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('parent.title')">
              <span v-text="$t('serviceReference4App.service.parent')">Parent</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'parent.title'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="service in services" :key="service.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ServiceView', params: { serviceId: service.id } }">{{ service.id }}</router-link>
            </td>
            <td>{{ service.serviceId }}</td>
            <td>{{ service.title }}</td>
            <td>
              <div v-if="service.parent">
                <router-link :to="{ name: 'ServiceView', params: { serviceId: service.parent.id } }">{{
                  service.parent.title
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ServiceView', params: { serviceId: service.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ServiceEdit', params: { serviceId: service.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(service)"
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
        ><span id="serviceReference4App.service.delete.question" data-cy="serviceDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-service-heading" v-text="$t('serviceReference4App.service.delete.question', { id: removeId })">
          Are you sure you want to delete this Service?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-service"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeService()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="services && services.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./service.component.ts"></script>

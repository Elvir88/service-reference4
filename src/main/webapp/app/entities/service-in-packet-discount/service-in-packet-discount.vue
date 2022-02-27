<template>
  <div>
    <h2 id="page-heading" data-cy="ServiceInPacketDiscountHeading">
      <span v-text="$t('serviceReference4App.serviceInPacketDiscount.home.title')" id="service-in-packet-discount-heading"
        >Service In Packet Discounts</span
      >
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('serviceReference4App.serviceInPacketDiscount.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ServiceInPacketDiscountCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-service-in-packet-discount"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('serviceReference4App.serviceInPacketDiscount.home.createLabel')">
              Create a new Service In Packet Discount
            </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && serviceInPacketDiscounts && serviceInPacketDiscounts.length === 0">
      <span v-text="$t('serviceReference4App.serviceInPacketDiscount.home.notFound')">No serviceInPacketDiscounts found</span>
    </div>
    <div class="table-responsive" v-if="serviceInPacketDiscounts && serviceInPacketDiscounts.length > 0">
      <table class="table table-striped" aria-describedby="serviceInPacketDiscounts">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('serviceReference4App.serviceInPacketDiscount.coefficient')">Coefficient</span></th>
            <th scope="row"><span v-text="$t('serviceReference4App.serviceInPacketDiscount.service')">Service</span></th>
            <th scope="row"><span v-text="$t('serviceReference4App.serviceInPacketDiscount.tariff')">Tariff</span></th>
            <th scope="row"><span v-text="$t('serviceReference4App.serviceInPacketDiscount.packetDiscount')">Packet Discount</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="serviceInPacketDiscount in serviceInPacketDiscounts" :key="serviceInPacketDiscount.id" data-cy="entityTable">
            <td>
              <router-link
                :to="{ name: 'ServiceInPacketDiscountView', params: { serviceInPacketDiscountId: serviceInPacketDiscount.id } }"
                >{{ serviceInPacketDiscount.id }}</router-link
              >
            </td>
            <td>{{ serviceInPacketDiscount.coefficient }}</td>
            <td>
              <div v-if="serviceInPacketDiscount.service">
                <router-link :to="{ name: 'ServiceView', params: { serviceId: serviceInPacketDiscount.service.id } }">{{
                  serviceInPacketDiscount.service.title
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="serviceInPacketDiscount.tariff">
                <router-link :to="{ name: 'TariffView', params: { tariffId: serviceInPacketDiscount.tariff.id } }">{{
                  serviceInPacketDiscount.tariff.title
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="serviceInPacketDiscount.packetDiscount">
                <router-link
                  :to="{ name: 'PacketDiscountView', params: { packetDiscountId: serviceInPacketDiscount.packetDiscount.id } }"
                  >{{ serviceInPacketDiscount.packetDiscount.title }}</router-link
                >
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ServiceInPacketDiscountView', params: { serviceInPacketDiscountId: serviceInPacketDiscount.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ServiceInPacketDiscountEdit', params: { serviceInPacketDiscountId: serviceInPacketDiscount.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(serviceInPacketDiscount)"
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
          id="serviceReference4App.serviceInPacketDiscount.delete.question"
          data-cy="serviceInPacketDiscountDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p
          id="jhi-delete-serviceInPacketDiscount-heading"
          v-text="$t('serviceReference4App.serviceInPacketDiscount.delete.question', { id: removeId })"
        >
          Are you sure you want to delete this Service In Packet Discount?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-serviceInPacketDiscount"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeServiceInPacketDiscount()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./service-in-packet-discount.component.ts"></script>

<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="contractPattern">
        <h2 class="jh-entity-heading" data-cy="contractPatternDetailsHeading">
          <span v-text="$t('serviceReference4App.contractPattern.detail.title')">ContractPattern</span> {{ contractPattern.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('serviceReference4App.contractPattern.title')">Title</span>
          </dt>
          <dd>
            <span>{{ contractPattern.title }}</span>
          </dd>
          <dt>
            <span v-text="$t('serviceReference4App.contractPattern.patternId')">Pattern Id</span>
          </dt>
          <dd>
            <span>{{ contractPattern.patternId }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="contractPattern.id"
          :to="{ name: 'ContractPatternEdit', params: { contractPatternId: contractPattern.id } }"
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

<script lang="ts" src="./contract-pattern-details.component.ts"></script>

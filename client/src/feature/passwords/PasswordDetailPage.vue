<template>
  <h2>Password Detail</h2>
  <p>{{ route.params.id }}</p>
</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router'

const route = useRoute()
</script>

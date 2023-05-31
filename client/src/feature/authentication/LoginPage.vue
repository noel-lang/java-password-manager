<template>
  <button @click="login">Login</button>
  {{ store.token }}
</template>

<script lang="ts" setup>
import { useAuthStore } from '@/stores/useAuthStore'

const store = useAuthStore()

const login = async () => {
  const response = await store.login('noel', '12345678')

  console.log(response)
}
</script>

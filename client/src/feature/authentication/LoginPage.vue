<template>
  <button @click="login">Login</button>
  {{ store.token }}
</template>

<script lang="ts" setup>
import { useAuthStore } from '@/stores/useAuthStore'
import { useRouter } from 'vue-router'

const store = useAuthStore()
const router = useRouter()

const login = async () => {
  const response = await store.login('noel', '12345678')

  console.log('redirect user after successful login')
  await router.push('/')
}
</script>

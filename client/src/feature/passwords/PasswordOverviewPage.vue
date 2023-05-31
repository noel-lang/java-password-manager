<template>
  <div class="mx-auto max-w-7xl sm:px-6 lg:px-8 grid grid-cols-3 gap-6 my-12">
    <div class="col-span-1">
      <password-list @click="navigateToPassword" />
    </div>
    <div class="col-span-2">
      <router-view />
    </div>
  </div>
</template>
<script setup>
import PasswordList from '@/feature/passwords/components/PasswordList.vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const navigateToPassword = ({ id }) => {
  router.push(`/${id}`)
}
</script>

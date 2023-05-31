<template>
  <div
    class="mx-auto max-w-7xl sm:px-6 lg:px-8 grid grid-cols-3 gap-6 my-12 overflow-y-scroll"
    style="max-height: 600px"
  >
    <div class="col-span-1">
      <password-list :passwords="passwords" @click="navigateToPassword" />
    </div>
    <div class="col-span-2">
      <router-view />
    </div>
  </div>
</template>
<script lang="ts" setup>
import PasswordList from '@/feature/passwords/components/PasswordList.vue'
import { useRouter } from 'vue-router'
import { PasswordControllerApi, PasswordDto } from '@api/api'
import type { Ref } from 'vue'
import { onMounted, ref } from 'vue'
import { Configuration } from '@api/configuration'
import { useAuthStore } from '@/stores/useAuthStore'
import { storeToRefs } from 'pinia'

const router = useRouter()
const authStore = useAuthStore()

const { token } = storeToRefs(authStore)

const configuration = new Configuration({
  baseOptions: {
    headers: {
      Authorization: `Bearer ${token.value}`
    }
  }
})

const passwordApi = new PasswordControllerApi(configuration)

const passwords: Ref<PasswordDto[]> = ref([])

onMounted(() => {
  passwordApi.index().then((response) => {
    passwords.value = response.data
  })
})

const navigateToPassword = ({ uuid }) => {
  router.push(`/${uuid}`)
}
</script>

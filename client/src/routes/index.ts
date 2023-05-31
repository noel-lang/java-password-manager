import PasswordOverviewPage from '@/feature/passwords/PasswordOverviewPage.vue'
import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/feature/authentication/LoginPage.vue'
import { useAuthStore } from '@/stores/useAuthStore'
import { storeToRefs } from 'pinia'
import PasswordDetailPage from '@/feature/passwords/PasswordDetailPage.vue'

const routes = [
  {
    path: '/',
    component: PasswordOverviewPage,
    children: [{ path: ':id', component: PasswordDetailPage }]
  },
  {
    path: '/login',
    component: LoginPage
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  const { authenticated } = storeToRefs(authStore)

  console.log('is authenticated:', authenticated.value)

  if (!authenticated.value && to.path !== '/login') {
    console.log('user not authenticated, redirecting to login')
    return next('/login')
  }

  console.log('user is authenticated or is on the login page')
  next()
})

export default router

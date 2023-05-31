import PasswordOverviewPage from '@/feature/passwords/PasswordOverviewPage.vue'
import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/feature/authentication/LoginPage.vue'

const routes = [
  {
    path: '/',
    component: PasswordOverviewPage
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

export default router

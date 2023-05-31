import { defineStore } from 'pinia'
import { AuthenticationControllerApi, TokenControllerApi } from '@api/api'

const encoder = new TextEncoder()

const hashPassword = async (masterPasswordKey: CryptoKey, salt: string) => {
  return await window.crypto.subtle.deriveBits(
    {
      name: 'PBKDF2',
      salt: encoder.encode(salt),
      iterations: 1000,
      hash: 'SHA-256'
    },
    masterPasswordKey,
    256
  )
}

const generateMasterPasswordKey = async (password: string) => {
  return await window.crypto.subtle.importKey(
    'raw',
    encoder.encode(password),
    { name: 'PBKDF2' },
    false,
    ['deriveBits', 'deriveKey']
  )
}

const arrayBufferToHex = (arrayBuffer: ArrayBuffer) => {
  return Array.from(new Uint8Array(arrayBuffer))
    .map((b) => b.toString(16).padStart(2, '0'))
    .join('')
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    authenticated: false,
    token: ''
  }),
  actions: {
    async login(username: string, cleartextPassword: string) {
      const authenticationService = new AuthenticationControllerApi()

      // 1. Retrieve Salt
      const saltResponse = await authenticationService.retrieveSalt(username)
      const salt = saltResponse.data.salt

      // 2. Hash Password
      const masterPasswordKey = await generateMasterPasswordKey(cleartextPassword)
      const hashedPassword = await hashPassword(masterPasswordKey, salt!)
      const hashedPasswordHex = arrayBufferToHex(hashedPassword)

      const tokenService = new TokenControllerApi()

      const response = await tokenService.token({ username, hashedPassword: hashedPasswordHex })

      this.authenticated = true
      this.token = response.data
    }
  }
})

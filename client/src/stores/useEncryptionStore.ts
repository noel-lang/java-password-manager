import { defineStore } from 'pinia'

interface State {
  masterKey: CryptoKey | null
}

const encoder = new TextEncoder()

const arrayBufferToHex = (arrayBuffer: ArrayBuffer) => {
  return Array.from(new Uint8Array(arrayBuffer))
    .map((b) => b.toString(16).padStart(2, '0'))
    .join('')
}

export const useEncryptionStore = defineStore('encryption', {
  state: (): State => ({
    masterKey: null
  }),
  actions: {
    async initialize(masterPassword: string) {
      this.masterKey = await window.crypto.subtle.importKey(
        'raw',
        encoder.encode(masterPassword),
        { name: 'PBKDF2' },
        false,
        ['deriveKey']
      )
    },
    async encryptWithDerivedKey(data: never, nonce: Uint8Array) {
      if (this.masterKey == null) {
        throw new Error('masterKey is null, initialize encryption store before using it.')
      }

      const derivedKey = await window.crypto.subtle.deriveKey(
        {
          name: 'PBKDF2',
          salt: nonce,
          iterations: 100000,
          hash: 'SHA-256'
        },
        this.masterKey,
        { name: 'AES-GCM', length: 256 },
        false,
        ['encrypt', 'decrypt']
      )

      const dataBuffer = encoder.encode(JSON.stringify(data))

      const cipherText = await window.crypto.subtle.encrypt(
        { name: 'AES-GCM', iv: nonce },
        derivedKey,
        dataBuffer
      )

      return {
        cipherText: arrayBufferToHex(new Uint8Array(cipherText)),
        nonce: arrayBufferToHex(nonce)
      }
    },
    async decriptWithDerivedKey(data: string, nonce: string) {}
  }
})

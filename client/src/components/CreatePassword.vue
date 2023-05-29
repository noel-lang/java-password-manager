<template>
  <form @submit.prevent="submit" class="space-y-3">
    <input type="text" v-model="formData.service" placeholder="Service" class="form-control" />
    <input type="text" v-model="formData.password" placeholder="Passwort" class="form-control" />
    <input type="submit" value="Speichern" class="btn btn-outline-primary" />
  </form>
</template>

<script lang="ts" setup>
import {ref} from "vue";

const encoder = new TextEncoder();

const formData = ref({
  service: "",
  password: ""
});

const props = defineProps({
  masterPassword: {
    type: String,
    required: true
  }
});

const emits = defineEmits(['submit']);

const submit = async () => {
  const nonce = window.crypto.getRandomValues(new Uint8Array(12));

  const deriveKey = async (masterPassword, nonce) => {
    let masterKey = await window.crypto.subtle.importKey(
        'raw',
        encoder.encode(props.masterPassword!),
        {name: 'PBKDF2'},
        false,
        ['deriveKey']
    );

    let derivedKey = await window.crypto.subtle.deriveKey(
        {
          name: 'PBKDF2',
          salt: nonce,
          iterations: 100000,
          hash: 'SHA-256',
        },
        masterKey,
        { name: 'AES-GCM', length: 256 },
        false,
        ['encrypt', 'decrypt']
    );

    return derivedKey;
  };

  const encryptPasswordEntry = async (derivedKey) => {
    const passwordEntryBuffer = encoder.encode(JSON.stringify(formData));

    const cipherText = await window.crypto.subtle.encrypt(
        { name: "AES-GCM", iv: nonce },
        derivedKey,
        passwordEntryBuffer
    );

    return new Uint8Array(cipherText);
  };

  const derivedKey = await deriveKey(props.masterPassword, nonce);
  const cipherText = await encryptPasswordEntry(derivedKey);

  emits('submit', { cipherText, nonce });
};
</script>

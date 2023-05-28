<template>
  <div class="p-12 grid grid-cols-4 gap-6">
	  <div class="flex flex-col space-y-6">
      <h2 class="text-3xl font-bold">Authentifizierung</h2>
      <input v-model="username" type="text" placeholder="Username" class="p-3 border-2 border-black border-solid" />
      <input v-model="password" type="text" placeholder="Passwort" class="p-3 border-2 border-black border-solid" />
      <button @click.prevent="register" type="button" class="rounded-md bg-indigo-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Registrieren</button>
		  <button @click.prevent="login" type="button" class="rounded-md bg-indigo-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Anmelden</button>
      <div class="break-words text-xs">{{ token }}</div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import axios from "axios";

const encoder = new TextEncoder();

const username = ref('noel');
const password = ref('12345678');
const token = ref('');

const generateMasterPasswordKey = async () => {
  return await window.crypto.subtle.importKey(
      'raw',
      encoder.encode(password.value),
      {name: 'PBKDF2'},
      false,
      ['deriveBits', 'deriveKey']
  );
};

const hashPassword = async (masterPasswordKey, salt) => {
  return await window.crypto.subtle.deriveBits(
      {
        name: 'PBKDF2',
        salt: encoder.encode(salt),
        iterations: 1000,
        hash: 'SHA-256'
      },
      masterPasswordKey,
      256
  );
};

const arrayBufferToHex = (arrayBuffer) => {
  return Array.from(new Uint8Array(arrayBuffer)).map(b => b.toString(16).padStart(2, '0')).join('');
}

const generateRandomBuffer = () => {
  const buffer = new Uint8Array(16);
  window.crypto.getRandomValues(buffer);
  return buffer;
};

const register = async () => {
  const salt = arrayBufferToHex(generateRandomBuffer());
  const masterPasswordKey = await generateMasterPasswordKey();
	const hashedPassword = await hashPassword(masterPasswordKey, salt);
	const hashedPasswordHex = arrayBufferToHex(hashedPassword);

	const response = await axios.post("http://localhost:8080/authentication/register", {
    username: username.value,
    hashedPassword: hashedPasswordHex,
    salt: salt
  });
};

const login = async () => {
	// 1. Retrieve Salt
  const saltResponse = await axios.get(`http://localhost:8080/authentication/${username.value}/salt`);
  const salt = saltResponse.data.salt;

  // 2. Hash Password
  const masterPasswordKey = await generateMasterPasswordKey();
  const hashedPassword = await hashPassword(masterPasswordKey, salt);
  const hashedPasswordHex = arrayBufferToHex(hashedPassword);

  // 3. Perform Login
	const response = await axios.post("http://localhost:8080/authentication/token", {}, {
	  auth: {
		  username: username.value,
		  password: hashedPasswordHex
	  }
  });

	token.value = response.data;
};
</script>

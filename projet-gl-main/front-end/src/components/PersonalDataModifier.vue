<template>
  <div class="modify-data-container">
    <div class="form-container">
      <hr>
      <h2 class="form-title">{{ $t('modify-data.title') }}</h2>
      <hr>

      <form @submit.prevent="postModifyData">

        <div class="form-group">
          <label for="telephone">{{ $t('modify-data.phone-number') }}</label>
          <input type="number" id="telephone" v-model="dataToSend.address.phonenumber" class="center-input">
        </div>

        <div class="form-group">
          <label for="email">{{ $t('modify-data.email') }}</label>
          <input type="email" id="email" v-model="dataToSend.privateEmail" class="center-input">
        </div>

        <div class="form-group">
          <label for="address">{{ $t('modify-data.address') }}</label>
          <input type="text" id="address" v-model="dataToSend.address.adress" class="center-input">
        </div>

        <div class="form-group">
          <label for="address2">{{ $t('modify-data.address2') }}</label>
          <input type="text" id="address2" v-model="dataToSend.address.adress2" class="center-input">
        </div>

        <div class="form-group">
          <label for="city">{{ $t('modify-data.city') }}</label>
          <input type="text" id="city" v-model="dataToSend.address.city" class="center-input">
        </div>

        <div class="form-group">
          <label for="region">{{ $t('modify-data.region') }}</label>
          <input type="text" id="region" v-model="dataToSend.address.region" class="center-input">
        </div>

        <div class="form-group">
          <label for="postalCode">{{ $t('modify-data.postal-code') }}</label>
          <input type="text" id="postalCode" v-model="dataToSend.address.postalCode" class="center-input">
        </div>

        <div class="form-group">
          <label for="country">{{ $t('modify-data.country') }}</label>
          <input type="text" id="country" v-model="dataToSend.address.country" class="center-input">
        </div>
        <button type="submit" class="submit-button">{{ $t('modify-data.modify') }}</button>
      </form>
    </div>
    <PopUp :showPopup="showPopup" v-if="showPopup" @close="showPopup = false" :message="popupMessage"/>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import api from "@/plugins/api"
import store from "@/plugins/store"
import PopUp from "./PopUp.vue";


const dataToSend = ref({
  privateEmail: null,
  address: {
    phonenumber: null,
    city: null,
    region: null,
    postalCode: null,
    country: null,
    adress: null,
    adress2: null
  }
})


const showPopup = ref(false)
const popupMessage = ref('')


async function postModifyData() {
  try {
    console.log(dataToSend.value)
    console.log(dataToSend.value.address.privateEmail)
    if (dataToSend.value.privateEmail != null 
      || dataToSend.value.address.phonenumber != null 
      || dataToSend.value.address.city != null 
      || dataToSend.value.address.region != null 
      || dataToSend.value.address.postalCode != null
      || dataToSend.value.address.country != null
      || dataToSend.value.address.adress != null
      || dataToSend.value.address.adress2 != null){
      const matricule = store.getters.userData.matricule;
      const response = await api.patch(`/users/${matricule}`, dataToSend.value);
      const udata = await api.get(`/users/${matricule}`);
      await store.dispatch("setUserData", udata.data);
      popupMessage.value = response.data
      dataToSend.value.privateEmail = null 
      dataToSend.value.address.phonenumber = null 
      dataToSend.value.address.city = null 
      dataToSend.value.address.region = null 
      dataToSend.value.address.postalCode = null
      dataToSend.value.address.country = null
      dataToSend.value.address.adress = null
      dataToSend.value.address.adress2 = null
    }
    else{
      popupMessage.value = "Fill in at least one field"
    }
  } catch (e) {
    console.error(e);
    popupMessage.value = e.response.data
  } finally {
    showPopup.value = true
  }
}
</script>

<style scoped>
.center-input {
  width: calc(100% - 16px);
  padding: 8px;
  font-size: 12px;
  border-radius: 5px;
  border: 1px solid #ccc;
  margin: 0 auto;
  display: block;
}

.modify-data-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
}


.form-container {
  text-align: center;
  margin-bottom: 20px;
}

.form-title {
  font-size: 24px;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.submit-button {
  background-color: #D9D9D9;
  color: #000;
  border: none;
  border-radius: 5px;
  padding: 10px 20px;
  cursor: pointer;
  transition: background-color 0.4s ease-in-out;
  font-family: "Kode Mono", monospace;
}


.submit-button:hover {
  background-color: #AFAFAF;
}

.message {
  margin-top: 10px;
  color: #000;
}
</style>

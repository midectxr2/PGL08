<template>
  <div class="container">
    <hr>
    <h2>{{ $t('aca-infos.title') }}</h2>
    <hr>

    <p class="cursus">{{ $t('aca-infos.cursus') }} {{ cursus }}</p>
    <!--<p class="cursus">{{ $t('aca-infos.level') }} </p>-->
    <p class="cursus">{{ $t('aca-infos.aca-year') }} {{ previousYear }} - {{ currentYear }}</p>

    <!--<button class="btn" @click="myCourses">{{ $t('aca-infos.my-courses') }}</button>-->
    <button class="btn" @click="sendProcedure('Registration')">{{ $t('aca-infos.re-roll') }}</button>
    <button class="btn" @click="sendProcedure('Unregister')">{{ $t('aca-infos.unroll') }}</button>
    <PopUp :showPopup="showPopup" v-if="showPopup" @close="showPopup = false" :message="popupMessage" />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import api from "@/plugins/api.js";
import store from "@/plugins/store.js";
import PopUp from '@/components/PopUp.vue';

const cursus = ref('');
const currentYear = ref('');
const previousYear = ref('');
const showPopup = ref(false);
const popupMessage = ref('');

async function sendProcedure(type) {
  try {
    const response = await api.post(`procedures/${store.getters.userData.matricule}/send?type=${type}`);
    popupMessage.value = response.data
    showPopup.value = true
    console.log(response.data);
  } catch (e) {
    console.error(e);
  }
}

onMounted(async () => {
  const matricule = store.getters.userData.matricule;
  const response = await api.get(`/students/${matricule}`);
  console.log(response.data);
  cursus.value = response.data.cursusTitle;
  const date = new Date();
  currentYear.value = date.getFullYear();
  previousYear.value = (currentYear.value) -1
  console.log(currentYear);
  showPopup
  popupMessage
});


</script>

<style scoped>
.container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 20px;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #D9D9D9;
  color: #000;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease-in-out;
  font-family: "Kode Mono", monospace;
}

button:hover{
  background-color: #AFAFAF;
  
}


.btn {
  margin: 1%;
}
</style>

<template>
  <div class="container">
    <hr>
    <h1>{{ $t("procedure-management.title") }}</h1>
    <hr>
    <ul v-if="procedureList.length > 0" class="procedureList">
      <li
        v-for="(procedure, index) in procedureList"
        :key="index"
        class="procedure"
      >
        <div class="procedure-details">
          <p>
            <span class="label">Type :</span> {{ procedure.type }}, 
            <span class="label">Matricule :</span> {{ procedure.studentData.matricule }}, 
            <span class="label">{{ $t("procedure-management.firstname") }}</span> {{ procedure.studentData.firstname }}, 
            <span class="label">{{ $t("procedure-management.lastname") }}</span> {{ procedure.studentData.lastname }}
          </p>
          <div class="buttons-container">
            <button @click="sendResponse(procedure.studentData.matricule, `Your ${procedure.type} procedure has been accepted`, index)"
              class="acceptButton">
              {{ $t("procedure-management.accept") }}
            </button>
            <button @click="sendResponse(procedure.studentData.matricule, `Your ${procedure.type} procedure has been rejected`, index)"
              class="rejectButton">
              {{ $t("procedure-management.reject") }}
            </button>
          </div>
        </div>
      </li>
    </ul>
    <div v-else>
      <p>
        {{ $t("procedure-management.no-procedure") }}
      </p>
    </div>
    <PopUp :showPopup="showPopup" v-if="showPopup" @close="showPopup = false" :message="popupMessage" />
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import api from "@/plugins/api.js";
import PopUp from "@/components/PopUp.vue";

const procedureList = ref([]);
const showPopup = ref(false);
const popupMessage = ref('');

async function fetchProcedures() {
  try {
    const response = await api.get("/procedures")
    procedureList.value = response.data
    console.log("procedures successfully fetched")
  } catch (e) {
    console.log("Something wrong")
    console.error(e)
  }
}

async function sendResponse(matricule, msg, index) {
  try {
    const response = await api.post("/procedures/sendResponse", {matricule: matricule, message: msg})
    console.log(response.data)
    popupMessage.value = "Response sent"
    showPopup.value = true
    procedureList.value.splice(index)
  } catch (e) {
    console.log(e)
  }
}

onMounted(fetchProcedures)
</script>

<style lang="scss" scoped>
.container {
  max-width: 100%;
  margin: 0 auto;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
}

h1 {
  text-align: center;
}

.procedureList {
  list-style-type: none;
  padding: 0;
}

.procedure {
  margin-bottom: 20px;
  border: 1px solid;
  border-radius: 5px;
  padding: 10px;
  transition: border-width 0.3s, box-shadow 0.3s;
}

.procedure:hover{
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.8);
}

.procedure-details {
  display: flex;
  justify-content: space-between;
  align-items: center; 
}

.buttons-container {
  display: flex;
  align-items: center; 
}

.acceptButton,
.rejectButton {
  margin-left: 10px;
  margin-right: 10px; 
  padding: 8px 16px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.4s ease-in-out;
}

.acceptButton {
  background-color: #349E38;
  color: white;
  
}

.acceptButton:hover{
  background-color: #238627;
}

.rejectButton {
  background-color: #E13124;
  color: white;
}

.rejectButton:hover{
  background-color: #CC2619;
}

.popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.label {
  font-weight: bold;
  margin-right: 5px;
}
</style>

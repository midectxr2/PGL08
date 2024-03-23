<template>
    <div class="container">
      <hr>
      <h2>{{ $t('registration-status.title') }}</h2>
      <hr>
      <p v-if="statutInscription === 'Enrolled'" class="success-message">{{ $t('registration-status.succes') }}</p>
      <p v-else-if="statutInscription === 'InProgress'" class="error-message">{{ $t('registration-status.in-progress') }}</p>
      <p v-else class="error-message">{{ $t('registration-status.not-found') }}</p>
    </div>
  </template>

<script>
import store from "@/plugins/store"
import api from "@/plugins/api"

export default {
  data() {
    return {
      statutInscription: null
    };
  },
  async mounted() {
    const matricule = store.getters.userData.matricule;
    const response = await api.get(`/students/${matricule}`)
    this.statutInscription = response.data.registrationStatus;
    console.log(this.statutInscription);

    console.log(response.data)
  }
};
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

  .success-message {
    color: green;
  }

  .error-message {
    color: red;
  }
  </style>

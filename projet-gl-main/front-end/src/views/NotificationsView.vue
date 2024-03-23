<template>
  <div class="notifications-container">
    <hr>
    <h1 class="title"> Notifications </h1>
    <hr>
    <ul v-if="isDataFetched && !isListEmpty" class="notifications-list">
      <li class="notification" v-for="(notification, index) in notifications" :key="index">
        {{ notification.message }}
      </li>
    </ul>
    <div v-show="isDataFetched && isListEmpty">
      <p>
        {{ $t("notifs.zero") }}
      </p>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from "vue";
import api from "@/plugins/api.js"; 
import store from "@/plugins/store.js";

const notifications = ref([]);
const isDataFetched = ref(false);

const isListEmpty = computed(() => {
  return notifications.value.length === 0;
});

async function fetchNotifications() {
  try {
    const response = await api.get(`/notifs/${store.getters.userData.matricule}`);

    notifications.value = response.data;

    isDataFetched.value = true;
  } catch (e) {
    notifications.value = [];
    console.error(e);
  }
}

async function clearNotifications() {
  try {
    const response = await api.get(`/notifs/${store.getters.userData.matricule}/clear`);
  } catch (e) {
    console.log(e)
  }
}

onUnmounted(clearNotifications);
onMounted(fetchNotifications);
</script>

<style lang="scss" scoped>
[v-cloak] {
  display: none;
}

.notifications-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
}



.notifications-list {
  list-style-type: none;
  padding: 0;
}

.notifications-list li {
  border: 1px solid black;
  margin: 3%;
  padding: 15px;
  border-radius: 5px;
  transition: border-width 0.3s, box-shadow 0.3s;
}

.notifications-list li:hover {
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.8);
}
</style>
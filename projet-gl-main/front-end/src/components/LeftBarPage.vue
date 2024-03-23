<template>
  <div class="vertical-bar">
    <button class="btn btn-custom" v-for="button in buttons" :key="button.id"
            @click="() => router.push(Tr.i18nRoute({ name: button.link }))">{{ $t(button.label) }}
    </button>
  </div>
</template>

<script setup>
import Tr from '@/plugins/translation';
import router from "@/plugins/router.js";

defineProps({
  buttons: {
    type: Array,
    default: () => []
  },
});
</script>

<style lang="scss" scoped>
.vertical-bar {
  display: flex;
  flex-direction: column;
  margin-top: 20px;
  padding: 1%;
}

.btn {
  margin: 3%;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  color: #fff;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease-in-out;
  font-family: "Kode Mono", monospace;
}

.btn:hover {
  background-color: #AFAFAF;
}

.btn:focus {
  outline: none;
}

.btn-custom {
  background-color: #D9D9D9; 
  color: #000; 
}
</style>

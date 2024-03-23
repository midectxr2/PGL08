<template>
  <h1 class="main-title">{{ $t("my-actions.title") }}</h1>
  <div class="actions" v-if="buttons && buttons.length > 0">
    <button v-for="button in buttons" :key="button.id" class="action-button">{{ $t(button.label) }}</button>
  </div>
</template>

<script setup>
defineProps({
  buttons: {
    type: Array,
    default: () => []
  }
});
</script>

<style lang="scss" scoped>
.actions {
  display: flex;
  flex-direction: row;
  width: 100%;
  justify-content: space-around;
  margin-bottom: 2%;
}

.main-title {
  text-align: center;
  margin-bottom: 2%;
  font-size: 24px;
  text-decoration: underline;
}

.action-button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-color: #D9D9D9;
  color: #000;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease-in-out;
  font-family: "Kode Mono", monospace;
}

.action-button:hover {
  background-color: #AFAFAF;
}
</style>

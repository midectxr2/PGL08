<template>
  <button :type="type">{{ msg }}</button>
</template>

<script setup>
defineProps({
  msg: String,
  type: String
})
</script>

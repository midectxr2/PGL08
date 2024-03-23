<template>
  <div class="rectangle">
    <AnimatedHeader></AnimatedHeader>
  </div>
</template>

<script setup>
import AnimatedHeader from "@/components/AnimatedHeader.vue";

</script>

<style lang="scss" scoped>
.main-title {
  text-align: center;
  font-size: xx-large;
  margin: 0;
}

.rectangle {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 110px;
  background-color: #D9D9D9;
  position: relative;
  top: 0;
  left: 0;
  margin: 0 5px;
  border-radius: 7px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
}

</style>

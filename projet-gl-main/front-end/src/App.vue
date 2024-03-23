<template>
    <div class="app-container" v-cloak>
        <Nav v-if="showNav()"></Nav>
      <RouterView/>
    </div>
</template>

<script setup>
import { RouterView, useRoute } from "vue-router";
import Nav from "@/components/Nav.vue";

const route = useRoute();

const showNav = () => {
  const shouldShow = route.name !== "login";
  console.log('Current route name:', route.name);
  return shouldShow;
};
</script>

<style lang="scss">
[v-cloak] > * {
  display:none;
  &:before{
    content: "loading...";
  }
}

html,
body {
  height: 100%;
  margin: 0;
}

body {
  display: flex;
  flex-direction: column;

}

#app {
  height: 100vh;
  height: 100lvh;
  width: 100vw;
  margin: 0;
  display: block;
  text-align: center;
  font-family: "Kode Mono", monospace;
}

.app-container{
  display: flex;
  flex-direction: column;
  height: 100%;
  align-items: center;
}

input{
  font-family: monospace;
}

</style>

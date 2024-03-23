<template>
  <nav class="nav-bar">
    <ul>
      <li>
        <RouterLink class="nav-element" :to="Tr.i18nRoute({ name: 'home' })">{{ $t("nav.home") }}</RouterLink>
      </li>
      <li>
        <RouterLink class="nav-element" :to="Tr.i18nRoute({ name: 'about' })">{{ $t("nav.about") }}</RouterLink>
      </li>
      <li>
        <a class="nav-element" href="#" @click="logout">{{ $t("nav.logout") }}</a>
      </li>
      <li>
        <LanguageSwitcher></LanguageSwitcher>
      </li>
    </ul>
  </nav>
</template>

<script>
import {useRouter} from "vue-router";
import LanguageSwitcher from './LanguageSwitcher.vue';
import Tr from '../plugins/translation';
import store from "@/plugins/store.js";

export default {
  components: {LanguageSwitcher},
  setup() {
    const router = useRouter();

    const logout = async () => {
      await store.dispatch('clearAuthToken')
      await store.dispatch('clearUserData')
      localStorage.clear();
      await router.push(Tr.i18nRoute({name: 'login'}));
    };

    return {Tr, logout};
  }
}
</script>

<style lang="scss" scoped>
@use "@/assets/styles/navbar";

[v-cloak] {
  display: none;
}

</style>

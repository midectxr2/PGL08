<template>
  <div class="form-container">
    <h2>{{ $t("form.login") }}</h2>
    <form @submit.prevent="login" autocomplete="off">
      <div class="form-input-group">
        <input
          type="email"
          v-model="loginData.email"
          placeholder=" "
          required
        />
        <label>{{ $t("login.email") }} :</label>
      </div>
      <div class="form-input-group">
        <input
          type="password"
          v-model="loginData.password"
          placeholder=" "
          required
        />
        <label>{{ $t("login.passwd") }} :</label>
      </div>
      <CustButton type="submit" :msg="$t('login.connexion')"></CustButton>
    </form>
    <p class="question">
      <u>{{ $t("login.notReg") }}</u>
    </p>
    <CustButton :msg="$t('login.manReg')" @click="manageRegistration"></CustButton>
  </div>
</template>

<script>
import CustButton from "./CustButton.vue";

import {ref} from "vue";
import {useRouter} from "vue-router";

import Tr from "@/plugins/translation";
import api from "@/plugins/api";
import store from "@/plugins/store";

export default {
  name: "Login",
  setup() {
    const router = useRouter();

    const loginData = ref({
      email: "",
      password: "",
    });

    const login = async () => {
      try {
        const response = await api.post("/auth/login", loginData.value);

        const token = response.data.jwt;
        const matricule = response.data.matricule;
        console.log("Successuflly logged-in", response.data);
        store.dispatch("setAuthToken", token);

        console.log("Fetching userdata...")

        const udata = await api.get(`/users/${matricule}`)
        console.log("data fetched")

        console.log("storing data...")
        store.dispatch("setUserData", udata.data)

        console.log("data stored!!")
        console.log("UserData:", store.getters.userData)

        router.push(Tr.i18nRoute({name: "home"}));
      } catch (e) {
        console.error("Error when login, verify given informations", e);
      }
    };

    return {
      loginData,
      login,
      Tr,
    };
  },
  methods: {
    manageRegistration() {
      // Extension de Nico
      console.log("Gérer l'inscription");
    },
  },
  components: {CustButton},
};
</script>

<style lang="scss" scoped>
.question {
  color: black;
  margin-top: 20px;
}
</style>

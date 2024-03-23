<template>
  <div class="login-group">
    <AnimatedHeaderPage></AnimatedHeaderPage>
    <div class="language-switcher-container">
      <LanguageSwitcher></LanguageSwitcher>
    </div>
    <p class="prompt">{{ $t("login-page.prompt-account") }}</p>

    <div class="login-container">
      <form @submit.prevent="login">
        <div class="input-group">
          <label for="matricule">{{ $t("login-page.matricule") }} :</label>
          <input type="text" id="username" v-model="loginData.email" required>
        </div>
        <div class="input-group">
          <label for="password">{{ $t("login-page.password") }} :</label>
          <div class="password-input">
            <input 
              :type="showPassword ? 'text' : 'password'" 
              id="password" 
              v-model="loginData.password" 
              required 
              class="password-field">
            <label class="checkbox-label">
              <input type="checkbox" v-model="showPassword" class="custom-checkbox">
              <span class="checkbox-image"></span>
            </label>
          </div>
        </div>

        <button type="submit" class="btn">{{ $t("login-page.login") }}</button>
        <p class="error-message" v-if="errorMessage">{{ errorMessage }}</p>
        <p class="question"><u>{{ $t("login-page.notReg") }}</u></p>
        <button type="button" class="btn" @click="manageRegistration">{{ $t("login-page.manageReg") }}</button>
      </form>
    </div>
  </div>
  <!--<button class="publi-sci" @click="publiSciExtension">{{ $t("login-page.publiSci") }}</button>-->
</template>

<script>
import AnimatedHeaderPage from '@/components/AnimatedHeaderPage.vue';
import Tr from '../plugins/translation';
import {ref} from 'vue';
import {useRouter} from 'vue-router';
import LanguageSwitcher from '../components/LanguageSwitcher.vue';
import api from '@/plugins/api';
import store from '@/plugins/store'
import { useI18n } from 'vue-i18n';

export default {
  components: {
    AnimatedHeaderPage,
    LanguageSwitcher
  },
  setup() {
    const token = ref('');
    const router = useRouter();
    const errorMessage = ref('');
    const { t } = useI18n();

    const loginData = ref({
      email: "",
      password: "",
    });

    const showPassword = ref(false);

    const login = async () => {
      try {

        // Check si le mot de passe est bon
        const response = await api.post("/auth/login", loginData.value);

        const token = response.data.jwt;
        const matricule = response.data.matricule;
        console.log("Successuflly logged-in", response.data);
        await store.dispatch("setAuthToken", token);

        console.log("Fetching userdata...")

        const udata = await api.get(`/users/${matricule}`)
        console.log("data fetched")

        console.log("storing data...")
        await store.dispatch("setUserData", udata.data)

        console.log("data stored!!")
        console.log("UserData:", store.getters.userData)


        router.push(Tr.i18nRoute({name: "home"}));

      } catch (e) {
        console.error("Error went login, verify given informations", e);
        errorMessage.value = t("login-page.error");
      }
    };

    
    const manageRegistration = () => {
      console.log("Gérer l'inscription");
    };

    const publiSciExtension = () => {
      console.log("Extension de publications scientifiques");
    };


    return {
      login,
      loginData,
      manageRegistration,
      publiSciExtension,
      Tr,
      errorMessage,
      showPassword
    };
  }
};
</script>

<style scoped>
.login-group{
  margin-top: 5%;
}

.prompt {
  text-align: center;
  position: relative;
  margin-top: 80px;
  font-size: 20px;
}

.login-container {
  max-width: 400px;
  padding: 20px;
  background-color: #FFF;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
  margin: 80px auto 0 auto;
  margin-top: 10%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.input-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input[type="text"],
input[type="password"] {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 3px;
  box-sizing: border-box; 
}

button {
  width: 100%;
  padding: 10px;
  color: #000;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  background-color: #D9D9D9;
  font-family: "Kode Mono", monospace;
}

.btn {
  background-color: #CCC; 
  transition: background-color 0.3s ease-in-out; 
}

.btn:hover {
  background-color: #AFAFAF;
}

.question {
  margin-top: 20px;
}

.publi-sci {
  width: 120px;
  margin-top: 10px;
  margin-left: auto;
  margin-right: auto;
  display: block;
}

.language-switcher-container {
  text-align: center;
  margin-top: 30px;
  margin-bottom: 20px;
}

.error-message {
  color: red;
  margin-top: 10px;
}

.checkbox-label {
  position: relative;
  display: inline-block;
}

.custom-checkbox {
  opacity: 0; 
  position: absolute; 
  top: 0;
  left: 0;
}

.checkbox-image {
  position: absolute;
  top: 50%;
  right: 5px; 
  transform: translateY(-40%);
  width: 20px; 
  height: 20px; 
  background-repeat: no-repeat;
  background-size: contain;
}

.custom-checkbox:checked + .checkbox-image {
  background-image: url('/Hide.png'); 
}

.custom-checkbox:not(:checked) + .checkbox-image {
  background-image: url('/Show.png'); 
}

.password-input {
  display: flex;
  align-items: center;
}

.password-input input {
  flex: 1;
  padding-right: 30px; 
  background-image: none !important; 
}
</style>

<template>
  <div>
    <h1 class="main-title">{{ $t("my-data.title") }}</h1>
    <ul class="global-list">
      <li class="global-element">
        <ul class="inner-list">
          <li>{{ $t("my-data.name") }} {{ userData.lastname }}</li>
          <li>{{ $t("my-data.firstname") }} {{ userData.firstname }}</li>
          <li>{{ $t("my-data.matricule") }} {{ userData.matricule }}</li>
          <li>{{ $t("my-data.school-email") }} {{ userData.schoolEmail }}</li>
          <li>{{ $t("my-data.perso-email") }} {{ userData.privateEmail }}</li>
        </ul>
      </li>


      <li class="global-element">
        <ul class="inner-list">
          <li>{{ $t("my-data.phone-number") }} {{ userData.address.phonenumber }}</li>
          <li>{{ $t("my-data.address") }} {{ userData.address.adress }}</li>
          <li class="add">{{ userData.address.postalCode }}, {{ userData.address.city }}</li>
          <li class="add">{{ userData.address.region }}, {{ userData.address.country }}</li>
        </ul>
      </li>
    </ul>
  </div>
</template>

<script setup>
import LanguageSwitcher from '@/components/LanguageSwitcher.vue';
import Tr from '../plugins/translation'
import store from "@/plugins/store.js";

const userData = store.getters.userData

</script>

<style lang="scss" scoped>
.main-title {
  text-align: center;
  margin-bottom: 50px;
  font-size: 24px;
  text-decoration: underline;
}

ul {
  padding: 0;
}

li {
  list-style: none;
}

.global-list {
  list-style-type: none;
  padding: 0;
  text-align: center;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;

  .global-element {

    .inner-list {
      display: flex;
      flex-direction: column;
      margin: 0 40px;

      li {
        margin-bottom: 5%;
        text-align: left;
        font-size: 18px;
      }

      .add {
        text-align: center;
      }
    }

    &:first-child {
      border-right: 1px solid #AFAFAF;
      
    }

    &:last-child {
      border-left: 1px solid #ccc;
      padding-right: 70px;
    }
  }
}


</style>

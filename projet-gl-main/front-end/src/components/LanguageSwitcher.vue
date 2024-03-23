<template>
  <div
    class="language-dropdown"
    v-on:mouseover="showDropdown"
    v-on:mouseleave="hideDropdown"
  >
    <span class="nav-element">{{ $t("lang.title") }}</span>
    <div
      class="dropdown-content"
      v-show="isDropdownVisible"
      @click="switchLanguage"
    >
      <a
        v-for="sLocale in supportedLocales"
        :key="`local-${sLocale}`"
        :class="{ selected: locale === sLocale }"
        :data-locale="sLocale"
      >
        {{ t(`locale.${sLocale}`) }}
      </a>
    </div>
  </div>
</template>

<script>
import {ref} from "vue";
import {useI18n} from "vue-i18n";
import {useRouter} from "vue-router";
import Tr from "@/plugins/translation";

export default {
  setup() {
    const {t, locale} = useI18n();

    const supportedLocales = Tr.supportedLocales;

    const router = useRouter();

    const isDropdownVisible = ref(false);

    const showDropdown = () => {
      isDropdownVisible.value = true;
    };

    const hideDropdown = () => {
      isDropdownVisible.value = false;
    };

    const switchLanguage = async (event) => {
      const newLocale = event.target.getAttribute("data-locale");

      await Tr.switchLanguage(newLocale);

      try {
        await router.replace({params: {locale: newLocale}});
      } catch (e) {
        console.error(e);
        router.push("/");
      }
    };

    return {
      t,
      locale,
      supportedLocales,
      switchLanguage,
      isDropdownVisible,
      hideDropdown,
      showDropdown,
    };
  },
};
</script>

<style lang="scss" scoped>
@use "@/assets/styles/navbar";

.language-dropdown {
  position: relative;
  display: inline-block;
}

span {
  cursor: pointer;
}

.dropdown-content {
  display: block;
  position: absolute;
  width: 100%;
  z-index: 1;

  font-size: 90%;


  background-color: rgba(64, 255, 182, 0.43);

  border-radius: 0 0 3px 3px;

  a {
    padding: 5% 0;
    text-decoration: none;
    display: block;
    cursor: pointer;
  }
}

.selected {
  font-weight: bold;
  color: #feb72b;
}
</style>

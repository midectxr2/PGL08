import {createI18n} from 'vue-i18n';
import pluralRules from './rules/pluralisation';
import numberFormats from './rules/numbers'
import datetimeFormats from './rules/datetime'
import en from './locales/en.json';

export default createI18n({
  locale: import.meta.env.VITE_DEFAULT_LOCALE,
  fallbackLocale: import.meta.env.VITE_FALLBACK_LOCALE,
  legacy: false,
  globalInjection: true,
  messages: {en},
  pluralRules,
  numberFormats,
  datetimeFormats,
})

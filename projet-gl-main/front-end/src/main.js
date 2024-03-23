import { createApp } from 'vue/dist/vue.esm-bundler'
import App from '@/App.vue'
import router from '@/plugins/router';
import store from '@/plugins/store';
import i18n from '@/plugins/i18n';

store.dispatch('initializeAuthToken')

const app = createApp(App)
app.use(store).use(router).use(i18n)
app.mount('#app')

import { fileURLToPath, URL } from "node:url";
import { resolve, dirname } from "node:path";
import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import VueI18nPlugin from "@intlify/unplugin-vue-i18n/vite";

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    port: 8081,
  },
  plugins: [
    vue(),
    VueI18nPlugin({
      include: resolve(
        dirname(fileURLToPath(import.meta.url)),
        "./src/locales/*"
      ),
    }),
  ],
  build: {
    outDir: '../back-end/api/src/main/resources/static/',
    emptyOutDir: true,
  },
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  optimizeDeps: {
    exclude: ["@/components/HelloWorld.vue"],
  },
});

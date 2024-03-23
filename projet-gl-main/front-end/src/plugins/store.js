import {createStore} from "vuex";
import api from "@/plugins/api.js";

export default createStore({
  state: {
    authToken: localStorage.getItem("authToken") || null,
    userData: localStorage.getItem("userData"),
  },
  mutations: {
    setAuthToken(state, token) {
      state.authToken = token;
      localStorage.setItem("authToken", token);
    },
    setUserData(state, data) {
      state.userData = data
      localStorage.setItem("userData", JSON.stringify(data));
      console.log(state.userData)
    },
    clearAuthToken(state) {
      state.authToken = null;
      localStorage.removeItem("authToken");
    },
    clearUserData(state) {
      state.userData = null;
      localStorage.removeItem("userData")
    }
  },
  actions: {
    setAuthToken({commit}, token) {
      commit("setAuthToken", token);
    },
    setUserData({commit}, data) {
      commit("setUserData", data);
    },
    clearAuthToken({commit}) {
      commit("clearAuthToken");
    },
    clearUserData({commit}) {
      commit("clearUserData")
    },
    async initializeAuthToken({commit}) {
      const token = localStorage.getItem("authToken")
      try {
        if (token) {
          commit("setAuthToken", token)
          await api.get("/auth/validation")
          console.log("Token is ok")

          commit("setUserData", JSON.parse(localStorage.getItem("userData")))
        } else console.log("no token")
      } catch (e) {
        localStorage.clear();
        console.log("Token expired...")
      }
    },
  },
  getters: {
    authToken: (state) => state.authToken,
    userData: (state) => {
      if (!state.userData) {
        state.userData = JSON.parse(localStorage.getItem("userData"))
        console.log("loaded")
      }

      return state.userData
    },
  },
  modules: {},
});

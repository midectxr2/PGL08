import axios from 'axios';
import store from '@/plugins/store';


const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

api.interceptors.request.use(
  (config) => {
    const authToken = store.getters.authToken
    if (authToken) {
      config.headers.Authorization = `Bearer ${authToken}`
    }

    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

export default api

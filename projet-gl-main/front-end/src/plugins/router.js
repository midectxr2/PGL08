import {createRouter, createWebHistory, RouterView} from 'vue-router';
import Tr from './translation';
import store from "./store";
import api from "./api";

const routes = [
  {
    path: '/:locale?',
    component: RouterView,
    beforeEnter: Tr.routeMiddleware,
    redirect: {
      name: "home"
    },
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import('@/views/LoginView.vue'),
        meta: {guest: true},
      },
      {
        path: 'home',
        name: 'home',
        component: RouterView,
        meta: {requiresAuth: true},
        redirect: {
          name: "homeMain"
        },
        children: [
          {
            path: '',
            name: 'homeMain',
            component: () => import('@/views/HomeView.vue'),
            meta: {requiresAuth: true}
          },
          {
            path: 'modify-data',
            name: 'modify-data',
            component: ()=> import('@/views/ModifyDatasView.vue'),
            meta: { requiresAuth: true }
          },
          {
            path: 'academic-infos',
            name: 'academic-infos',
            component: ()=> import('@/views/AcademicInfosView.vue'),
            meta: { requiresAuth: true }
          },
          {
            path: 'check-registration',
            name: 'check-registration',
            component: () => import('@/views/CheckRegistrationView.vue'),
            meta: {requiresAuth: true}
          },
          {
            path: 'listCourseSecretary',
            name: 'listCourseSecretary',
            component: () => import('@/views/ListCourseSecretaryView.vue'),
            meta: {requiresAuth: true}
          },
          {
            path: 'listCourseTeacher',
            name: 'listCourseTeacher',
            component: () => import('@/views/ListCourseTeacherView.vue'),
            meta: {requiresAuth: true}
          },
          {
            path: 'create-course',
            name: 'create-course',
            component: () => import('@/views/CreateCourseView.vue'),
            meta: {requiresAuth: true}
          },
          {
            path: 'notifications',
            name: 'notifications',
            component: () => import('@/views/NotificationsView.vue'),
            meta: {requiresAuth: true}
          }
        ]
      },
      {
        path: 'about',
        name: 'about',
        component: () => import('@/views/AboutView.vue'),
        meta: {guest: true}
      },
      {
        path: 'regService',
        name: 'regService',
        component: RouterView,
        children: [
          {
            path: "studentManagement",
            name: "studentManagement",
            component: () => import("@/views/StudentManagementView.vue"),
            meta: {requiresAuth: true}
          },
          {
            path: "procedureManagement",
            name: "procedureManagement",
            component: () => import("@/views/AcademicProcedureManageView.vue"),
            meta: {requiresAuth: true}
          }
        ]
      }
    ],
  },
]


const router = createRouter({
  routes: routes,
  history: createWebHistory(import.meta.env.VITE_BASE_URL),
});

router.beforeEach(async (to, _from, next) => {
  const authToken = store.getters.authToken;
  const isAuthenticated = authToken !== null;

  if (to.meta.requiresAuth && !isAuthenticated) {
    //If user try to access a page than need authentification
    console.log("What are you trying to do, who are you?");
    next(Tr.i18nRoute({name: "login"}));
  } else if (
    (to.meta.requiresAuth && isAuthenticated)
  ) {
    //User already have a token so check his validity
    console.log("Starting token validation...");
    try {
      await api.get("/auth/validation");
      console.log("check ok, you can pass!");
      next();
    } catch (e) {
      //An error due to the token non-valid
      console.log("Your token is no longer valid!");
      await store.dispatch("clearAuthToken");
      next(Tr.i18nRoute({name: "login"}));
    }
  } else {
    next();
  }
});

export default router;

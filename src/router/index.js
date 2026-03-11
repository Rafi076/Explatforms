import Vue from "vue";
import VueRouter from "vue-router";
import LoginView from "../views/LoginView.vue";
import DashboardView from "../views/DashboardView.vue";
import ForgotPasswordView from "@/views/ForgotPasswordView.vue";
import SignUpView from "@/views/SignUpView.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    redirect: "/login",
  },


  {
    path: "/login",
    component: LoginView,
  },
  {
    path: "/forgot-password",
    component: ForgotPasswordView,
  },
  {
    path: "/signup",
    component: SignUpView,
  },
  {
    path: "/dashboard",
    component: DashboardView,
  },
];

const router = new VueRouter({
  mode: "history",
  routes,
});

export default router;
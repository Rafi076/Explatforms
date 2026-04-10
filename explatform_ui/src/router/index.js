import Vue from "vue";
import VueRouter from "vue-router";

import LoginView from "../views/LoginView.vue";
import ForgotPasswordView from "../views/ForgotPasswordView.vue";
import SignUpView from "../views/SignUpView.vue";

import DashboardLayout from "../layouts/DashboardLayout.vue";
import DashboardHomeView from "../views/DashboardHomeView.vue";
import EmployeeDetailView from "../views/EmployeeDetailView.vue";
import DepartmentDetailView from "../views/DepartmentDetailView.vue";
import WarehouseView from "../views/WarehouseDetailView.vue";
import WarehouseStateView from "../views/WarehouseWorkListView.vue";
import LedgerView from "../views/LedgerView.vue";
import EquipOrderPurchaseView from "../views/EquipOrderPurchaseView.vue";
import ReportView from "../views/ReportView.vue";
import SettingView from "../views/SettingView.vue";
import DepartmentWorkListView from "../views/DepartmentWorkListView.vue";

Vue.use(VueRouter);

const routes = [
  { path: "/", redirect: "/login" },
  { path: "/login", component: LoginView },
  { path: "/forgot-password", component: ForgotPasswordView },
  { path: "/signup", component: SignUpView },

  {
    path: "/dashboard",
    component: DashboardLayout,
    children: [
      {
        path: "",
        name: "DashboardHome",
        component: DashboardHomeView,
      },
      {
        path: "employee-detail",
        name: "EmployeeDetail",
        component: EmployeeDetailView,
      },
      {
        path: "department-detail",
        name: "DepartmentDetail",
        component: DepartmentDetailView,
      },
      {
        path: "department-work/:departmentId",
        name: "DepartmentWorkList",
        component: DepartmentWorkListView,
      },
      {
        path: "warehouse",
        name: "Warehouse",
        component: WarehouseView,
      },
      {
        path: "warehouse-state/:warehouseId",
        name: "WarehouseState",
        component: WarehouseStateView,
      },
      {
        path: "ledger",
        name: "Ledger",
        component: LedgerView,
      },
      {
        path: "equip-order-purchase",
        name: "EquipOrderPurchase",
        component: EquipOrderPurchaseView,
      },
      {
        path: "report",
        name: "Report",
        component: ReportView,
      },
      {
        path: "setting",
        name: "Setting",
        component: SettingView,
      },
    ],
  },
];

export default new VueRouter({
  mode: "history",
  routes,
});
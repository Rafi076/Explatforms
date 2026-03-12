<template>
  <div class="dashboard-page">
    <AppNavbar />

    <div class="dashboard-body">
      <AppSidebar />

      <main class="dashboard-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script>
import AppNavbar from "../components/Navbar.vue";
import AppSidebar from "../components/Sidebar.vue";

export default {
  name: "DashboardLayout",
  components: {
    AppNavbar,
    AppSidebar,
  },
};
</script>

<style scoped>
.dashboard-page {
  min-height: 100vh;
  background: #f5f7fb;
}

.dashboard-body {
  display: flex;
}

.dashboard-content {
  flex: 1;
  padding: 24px;
  box-sizing: border-box;
}
</style>
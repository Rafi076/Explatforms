<template>
  <router-view />
</template>

<script>
export default {
  name: "App",
};
</script>

<style>
html,
body,
#app {
  margin: 0;
  padding: 0;
  width: 100%;
  min-height: 100%;
}

body {
  background: #f5f7fb;
  color: #111827;
  transition: background 0.3s ease, color 0.3s ease;
}

body.dark-mode {
  background: #111827 !important;
  color: #f9fafb !important;
}

body.dark-mode .top-navbar {
  background: #1f2937 !important;
  border-bottom: 1px solid #374151 !important;
}

body.dark-mode .sidebar {
  background: #0f172a !important;
  color: #f9fafb !important;
}

body.dark-mode .dashboard-page,
body.dark-mode .dashboard-content {
  background: #111827 !important;
  color: #f9fafb !important;
}

body.dark-mode .dashboard-card {
  background: #1f2937 !important;
  color: #f9fafb !important;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.3) !important;
}

body.dark-mode .dashboard-card h3,
body.dark-mode .dashboard-card p,
body.dark-mode .dashboard-content h1 {
  color: #f9fafb !important;
}

body.dark-mode .nav-btn {
  background: #374151 !important;
  color: #f9fafb !important;
}

body.dark-mode .nav-btn:hover {
  background: #4b5563 !important;
}

body.dark-mode .sub-item {
  background: #1e293b !important;
  color: #f9fafb !important;
}
</style>
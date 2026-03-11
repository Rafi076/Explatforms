<template>
  <div class="dashboard">
    <h1>Dashboard</h1>
    <p>Login successful.</p>
    <button @click="logout">Logout</button>
  </div>
</template>

<script>
export default {
  name: "DashboardView",
  methods: {
    logout() {
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 14px;
}

button {
  padding: 12px 24px;
  background: #8dc52b;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}
</style>
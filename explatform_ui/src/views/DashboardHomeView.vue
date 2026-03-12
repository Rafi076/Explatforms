<template>
  <div>
    <h1>Dashboard</h1>
    <DashboardCards />
  </div>
</template>

<script>
import DashboardCards from "../components/DashboardCards.vue";

export default {
  name: "DashboardHomeView",
  components: {
    DashboardCards,
  },
};
</script>

<style scoped>
h1 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #1f2937;
}
</style>
<template>
  <div class="cards-wrapper">
    <div class="cards-track">
      <div class="dashboard-card" v-for="(card, index) in repeatedCards" :key="index">
        <h3>{{ card.title }}</h3>
        <p>{{ card.description }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "DashboardCards",
  data() {
    return {
      cards: [
        { title: "Employee Overview", description: "Manage all employee records easily." },
        { title: "Department Summary", description: "Track department information and structure." },
        { title: "Warehouse Status", description: "View stock and warehouse activities." },
        { title: "Ledger Report", description: "Monitor financial ledger and transactions." },
        { title: "Equipment Orders", description: "Check equipment purchase and order details." },
      ],
    };
  },
  computed: {
    repeatedCards() {
      return [...this.cards, ...this.cards];
    },
  },
};
</script>

<style scoped>
.cards-wrapper {
  width: 100%;
  height: calc(100vh - 130px);
  overflow: hidden;
  position: relative;
}

.cards-track {
  display: flex;
  flex-direction: column;
  gap: 20px;

  animation: moveUp 25s linear infinite;
  animation-delay: 5s;
  animation-fill-mode: forwards;
}

.dashboard-card {
  background: white;
  border-radius: 18px;
  padding: 24px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  min-height: 120px;
}

.dashboard-card h3 {
  margin: 0 0 10px 0;
  color: #1f2937;
}

.dashboard-card p {
  margin: 0;
  color: #23478f;
  line-height: 1.9;
}

@keyframes moveUp {
  0% {
    transform: translateY(0%);
  }

  100% {
    transform: translateY(-50%);
  }
}
</style>
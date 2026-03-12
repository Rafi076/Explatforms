<template>
  <aside class="sidebar">
    <ul>
      <li @click="$router.push('/dashboard')">Home</li>
      <li @click="$router.push('/dashboard/employee-detail')">Employee Detail</li>
      <li @click="$router.push('/dashboard/department-detail')">Department Detail</li>
      <li @click="$router.push('/dashboard/warehouse')">Warehouse</li>
      <li @click="$router.push('/dashboard/ledger')">Ledger</li>
      <li @click="$router.push('/dashboard/equip-order-purchase')">Equip Order & Purchase</li>

      <li class="menu-title toggle-item" @click="toggleTerm">
        <span>Term</span>
        <span class="arrow" :class="{ rotate: showTermItems }">▼</span>
      </li>

      <template v-if="showTermItems">
        <li class="sub-item" @click="$router.push('/dashboard/report')">Report</li>
        <li class="sub-item" @click="$router.push('/dashboard/setting')">Setting</li>
        <li class="sub-item" @click="$router.push('/login')">Logout</li>
      </template>
    </ul>
  </aside>
</template>

<script>
export default {
  name: "AppSidebar",
  data() {
    return {
      showTermItems: false,
    };
  },
  methods: {
    toggleTerm() {
      this.showTermItems = !this.showTermItems;
    },
  },
};
</script>

<style scoped>
.sidebar {
  width: 250px;
  min-height: calc(100vh - 70px);
  background: #1f2937;
  color: white;
  padding: 24px 18px;
  box-sizing: border-box;
  flex-shrink: 0;
}

ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

li {
  padding: 12px 14px;
  margin-bottom: 10px;
  border-radius: 10px;
  cursor: pointer;
  transition: 0.3s;
}

li:hover {
  background: #374151;
}

.menu-title {
  margin-top: 24px;
  font-weight: bold;
  color: #8dc52b;
}

.toggle-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.arrow {
  font-size: 12px;
  transition: transform 0.3s ease;
}

.rotate {
  transform: rotate(180deg);
}

.sub-item {
  margin-left: 10px;
  background: #111827;
}
</style>
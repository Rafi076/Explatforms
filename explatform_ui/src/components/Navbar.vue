<template>
  <nav class="top-navbar">
    <div class="nav-left">
      <img src="../assets/logo1.png" alt="Logo" class="logo" />
    </div>

    <div class="nav-right">
      <button class="nav-btn">Profile</button>

      <!-- Dark / Light Mode Button -->
      <button class="nav-btn" @click="toggleMode">
        {{ darkMode ? "☀ Light Mode" : "🌙 Dark Mode" }}
      </button>

      <button class="nav-btn logout-btn" @click="$router.push('/login')">
        Logout
      </button>
    </div>
  </nav>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Navbar",

  data() {
    return {
      darkMode: false,
    };
  },

  mounted() {
  const savedMode = localStorage.getItem("theme");

  document.body.classList.remove("dark-mode");

  if (savedMode === "dark") {
    this.darkMode = true;
    document.body.classList.add("dark-mode");
  }
},

  methods: {
    toggleMode() {
      this.darkMode = !this.darkMode;

      if (this.darkMode) {
        document.body.classList.add("dark-mode");
        localStorage.setItem("theme", "dark");
      } else {
        document.body.classList.remove("dark-mode");
        localStorage.setItem("theme", "light");
      }
    },
  },
};
</script>

<style scoped>
.top-navbar {
  height: 70px;
  background: #ffffff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-sizing: border-box;
}

.logo {
  height: 48px;
  object-fit: contain;
  position: relative;
  top: 20px; /* moves it down */
}

.nav-right {
  display: flex;
  gap: 12px;
}

.nav-btn {
  border: none;
  background: #f3f4f6;
  padding: 10px 16px;
  border-radius: 10px;
  cursor: pointer;
  font-size: 14px;
}

.nav-btn:hover {
  background: #e5e7eb;
}

.logout-btn {
  background: #8dc52b;
  color: white;
}

.logout-btn:hover {
  background: #7bb025;
}
</style>
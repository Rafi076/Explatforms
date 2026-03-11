<template>
    
  <div class="forgot-page">
     <img src="../assets/logo1.png" class="logo" alt="logo" />
    <div class="forgot-card">
      <h1>Request to Reset Password</h1>

      <label>Username</label>
      <input v-model="username" type="text" />

      <p class="optional">(Optional)</p>

      <label>Email</label>
      <input v-model="email" type="email" />

      <label>Role</label>
      <select v-model="role">
         <option>Official</option>
      <option>Admin</option>
      <option>HR</option>
      </select>

      <button class="submit-btn" @click="submitResetRequest">Submit</button>

      <p class="back-link" @click="goToLogin">Back to login page</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "ForgotPasswordView",
  data() {
    return {
      username: "",
      email: "",
      role: "Official",
    };
  },
  methods: {
    submitResetRequest() {
      if (!this.email && !this.username) {
        alert("Please enter at least username or email.");
        return;
      }

      alert("Password reset request submitted.");
      this.$router.push("/login");
    },
    goToLogin() {
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
.forgot-page {
  min-height: 100vh;
  background: #fefefe;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.forgot-card {
  width: 340px;
  background: #f7f7f7;
  border-radius: 18px;
  padding: 24px;
  box-shadow: 0 4px 18px rgba(0, 0, 0, 0.18);
}

h1 {
  font-size: 22px;
  margin: 0 0 18px 0;
  color: #111212;
  font-weight: 700;
}

label {
  display: block;
  margin-top: 18px;
  margin-bottom: 8px;
  font-size: 18px;
  color: #151414;
}

input,
select {
  width: 100%;
  height: 42px;
  border: 1px solid #23344b;
  border-radius: 10px;
  padding: 0 14px;
  font-size: 16px;
  outline: none;
  box-sizing: border-box;
  background: #fff;
}

.optional {
  margin-top: 6px;
  font-size: 16px;
  font-style: italic;
  color: #222;
}

.submit-btn {
  width: 100%;
  height: 44px;
  margin-top: 18px;
  background: #8dc52b;
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 18px;
  cursor: pointer;
}

.submit-btn:hover {
  background: #7ab01f;
}

.back-link {
  margin-top: 18px;
  text-align: center;
  color: #7caf1f;
  font-size: 16px;
  cursor: pointer;
}

.forgot-page {
  min-height: 100vh;
  background-image: url("../assets/logformbackground.png");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
}
.logo {
  width: 120px;
  margin-bottom: 20px;
}
</style>
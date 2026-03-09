<template>
  <div class="card">
    <label>Username</label>
    <input v-model="username" type="text" />

    <label>Password</label>
    <div class="password-box">
      <input v-model="password" :type="showPassword ? 'text' : 'password'" />
      <button type="button" class="eye-btn" @click="showPassword = !showPassword">
        👁
      </button>
    </div>

    <label>Role</label>
    <select v-model="role">
      <option>Official</option>
      <option>HR</option>
      <option>Admin</option>
    </select>

    <p class="terms">
      By signing in, you agree to the <span>Terms of Use</span>, and you have
      read and acknowledge the <span>Privacy Policy</span>.
    </p>

    <button class="login-btn" @click="login">Login >></button>

    <p class="forgot">Forgot password?</p>
  </div>
</template>

<script>
export default {
  name: "LoginForm",
  data() {
    return {
      username: "",
      password: "",
      role: "Instructor",
      showPassword: false,
    };
  },
  methods: {
    login() {
      if (!this.username || !this.password) {
        alert("Please enter username and password");
        return;
      }

      this.$router.push("/dashboard");
    },
  },
};
</script>

<style scoped>
.card {
  width: 430px;
  background: #f7f7f7;
  padding: 28px;
  border-radius: 18px;
  box-shadow: 0 4px 18px rgba(0, 0, 0, 0.18);
}

label {
  display: block;
  margin-bottom: 8px;
  margin-top: 18px;
  font-size: 18px;
  color: #1f2a37;
}

input,
select {
  width: 100%;
  height: 50px;
  border: 1px solid #cfd6df;
  border-radius: 12px;
  padding: 0 14px;
  font-size: 16px;
  outline: none;
  box-sizing: border-box;
}

.password-box {
  display: flex;
  align-items: center;
}

.password-box input {
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
}

.eye-btn {
  width: 58px;
  height: 50px;
  border: 1px solid #cfd6df;
  border-left: none;
  background: white;
  border-top-right-radius: 12px;
  border-bottom-right-radius: 12px;
  cursor: pointer;
}

.terms {
  margin-top: 22px;
  font-size: 15px;
  line-height: 1.6;
  color: #233142;
}

.terms span {
  color: #7caf1f;
  font-weight: 600;
}

.login-btn {
  width: 100%;
  height: 52px;
  margin-top: 22px;
  background: #8dc52b;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 18px;
  cursor: pointer;
}

.login-btn:hover {
  background: #7ab01f;
}

.forgot {
  margin-top: 22px;
  text-align: right;
  color: #7caf1f;
  font-size: 16px;
  cursor: pointer;
}
</style>
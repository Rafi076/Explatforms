<template>
  <div class="login-container">
    <img src="../assets/logo1.png" class="logo" alt="logo" />
    <LoginForm />
  </div>
</template>

<script>
import LoginForm from "../components/LoginForm.vue";

export default {
  name: "LoginView",
  components: {
    LoginForm,
  },
};
</script>

<style>
.login-container {
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
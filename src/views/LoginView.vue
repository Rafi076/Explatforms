<template>
  <div class="login-container">

    <!-- Logo -->
    <img src="../assets/logo1.png" class="logo" alt="logo" />

    <!-- Login Form -->
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

  /* Background image */
  background-image: url("../assets/logformbackground.png");

  /* Make image cover whole screen */
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

/* Logo style */
.logo {
  width: 140px;
  margin-bottom: 20px;
}
</style>
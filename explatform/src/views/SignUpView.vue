<template>
  <div class="signup-container">

    <img src="../assets/logo1.png" class="logo" />

    <SignUpForm />

  </div>
</template>

<script>
import SignUpForm from "../components/SignUpForm.vue";

export default {
  components: {
    SignUpForm
  }
};
</script>

<style>
.signup-container{
  min-height:100vh;
  background-image:url("../assets/logformbackground.png");
  background-size:cover;
  background-position:center;

  display:flex;
  flex-direction:column;
  justify-content:center;
  align-items:center;
}

.logo{
  width:120px;
  margin-bottom:20px;
}
</style>
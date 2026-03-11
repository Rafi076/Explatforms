<template>
<div class="card">

<h2>Create Account</h2>

<label>Full Name</label>
<input v-model="name" type="text">

<label>Email</label>
<input v-model="email" type="email">

<label>Username</label>
<input v-model="username" type="text">

<label>Password</label>
<input v-model="password" type="password">

<label>Confirm Password</label>
<input v-model="confirmPassword" type="password">

<label>Role</label>
<select v-model="role">
<option>Official</option>
      <option>Admin</option>
      <option>HR</option>
</select>

<button class="signup-btn" @click="register">Sign Up</button>

<p class="login-link" @click="goToLogin">
Already have account? Login
</p>

</div>
</template>

<script>
export default {
data(){
return{
name:"",
email:"",
username:"",
password:"",
confirmPassword:"",
role:"Instructor"
}
},
methods:{
register(){

if(!this.name || !this.email || !this.username || !this.password){
alert("Please fill all fields")
return
}

if(this.password !== this.confirmPassword){
alert("Passwords do not match")
return
}

alert("Account created successfully")

this.$router.push("/login")
},

goToLogin(){
this.$router.push("/login")
}
}
}
</script>

<style scoped>

.card{
width:420px;
background:#f7f7f7;
padding:28px;
border-radius:18px;
box-shadow:0 4px 18px rgba(0,0,0,0.2);
}

h2{
margin-bottom:15px;
}

label{
display:block;
margin-top:15px;
}

input,select{
width:100%;
height:45px;
border:1px solid #ccc;
border-radius:10px;
padding:0 10px;
margin-top:5px;
}

.signup-btn{
width:100%;
margin-top:20px;
height:45px;
background:#8dc52b;
border:none;
border-radius:10px;
color:white;
font-size:16px;
cursor:pointer;
}

.login-link{
margin-top:18px;
text-align:center;
color:#7caf1f;
cursor:pointer;
}

</style>
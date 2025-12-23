<template>
  <div class="welcome-container">
    <div class="glass-card welcome-card">
      <div class="content-wrapper">
        <h1 class="title">
          <span class="text-slide">CET-6</span>
          <span class="text-slide delay-1">Vocab</span>
        </h1>
        <p class="subtitle typing-effect">
          Your Glassmorphism Memory Assistant
        </p>

        <div class="form-container">
          <div class="input-group">
            <input
              type="text"
              v-model="username"
              placeholder="Username"
              class="glass-input"
              :class="{ 'input-error': errors.username }"
              @focus="clearError('username')"
            />
          </div>
          <div class="input-group">
            <input
              type="password"
              v-model="password"
              placeholder="Password"
              class="glass-input"
              :class="{ 'input-error': errors.password }"
              @focus="clearError('password')"
              @keyup.enter="handleSubmit"
            />
          </div>

          <div class="error-msg" v-if="errorMessage">{{ errorMessage }}</div>

          <button class="btn-enter" @click="handleSubmit">
            <span>{{ isRegistering ? "Register" : "Login" }}</span>
            <div class="liquid"></div>
          </button>

          <p class="switch-mode">
            {{ isRegistering ? "Already have an account?" : "New here?" }}
            <span @click="toggleMode">{{
              isRegistering ? "Login" : "Create Account"
            }}</span>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

const router = useRouter();
const username = ref("");
const password = ref("");
const isRegistering = ref(false);
const errorMessage = ref("");
const errors = reactive({ username: false, password: false });

const toggleMode = () => {
  isRegistering.value = !isRegistering.value;
  errorMessage.value = "";
  errors.username = false;
  errors.password = false;
  password.value = "";
};

const clearError = (field) => {
  errors[field] = false;
  errorMessage.value = "";
};

const handleSubmit = () => {
  if (!username.value.trim()) {
    errors.username = true;
    errorMessage.value = "Username is required";
    shakeCard();
    return;
  }
  if (!password.value.trim()) {
    errors.password = true;
    errorMessage.value = "Password is required";
    shakeCard();
    return;
  }

  if (isRegistering.value) {
    handleRegister();
  } else {
    handleLogin();
  }
};

const handleLogin = async () => {
  try {
    const response = await axios.post("http://localhost:8080/user/login", {
      username: username.value,
      password: password.value,
    });

    if (response.data.success) {
      proceedToHome(response.data.user);
    } else {
      errorMessage.value = response.data.message || "Login failed";
      errors.password = true;
      shakeCard();
    }
  } catch (error) {
    console.error("Login error:", error);
    errorMessage.value = "Network error or server offline";
    shakeCard();
  }
};

const handleRegister = async () => {
  try {
    const response = await axios.post("http://localhost:8080/user/register", {
      username: username.value,
      password: password.value,
    });

    if (response.data.success) {
      // Auto login after register to get user ID
      handleLogin();
    } else {
      errorMessage.value = response.data.message || "Registration failed";
      errors.username = true;
      shakeCard();
    }
  } catch (error) {
    console.error("Register error:", error);
    errorMessage.value = "Network error or server offline";
    shakeCard();
  }
};

const shakeCard = () => {
  const card = document.querySelector(".welcome-card");
  card.classList.remove("shake-card");
  void card.offsetWidth; // trigger reflow
  card.classList.add("shake-card");
};

const proceedToHome = (user) => {
  if (user) {
    localStorage.setItem("user", JSON.stringify(user));

    const card = document.querySelector(".welcome-card");
    card.style.transform = "scale(1.1)";
    card.style.opacity = "0";

    setTimeout(() => {
      router.push("/home");
    }, 500);
  } else {
    errorMessage.value = "Login successful but user data missing";
    shakeCard();
  }
};
</script>

<style scoped>
.welcome-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}

.welcome-card {
  padding: 60px 80px;
  text-align: center;
  width: 500px;
  transition: all 0.5s ease;
  animation: cardFloat 6s ease-in-out infinite;
  --warm-accent: #ff8c69;
  --warm-text: #5d4037;
}

@keyframes cardFloat {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.title {
  font-size: 4rem;
  font-weight: 800;
  margin: 0 0 20px 0;
  color: var(--warm-text);
  display: flex;
  justify-content: center;
  gap: 15px;
}

.text-slide {
  display: inline-block;
  opacity: 0;
  transform: translateY(20px);
  animation: slideUp 0.8s cubic-bezier(0.2, 0.8, 0.2, 1) forwards;
}

.delay-1 {
  animation-delay: 0.2s;
  color: var(--warm-accent);
}

@keyframes slideUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.subtitle {
  font-size: 1.2rem;
  color: #8d6e63;
  margin-bottom: 40px;
  white-space: nowrap;
  overflow: hidden;
  border-right: 3px solid var(--warm-accent);
  width: 0;
  animation: typing 3s steps(30, end) forwards, blink 0.75s step-end infinite;
  margin-left: auto;
  margin-right: auto;
}

@keyframes typing {
  from {
    width: 0;
  }
  to {
    width: 100%;
  }
}

@keyframes blink {
  from,
  to {
    border-color: transparent;
  }
  50% {
    border-color: var(--warm-accent);
  }
}

.form-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.input-group {
  width: 100%;
}

.glass-input {
  width: 100%;
  padding: 16px;
  border-radius: 30px;
  border: 2px solid rgba(255, 255, 255, 0.5);
  background: rgba(255, 255, 255, 0.3);
  color: var(--warm-text);
  font-size: 1.1rem;
  text-align: center;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.glass-input:focus {
  background: rgba(255, 255, 255, 0.6);
  border-color: var(--warm-accent);
  box-shadow: 0 0 20px rgba(255, 140, 105, 0.3);
}

.input-error {
  border-color: #e74c3c !important;
  background: rgba(231, 76, 60, 0.1) !important;
  animation: shake 0.5s;
}

.error-msg {
  color: #e74c3c;
  font-size: 0.9rem;
  font-weight: bold;
  min-height: 20px;
}

.shake-card {
  animation: shake 0.5s cubic-bezier(0.36, 0.07, 0.19, 0.97) both;
}

@keyframes shake {
  10%,
  90% {
    transform: translate3d(-1px, 0, 0);
  }
  20%,
  80% {
    transform: translate3d(2px, 0, 0);
  }
  30%,
  50%,
  70% {
    transform: translate3d(-4px, 0, 0);
  }
  40%,
  60% {
    transform: translate3d(4px, 0, 0);
  }
}

.btn-enter {
  position: relative;
  padding: 16px 0;
  width: 100%;
  border-radius: 35px;
  background: var(--warm-accent);
  color: white;
  font-size: 1.2rem;
  font-weight: bold;
  overflow: hidden;
  transition: all 0.3s ease;
  z-index: 1;
  box-shadow: 0 8px 20px rgba(255, 140, 105, 0.4);
}

.btn-enter:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 25px rgba(255, 140, 105, 0.5);
}

.switch-mode {
  margin-top: 10px;
  color: var(--warm-text);
  font-size: 0.95rem;
}

.switch-mode span {
  color: var(--warm-accent);
  font-weight: bold;
  cursor: pointer;
  margin-left: 5px;
  text-decoration: underline;
  transition: color 0.3s;
}

.switch-mode span:hover {
  color: #e67e22;
}
</style>

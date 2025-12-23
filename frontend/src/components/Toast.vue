<template>
  <transition name="toast">
    <div
      v-if="visible"
      class="toast-container glass-card"
      :class="{ 'toast-error': type === 'error' }"
    >
      <span class="toast-message">{{ message }}</span>
    </div>
  </transition>
</template>

<script setup>
import { ref } from "vue";

const visible = ref(false);
const message = ref("");
const type = ref("info");

const show = (msg, t = "info") => {
  message.value = msg;
  type.value = t;
  visible.value = true;
  setTimeout(() => {
    visible.value = false;
  }, 3000);
};

defineExpose({ show });
</script>

<style scoped>
.toast-container {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  padding: 12px 24px;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(255, 255, 255, 0.8);
}

.toast-error {
  border-left: 4px solid var(--danger-color);
  color: var(--danger-color);
  font-weight: bold;
}

.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}

.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translate(-50%, -20px);
}
</style>

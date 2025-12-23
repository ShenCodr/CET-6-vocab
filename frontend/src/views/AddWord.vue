<template>
  <div class="add-container">
    <div class="form-wrapper glass-card">
      <h2>添加新单词</h2>

      <div class="form-group">
        <label>单词拼写</label>
        <input
          v-model="form.spelling"
          type="text"
          placeholder="例如: ephemeral"
          :class="{ 'error-border': errors.spelling }"
        />
      </div>

      <div class="form-group">
        <label>中文释义</label>
        <input
          v-model="form.meaning"
          type="text"
          placeholder="例如: 短暂的"
          :class="{ 'error-border': errors.meaning }"
        />
      </div>

      <div class="form-group">
        <label>例句 (选填)</label>
        <textarea
          v-model="form.exampleSentence"
          rows="3"
          placeholder="例如: Fashion is ephemeral."
        ></textarea>
      </div>

      <div class="form-actions">
        <button class="btn-cancel" @click="$router.push('/home')">取消</button>
        <button class="btn-primary" @click="saveWord">保存</button>
      </div>
    </div>

    <Toast ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import Toast from "../components/Toast.vue";

const router = useRouter();
const toastRef = ref(null);

const form = reactive({
  spelling: "",
  meaning: "",
  exampleSentence: "",
  status: 0,
});

const errors = reactive({
  spelling: false,
  meaning: false,
});

const validate = () => {
  let isValid = true;
  errors.spelling = false;
  errors.meaning = false;

  if (!form.spelling.trim()) {
    errors.spelling = true;
    isValid = false;
  } else if (!/^[a-zA-Z\s-]+$/.test(form.spelling.trim())) {
    errors.spelling = true;
    toastRef.value.show("单词只能包含字母哦，不能填数字！(>_<)", "error");
    return false;
  }

  if (!form.meaning.trim()) {
    errors.meaning = true;
    isValid = false;
  }

  if (!isValid) {
    toastRef.value.show("同学，单词和释义不能为空哦！(>_<)", "error");
  }

  return isValid;
};

const saveWord = async () => {
  if (!validate()) return;

  const storedUser = localStorage.getItem("user");
  if (!storedUser) {
    router.push("/");
    return;
  }
  const user = JSON.parse(storedUser);

  try {
    await axios.post("/api/words", { ...form, userId: user.id });
    router.push("/home");
  } catch (error) {
    console.error("Failed to save word:", error);
    toastRef.value.show("保存失败，请稍后再试", "error");
  }
};
</script>

<style scoped>
.add-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px;
}

.form-wrapper {
  width: 100%;
  max-width: 500px;
  padding: 40px;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: var(--text-primary);
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  color: var(--text-secondary);
  font-weight: 500;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 30px;
}

.btn-cancel {
  background: transparent;
  color: var(--text-secondary);
  padding: 12px 24px;
}

.btn-cancel:hover {
  color: var(--text-primary);
}

.error-border {
  border-color: var(--danger-color) !important;
  box-shadow: 0 0 0 3px rgba(231, 76, 60, 0.2) !important;
}
</style>

<template>
  <div class="home-container">
    <nav class="glass-nav glass-card">
      <div class="nav-content">
        <div class="title-section">
          <h1>CET-6 单词本</h1>
          <span class="subtitle" v-if="user">Hi, {{ user.username }}! 👋</span>
          <span class="subtitle" v-else>点击卡片查看例句详情</span>
        </div>
        <div class="search-box">
          <input
            type="text"
            v-model="searchQuery"
            placeholder="搜索单词..."
            class="search-input"
          />
        </div>
        <div class="nav-actions">
          <router-link to="/add" class="btn-add glass-card" title="添加新单词"
            >+</router-link
          >
          <button
            @click="logout"
            class="btn-logout glass-card"
            title="退出登录"
          >
            🚪
          </button>
        </div>
      </div>
    </nav>

    <main class="word-grid" v-if="filteredWords.length > 0">
      <WordCard
        v-for="word in filteredWords"
        :key="word.id"
        :word="word"
        @toggle-status="toggleStatus"
        @delete-word="deleteWord"
      />
    </main>

    <div v-else-if="searchQuery" class="no-results glass-card">
      <div class="empty-state">
        <span class="empty-icon">🔍</span>
        <p>哎呀，找不到 "{{ searchQuery }}" 这个单词呢...</p>
        <p class="empty-hint">要不点击右上角添加一个？</p>
      </div>
    </div>

    <Toast ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import WordCard from "../components/WordCard.vue";
import Toast from "../components/Toast.vue";

const router = useRouter();
const words = ref([]);
const searchQuery = ref("");
const toastRef = ref(null);
const user = ref(null);

onMounted(() => {
  checkAuth();
});

const checkAuth = () => {
  try {
    const storedUser = localStorage.getItem("user");
    console.log("Checking auth, storedUser:", storedUser);

    if (storedUser && storedUser !== "undefined" && storedUser !== "null") {
      user.value = JSON.parse(storedUser);
      if (!user.value || !user.value.id) {
        console.warn("User object invalid, logging out");
        logout();
        return;
      }
      fetchWords();
    } else {
      console.warn("No user found in localStorage, logging out");
      logout();
    }
  } catch (e) {
    console.error("Auth error:", e);
    logout();
  }
};

const fetchWords = async () => {
  if (!user.value) return;
  try {
    console.log("Fetching words for user:", user.value.id);
    const response = await axios.get("/api/words", {
      params: { userId: user.value.id },
    });
    console.log("Words fetched:", response.data.length);
    words.value = response.data;
  } catch (error) {
    console.error("Failed to fetch words:", error);
    if (toastRef.value) toastRef.value.show("加载数据失败", "error");
  }
};

const logout = () => {
  localStorage.removeItem("user");
  router.push("/");
};

const toggleStatus = async (word) => {
  try {
    const newStatus = word.status === 1 ? 0 : 1;
    await axios.put(`/api/words/${word.id}`, { ...word, status: newStatus });
    word.status = newStatus;
  } catch (error) {
    console.error("Failed to update status:", error);
    if (toastRef.value) toastRef.value.show("更新状态失败", "error");
  }
};

const deleteWord = async (word) => {
  if (!confirm(`确定要删除单词 "${word.spelling}" 吗？`)) return;

  try {
    await axios.delete(`/api/words/${word.id}`);
    // Remove from local list
    words.value = words.value.filter((w) => w.id !== word.id);
    if (toastRef.value) toastRef.value.show("删除成功", "info");
  } catch (error) {
    console.error("Failed to delete word:", error);
    if (toastRef.value) toastRef.value.show("删除失败，请稍后重试", "error");
  }
};

const filteredWords = computed(() => {
  if (!searchQuery.value) return words.value;
  const query = searchQuery.value.toLowerCase();
  return words.value.filter(
    (word) =>
      word.spelling.toLowerCase().includes(query) ||
      word.meaning.includes(query)
  );
});
</script>

<style scoped>
.home-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.glass-nav {
  position: sticky;
  top: 20px;
  z-index: 100;
  margin-bottom: 40px;
  padding: 15px 30px;
}

.nav-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-section {
  display: flex;
  flex-direction: column;
}

h1 {
  margin: 0;
  font-size: 1.5rem;
  color: var(--text-primary);
}

.subtitle {
  font-size: 0.8rem;
  color: var(--text-secondary);
  margin-top: 4px;
}

.search-box {
  flex: 1;
  max-width: 400px;
  margin: 0 20px;
}

.search-input {
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.4);
  box-shadow: inset 2px 2px 5px rgba(0, 0, 0, 0.05);
}

.nav-actions {
  display: flex;
  gap: 15px;
}

.btn-add,
.btn-logout {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  text-decoration: none;
  color: var(--accent-color);
  font-size: 24px;
  font-weight: bold;
  transition: all 0.3s ease;
  border: none;
  cursor: pointer;
}

.btn-add:hover,
.btn-logout:hover {
  transform: scale(1.1);
  background: rgba(255, 255, 255, 0.6);
}

.word-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 25px;
  padding-bottom: 40px;
  align-items: start;
}

.no-results {
  padding: 40px;
  text-align: center;
  margin-top: 40px;
  animation: fadeIn 0.5s ease;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 10px;
}

.empty-hint {
  color: var(--text-secondary);
  font-size: 0.9rem;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>

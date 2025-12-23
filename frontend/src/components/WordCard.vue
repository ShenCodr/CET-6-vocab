<template>
  <div
    class="word-card glass-card"
    :class="{ mastered: word.status === 1, expanded: isExpanded }"
    @click="toggleExpand"
  >
    <div class="card-header">
      <div class="card-content">
        <h3 class="word-spelling">{{ word.spelling }}</h3>
        <div class="word-meaning-container">
          <p class="word-meaning">{{ word.meaning }}</p>
        </div>
      </div>
      <button
        class="btn-check"
        @click.stop="$emit('toggle-status', word)"
        title="标记为已掌握"
      >
        <span v-if="word.status === 1">✅</span>
        <span v-else>⬜</span>
      </button>
    </div>

    <div class="card-details-wrapper" :class="{ expanded: isExpanded }">
      <div class="card-details-inner" @click.stop>
        <div class="divider"></div>
        <div class="detail-item">
          <p class="detail-label">📝 例句：</p>
          <p class="detail-text">{{ word.exampleSentence || "暂无例句" }}</p>
        </div>
        <div class="detail-item">
          <p class="detail-label">📅 创建时间：</p>
          <p class="detail-text">{{ formatDate(word.createTime) }}</p>
        </div>

        <div class="card-actions">
          <button class="btn-delete" @click="$emit('delete-word', word)">
            🗑️ 删除单词
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

defineProps({
  word: {
    type: Object,
    required: true,
  },
});

defineEmits(["toggle-status", "delete-word"]);

const isExpanded = ref(false);

const toggleExpand = () => {
  isExpanded.value = !isExpanded.value;
};

const formatDate = (dateStr) => {
  if (!dateStr) return "";
  const date = new Date(dateStr);
  return (
    date.toLocaleDateString() + " " + date.toLocaleTimeString().slice(0, 5)
  );
};
</script>

<style scoped>
.word-card {
  padding: 20px;
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  cursor: pointer;
}

.word-card:hover {
  transform: translateY(-5px);
  background: rgba(255, 255, 255, 0.35);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.card-content {
  flex: 1;
}

.word-spelling {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
}

.word-meaning-container {
  margin-top: 8px;
}

.word-meaning {
  margin: 0;
  color: var(--text-secondary);
  font-size: 1rem;
}

.btn-check {
  background: transparent;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  padding: 5px;
  border-radius: 50%;
  transition: transform 0.2s ease;
  margin-left: 10px;
}

.btn-check:hover {
  transform: scale(1.2);
}

.mastered {
  opacity: 0.7;
  background: rgba(200, 255, 200, 0.2);
  border-color: rgba(100, 200, 100, 0.3);
}

.mastered .word-spelling {
  text-decoration: line-through;
  color: var(--text-secondary);
}

/* Details Section - Optimized Animation */
.card-details-wrapper {
  display: grid;
  grid-template-rows: 0fr;
  transition: grid-template-rows 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.card-details-wrapper.expanded {
  grid-template-rows: 1fr;
}

.card-details-inner {
  overflow: hidden;
  opacity: 0;
  transform: translateY(-10px);
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.card-details-wrapper.expanded .card-details-inner {
  opacity: 1;
  transform: translateY(0);
  padding-top: 15px;
}

.divider {
  height: 1px;
  background: rgba(0, 0, 0, 0.05);
  margin-bottom: 15px;
}

.detail-item {
  margin-bottom: 12px;
}

.detail-label {
  font-size: 0.85rem;
  color: var(--text-secondary);
  margin: 0 0 4px 0;
  font-weight: bold;
}

.detail-text {
  margin: 0;
  color: var(--text-primary);
  line-height: 1.5;
  font-size: 0.95rem;
}

.card-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 15px;
}

.btn-delete {
  background: rgba(231, 76, 60, 0.1);
  color: var(--danger-color);
  border: 1px solid rgba(231, 76, 60, 0.2);
  padding: 6px 12px;
  border-radius: 8px;
  font-size: 0.85rem;
  transition: all 0.2s;
}

.btn-delete:hover {
  background: var(--danger-color);
  color: white;
}

/* Transition */
.slide-fade-enter-active {
  transition: all 0.3s ease-out;
}

.slide-fade-leave-active {
  transition: all 0.2s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateY(-10px);
  opacity: 0;
  max-height: 0;
}
</style>

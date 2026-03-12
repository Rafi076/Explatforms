<template>
  <div class="pagination">
    <button
      class="page-btn"
      :disabled="currentPage === 1"
      @click="$emit('page-change', currentPage - 1)"
    >
      Prev
    </button>

    <button
      v-for="page in totalPages"
      :key="page"
      class="page-btn"
      :class="{ active: page === currentPage }"
      @click="$emit('page-change', page)"
    >
      {{ page }}
    </button>

    <button
      class="page-btn"
      :disabled="currentPage === totalPages || totalPages === 0"
      @click="$emit('page-change', currentPage + 1)"
    >
      Next
    </button>
  </div>
</template>

<script>
export default {
  name: "AppPagination",
  props: {
    currentPage: {
      type: Number,
      required: true,
    },
    totalPages: {
      type: Number,
      required: true,
    },
  },
};
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 8px;
  margin-top: 18px;
  flex-wrap: wrap;
}

.page-btn {
  min-width: 38px;
  height: 38px;
  padding: 0 12px;
  border: none;
  border-radius: 8px;
  background: #f3f4f6;
  color: #111827;
  cursor: pointer;
  font-size: 14px;
}

.page-btn:hover:not(:disabled) {
  background: #e5e7eb;
}

.page-btn.active {
  background: #8dc52b;
  color: white;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
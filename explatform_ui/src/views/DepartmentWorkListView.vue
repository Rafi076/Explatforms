<template>
  <div class="work-page">
    <div class="page-header">
  <button class="back-btn" @click="goBack">
    ← Back
  </button>

  <h2 class="page-title">
    Department Work List - {{ departmentId }}
  </h2>
</div>

    <div class="top-bar">
      <div class="search-group">
        <input
          v-model="searchProcessCode"
          type="text"
          placeholder="Search by Process Code"
          class="search-input"
        />

        <input
          v-model="searchStartDate"
          type="date"
          class="search-input"
        />

        <input
          v-model="searchEndDate"
          type="date"
          class="search-input"
        />

        <button class="action-btn add-btn" @click="handleAdd">Add</button>
        <button class="action-btn search-btn" @click="handleSearch">Search</button>

        <button
          v-if="!isEditing"
          class="action-btn edit-btn"
          @click="handleEdit"
        >
          Edit
        </button>

        <button
          v-if="isEditing"
          class="action-btn save-btn"
          @click="handleSave"
        >
          Save
        </button>

        <button
          v-if="isEditing"
          class="action-btn cancel-btn"
          @click="handleCancelEdit"
        >
          Cancel
        </button>

        <button class="action-btn delete-btn" @click="handleDelete">Delete</button>
        <button class="action-btn reset-btn" @click="handleReset">Reset</button>
      </div>
    </div>

    <div class="table-wrapper">
      <table class="department-work-table">
        <thead>
          <tr>
            <th class="checkbox-col">
              <input
                type="checkbox"
                :checked="isAllCurrentPageSelected"
                @change="toggleSelectAllCurrentPage"
              />
            </th>
            <th>On Process Work</th>
            <th>Process Code</th>
            <th>Buyer</th>
            <th>Purchase Date</th>
            <th>Delivery Date</th>
            <th>Quantity</th>
            <th>Details</th>
            <th>Note</th>
          </tr>
        </thead>

        <tbody>
          <tr v-if="isAdding" class="editing-row">
            <td class="checkbox-col"></td>
            <td><input v-model="editForm.onProcessWork" class="table-edit-input" /></td>
            <td><input v-model="editForm.processCode" class="table-edit-input" /></td>
            <td><input v-model="editForm.buyer" class="table-edit-input" /></td>
            <td><input v-model="editForm.purchaseDate" type="date" class="table-edit-input" /></td>
            <td><input v-model="editForm.deliveryDate" type="date" class="table-edit-input" /></td>
            <td><input v-model="editForm.quantity" class="table-edit-input" /></td>
            <td><input v-model="editForm.details" class="table-edit-input" /></td>
            <td><input v-model="editForm.note" class="table-edit-input" /></td>
          </tr>

          <tr
            v-for="item in paginatedWorks"
            :key="item.id"
            :class="{ 'editing-row': editingWorkId === item.id }"
          >
            <td class="checkbox-col">
              <input
                type="checkbox"
                :value="item.id"
                v-model="selectedWorkIds"
                :disabled="isEditing && editingWorkId !== item.id"
              />
            </td>

            <template v-if="editingWorkId === item.id">
              <td><input v-model="editForm.onProcessWork" class="table-edit-input" /></td>
              <td><input v-model="editForm.processCode" class="table-edit-input" /></td>
              <td><input v-model="editForm.buyer" class="table-edit-input" /></td>
              <td><input v-model="editForm.purchaseDate" type="date" class="table-edit-input" /></td>
              <td><input v-model="editForm.deliveryDate" type="date" class="table-edit-input" /></td>
              <td><input v-model="editForm.quantity" class="table-edit-input" /></td>
              <td><input v-model="editForm.details" class="table-edit-input" /></td>
              <td><input v-model="editForm.note" class="table-edit-input" /></td>
            </template>

            <template v-else>
              <td>{{ item.onProcessWork }}</td>
              <td>{{ item.processCode }}</td>
              <td>{{ item.buyer }}</td>
              <td>{{ item.purchaseDate }}</td>
              <td>{{ item.deliveryDate }}</td>
              <td>{{ item.quantity }}</td>
              <td>{{ item.details }}</td>
              <td>{{ item.note }}</td>
            </template>
          </tr>

          <tr v-if="!isAdding && paginatedWorks.length === 0">
            <td colspan="9" class="no-data">No work found</td>
          </tr>
        </tbody>
      </table>
    </div>

    <AppPagination
      :currentPage="currentPage"
      :totalPages="totalPages"
      @page-change="changePage"
    />
  </div>
</template>

<script>
import AppPagination from "../components/AppPagination.vue";

const API_BASE = "http://localhost:8080/api";

export default {
  name: "DepartmentWorkListView",
  components: {
    AppPagination,
  },

  data() {
    return {
      searchProcessCode: "",
      searchStartDate: "",
      searchEndDate: "",
      currentPage: 1,
      itemsPerPage: 5,
      selectedWorkIds: [],
      editingWorkId: null,
      isAdding: false,

      editForm: {
        onProcessWork: "",
        processCode: "",
        buyer: "",
        purchaseDate: "",
        deliveryDate: "",
        quantity: "",
        details: "",
        note: "",
      },

      works: [],
    };
  },

  computed: {
    departmentId() {
      return this.$route.params.departmentId;
    },

    filteredWorks() {
      return this.works
        .filter((item) => item.departmentId === this.departmentId)
        .filter((item) => {
          const matchProcessCode = String(item.processCode || "")
            .toLowerCase()
            .includes(this.searchProcessCode.toLowerCase());

          const matchStartDate =
            !this.searchStartDate ||
            new Date(item.purchaseDate) >= new Date(this.searchStartDate);

          const matchEndDate =
            !this.searchEndDate ||
            new Date(item.purchaseDate) <= new Date(this.searchEndDate);

          return matchProcessCode && matchStartDate && matchEndDate;
        });
    },

    totalPages() {
      return Math.ceil(this.filteredWorks.length / this.itemsPerPage) || 1;
    },

    paginatedWorks() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredWorks.slice(start, end);
    },

    isAllCurrentPageSelected() {
      if (this.paginatedWorks.length === 0 || this.isEditing) return false;

      return this.paginatedWorks.every((item) =>
        this.selectedWorkIds.includes(item.id)
      );
    },

    isEditing() {
      return this.editingWorkId !== null || this.isAdding;
    },
  },

  watch: {
    searchProcessCode() {
      this.currentPage = 1;
    },
    searchStartDate() {
      this.currentPage = 1;
    },
    searchEndDate() {
      this.currentPage = 1;
    },
  },

  mounted() {
    this.fetchWorks();
  },

  methods: {
    // ✅ FETCH DATA
    fetchWorks() {
      fetch(`${API_BASE}/department-works/${this.departmentId}`)
        .then((res) => {
          if (!res.ok) throw new Error("Failed to fetch works");
          return res.json();
        })
        .then((data) => {
          console.log("WORK DATA:", data); // 🔍 debug
          this.works = data;
        })
        .catch((err) => {
          console.error("Error fetching works:", err);
          this.works = [];
        });
    },

    goBack() {
      this.$router.push("/dashboard/department-detail");
    },

    changePage(page) {
      if (page < 1 || page > this.totalPages) return;
      this.currentPage = page;
    },

    resetEditForm() {
      this.editForm = {
        onProcessWork: "",
        processCode: "",
        buyer: "",
        purchaseDate: "",
        deliveryDate: "",
        quantity: "",
        details: "",
        note: "",
      };
    },

    handleSearch() {
      this.currentPage = 1;
    },

    handleAdd() {
      if (this.isEditing) {
        alert("Please save or cancel current editing first.");
        return;
      }

      this.isAdding = true;
      this.editingWorkId = null;
      this.selectedWorkIds = [];
      this.resetEditForm();
      this.currentPage = 1;
    },

    handleEdit() {
      if (this.isAdding) {
        alert("Please save or cancel new row first.");
        return;
      }

      if (this.selectedWorkIds.length !== 1) {
        alert("Select exactly one item to edit.");
        return;
      }

      const selectedId = this.selectedWorkIds[0];
      const item = this.works.find((w) => w.id === selectedId);

      if (!item) return;

      this.editingWorkId = selectedId;
      this.editForm = { ...item };
    },

    // ✅ SAVE (ADD + UPDATE)
    handleSave() {
      // ➕ ADD
      if (this.isAdding) {
        fetch(`${API_BASE}/department-works`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            ...this.editForm,
            departmentId: this.departmentId,
          }),
        })
          .then((res) => {
            if (!res.ok) throw new Error("Add failed");
            return res.json();
          })
          .then(() => {
            this.fetchWorks();
            this.isAdding = false;
            this.resetEditForm();
          })
          .catch((err) => console.error("Add error:", err));

        return;
      }

      // ✏️ UPDATE
      if (!this.editingWorkId) return;

      fetch(`${API_BASE}/department-works/${this.editingWorkId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          ...this.editForm,
          departmentId: this.departmentId,
        }),
      })
        .then((res) => {
          if (!res.ok) throw new Error("Update failed");
          return res.json();
        })
        .then(() => {
          this.fetchWorks();
          this.editingWorkId = null;
          this.resetEditForm();
        })
        .catch((err) => console.error("Update error:", err));
    },

    handleCancelEdit() {
      this.isAdding = false;
      this.editingWorkId = null;
      this.resetEditForm();
    },

    // ✅ DELETE
    handleDelete() {
      if (this.isEditing) {
        alert("Please finish editing first.");
        return;
      }

      if (!this.selectedWorkIds.length) {
        alert("Select at least one item.");
        return;
      }

      if (!confirm("Are you sure?")) return;

      Promise.all(
        this.selectedWorkIds.map((id) =>
          fetch(`${API_BASE}/department-works/${id}`, {
            method: "DELETE",
          }).then((res) => {
            if (!res.ok) throw new Error("Delete failed");
          })
        )
      )
        .then(() => {
          this.fetchWorks();
          this.selectedWorkIds = [];
        })
        .catch((err) => console.error("Delete error:", err));
    },

    handleReset() {
      this.searchProcessCode = "";
      this.searchStartDate = "";
      this.searchEndDate = "";
      this.selectedWorkIds = [];
      this.currentPage = 1;
      this.handleCancelEdit();
    },

    toggleSelectAllCurrentPage(event) {
      if (this.isEditing) return;

      const currentIds = this.paginatedWorks.map((item) => item.id);

      if (event.target.checked) {
        this.selectedWorkIds = [
          ...new Set([...this.selectedWorkIds, ...currentIds]),
        ];
      } else {
        this.selectedWorkIds = this.selectedWorkIds.filter(
          (id) => !currentIds.includes(id)
        );
      }
    },
  },
};
</script>

<style scoped>
.work-page {
  width: 100%;
}

.page-title-row {
  margin-bottom: 16px;
}

.page-title-row h2 {
  margin: 0;
}

.top-bar {
  display: flex;
  justify-content: flex-end;
  width: 100%;
  margin-bottom: 24px;
}

.search-group {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  justify-content: flex-end;
  align-items: center;
}

.search-input {
  width: 220px;
  height: 42px;
  border: 1px solid #d1d5db;
  border-radius: 10px;
  padding: 0 14px;
  font-size: 14px;
  outline: none;
  background: #ffffff;
  box-sizing: border-box;
}

.table-wrapper {
  width: 100%;
  overflow-x: auto;
  background: #ffffff;
  border-radius: 14px;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.06);
}

.department-work-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1300px;
}

.department-work-table thead {
  background: #f3f4f6;
}

.department-work-table th,
.department-work-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #e5e7eb;
  font-size: 14px;
  vertical-align: middle;
}

.department-work-table tbody tr:hover {
  background: #f9fafb;
}

.checkbox-col {
  width: 50px;
  text-align: center !important;
}

.no-data {
  text-align: center;
  padding: 20px;
  color: #6b7280;
}

.table-edit-input {
  width: 100%;
  min-width: 120px;
  height: 36px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  padding: 0 10px;
  font-size: 14px;
  outline: none;
  box-sizing: border-box;
}

.editing-row {
  background: #fff7ed;
}

.action-btn {
  height: 42px;
  border: none;
  border-radius: 10px;
  padding: 0 18px;
  font-size: 14px;
  cursor: pointer;
  color: white;
}

.add-btn {
  background: #10b981;
}

.search-btn {
  background: #3b82f6;
}

.edit-btn {
  background: #f59e0b;
}

.save-btn {
  background: #10b981;
}

.cancel-btn {
  background: #6b7280;
}

.delete-btn {
  background: #ef4444;
}

.reset-btn {
  background: #6b7280;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 20px;
}

.page-title {
  margin: 0;
}

.back-btn {
  border: none;
  background: #3b82f6;
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
}

.back-btn:hover {
  background: #2563eb;
}
</style>
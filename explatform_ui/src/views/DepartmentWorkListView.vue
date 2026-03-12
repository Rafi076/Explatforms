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
      works: [
        {
          id: 1,
          departmentId: "DEP001",
          onProcessWork: "Fabric Purchase",
          processCode: "P-1001",
          buyer: "John Buyer",
          purchaseDate: "2024-01-05",
          deliveryDate: "2024-01-15",
          quantity: "500",
          details: "Cotton fabric purchase",
          note: "Urgent",
        },
        {
          id: 2,
          departmentId: "DEP001",
          onProcessWork: "Dyeing",
          processCode: "P-1002",
          buyer: "Sarah Buyer",
          purchaseDate: "2024-01-10",
          deliveryDate: "2024-01-20",
          quantity: "300",
          details: "Blue color dyeing",
          note: "Priority",
        },
        {
          id: 3,
          departmentId: "DEP002",
          onProcessWork: "Packaging",
          processCode: "P-2001",
          buyer: "Michael",
          purchaseDate: "2024-02-01",
          deliveryDate: "2024-02-12",
          quantity: "700",
          details: "Final packaging",
          note: "Normal",
        },
      ],
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
          const matchProcessCode = item.processCode
            .toLowerCase()
            .includes(this.searchProcessCode.toLowerCase());

          const matchStartDate =
            !this.searchStartDate || item.purchaseDate >= this.searchStartDate;
          const matchEndDate =
            !this.searchEndDate || item.purchaseDate <= this.searchEndDate;

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
  methods: {
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
        alert("Please save or cancel the current editing first.");
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
        alert("Please save or cancel the new row first.");
        return;
      }

      if (this.selectedWorkIds.length === 0) {
        alert("Please select one work item to edit.");
        return;
      }

      if (this.selectedWorkIds.length > 1) {
        alert("Please select only one work item to edit.");
        return;
      }

      const selectedId = this.selectedWorkIds[0];
      const item = this.works.find((w) => w.id === selectedId);

      if (!item) return;

      this.editingWorkId = selectedId;
      this.editForm = {
        onProcessWork: item.onProcessWork,
        processCode: item.processCode,
        buyer: item.buyer,
        purchaseDate: item.purchaseDate,
        deliveryDate: item.deliveryDate,
        quantity: item.quantity,
        details: item.details,
        note: item.note,
      };
    },

    handleSave() {
      if (this.isAdding) {
        if (
          !this.editForm.onProcessWork ||
          !this.editForm.processCode ||
          !this.editForm.buyer ||
          !this.editForm.purchaseDate ||
          !this.editForm.deliveryDate ||
          !this.editForm.quantity
        ) {
          alert("Please fill required fields before saving.");
          return;
        }

        const newItem = {
          id: Date.now(),
          departmentId: this.departmentId,
          onProcessWork: this.editForm.onProcessWork,
          processCode: this.editForm.processCode,
          buyer: this.editForm.buyer,
          purchaseDate: this.editForm.purchaseDate,
          deliveryDate: this.editForm.deliveryDate,
          quantity: this.editForm.quantity,
          details: this.editForm.details,
          note: this.editForm.note,
        };

        this.works.unshift(newItem);
        this.isAdding = false;
        this.resetEditForm();
        return;
      }

      if (!this.editingWorkId) return;

      const index = this.works.findIndex((w) => w.id === this.editingWorkId);
      if (index === -1) return;

      this.works[index] = {
        ...this.works[index],
        onProcessWork: this.editForm.onProcessWork,
        processCode: this.editForm.processCode,
        buyer: this.editForm.buyer,
        purchaseDate: this.editForm.purchaseDate,
        deliveryDate: this.editForm.deliveryDate,
        quantity: this.editForm.quantity,
        details: this.editForm.details,
        note: this.editForm.note,
      };

      this.works = [...this.works];
      this.editingWorkId = null;
      this.resetEditForm();
    },

    handleCancelEdit() {
      this.isAdding = false;
      this.editingWorkId = null;
      this.resetEditForm();
    },

    handleDelete() {
      if (this.isEditing) {
        alert("Please save or cancel editing first.");
        return;
      }

      if (this.selectedWorkIds.length === 0) {
        alert("Please select at least one work item to delete.");
        return;
      }

      const confirmed = window.confirm(
        "Are you sure you want to delete the selected work item(s)?"
      );
      if (!confirmed) return;

      this.works = this.works.filter((w) => !this.selectedWorkIds.includes(w.id));
      this.selectedWorkIds = [];

      if (this.currentPage > this.totalPages) {
        this.currentPage = this.totalPages;
      }
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

      const currentPageIds = this.paginatedWorks.map((item) => item.id);

      if (event.target.checked) {
        const merged = [...this.selectedWorkIds, ...currentPageIds];
        this.selectedWorkIds = [...new Set(merged)];
      } else {
        this.selectedWorkIds = this.selectedWorkIds.filter(
          (id) => !currentPageIds.includes(id)
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
<template>
  <div class="department-page">
    <div class="top-bar">
      <div class="search-group">
        <input
          v-model="searchDepartmentId"
          type="text"
          placeholder="Search by Department ID"
          class="search-input"
        />

        <input
          v-model="searchDepartmentCode"
          type="text"
          placeholder="Search by Department Code"
          class="search-input"
        />

        <input
          v-model="searchDepartmentName"
          type="text"
          placeholder="Search by Department Name"
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
      <table class="department-table">
        <thead>
          <tr>
            <th class="checkbox-col">
              <input
                type="checkbox"
                :checked="isAllCurrentPageSelected"
                @change="toggleSelectAllCurrentPage"
              />
            </th>
            <th>Department ID</th>
            <th>Department Code</th>
            <th>Department Name</th>
          </tr>
        </thead>

        <tbody>
          <!-- Add new row -->
          <tr v-if="isAdding" class="editing-row">
            <td class="checkbox-col"></td>

            <td>Auto</td>

            <td>
              <input
                v-model="editForm.departmentCode"
                type="text"
                class="table-edit-input"
                placeholder="Department Code"
              />
            </td>

            <td>
              <input
                v-model="editForm.departmentName"
                type="text"
                class="table-edit-input"
                placeholder="Department Name"
              />
            </td>
          </tr>

          <!-- Existing rows -->
          <tr
            v-for="department in paginatedDepartments"
            :key="department.departmentId"
            :class="{ 'editing-row': editingDepartmentId === department.departmentId }"
          >
            <td class="checkbox-col">
              <input
                type="checkbox"
                :value="department.departmentId"
                v-model="selectedDepartmentIds"
                :disabled="isEditing && editingDepartmentId !== department.departmentId"
              />
            </td>

            <template v-if="editingDepartmentId === department.departmentId">
              <td>{{ department.departmentId }}</td>

              <td>
                <input
                  v-model="editForm.departmentCode"
                  type="text"
                  class="table-edit-input"
                />
              </td>

              <td>
                <input
                  v-model="editForm.departmentName"
                  type="text"
                  class="table-edit-input"
                />
              </td>
            </template>

            <template v-else>
              <td>
                <span
                  class="clickable-link"
                  @click="goToDepartmentWork(department)"
                >
                  {{ department.departmentId }}
                </span>
              </td>

              <td>{{ department.departmentCode }}</td>

              <td>
                <span
                  class="clickable-link"
                  @click="goToDepartmentWork(department)"
                >
                  {{ department.departmentName }}
                </span>
              </td>
            </template>
          </tr>

          <tr v-if="!isAdding && paginatedDepartments.length === 0">
            <td colspan="4" class="no-data">No department found</td>
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
  name: "DepartmentDetailView",
  components: {
    AppPagination,
  },

  data() {
    return {
      searchDepartmentId: "",
      searchDepartmentCode: "",
      searchDepartmentName: "",
      currentPage: 1,
      itemsPerPage: 5,
      selectedDepartmentIds: [],
      editingDepartmentId: null,
      isAdding: false,

      editForm: {
        departmentCode: "",
        departmentName: "",
      },

      // ✅ data will come from backend
      departments: [],
    };
  },

mounted() {
  fetch("http://localhost:8080/departments")
    .then((res) => res.json())
    .then((data) => {
      console.log("DATA:", data); // 👈 check this
      this.departments = data;
    })
    .catch((err) => {
      console.error("Error fetching departments:", err);
    });
},

  computed: {
    filteredDepartments() {
      return this.departments.filter((department) => {
        const matchDepartmentId = String(department.departmentId)
  .toLowerCase()
          .includes(this.searchDepartmentId.toLowerCase());

        const matchDepartmentCode = department.departmentCode
          ?.toLowerCase()
          .includes(this.searchDepartmentCode.toLowerCase());

        const matchDepartmentName = department.departmentName
          ?.toLowerCase()
          .includes(this.searchDepartmentName.toLowerCase());

        return (
          matchDepartmentId &&
          matchDepartmentCode &&
          matchDepartmentName
        );
      });
    },

    totalPages() {
      return Math.ceil(this.filteredDepartments.length / this.itemsPerPage) || 1;
    },

    paginatedDepartments() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredDepartments.slice(start, end);
    },

    isAllCurrentPageSelected() {
      if (this.paginatedDepartments.length === 0 || this.isEditing) return false;

      return this.paginatedDepartments.every((department) =>
        this.selectedDepartmentIds.includes(department.departmentId)
      );
    },

    isEditing() {
      return this.editingDepartmentId !== null || this.isAdding;
    },
  },

  watch: {
    searchDepartmentId() {
      this.currentPage = 1;
    },
    searchDepartmentCode() {
      this.currentPage = 1;
    },
    searchDepartmentName() {
      this.currentPage = 1;
    },
  },

  methods: {
    changePage(page) {
      if (page < 1 || page > this.totalPages) return;
      this.currentPage = page;
    },

    resetEditForm() {
      this.editForm = {
        departmentCode: "",
        departmentName: "",
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
      this.editingDepartmentId = null;
      this.selectedDepartmentIds = [];
      this.resetEditForm();
      this.currentPage = 1;
    },

    handleEdit() {
      if (this.isAdding) {
        alert("Please save or cancel the new row first.");
        return;
      }

      if (this.selectedDepartmentIds.length !== 1) {
        alert("Please select exactly one department to edit.");
        return;
      }

      const selectedId = this.selectedDepartmentIds[0];
      const department = this.departments.find(
        (item) => item.departmentId === selectedId
      );

      if (!department) return;

      this.editingDepartmentId = selectedId;
      this.editForm = {
        departmentCode: department.departmentCode,
        departmentName: department.departmentName,
      };
    },

    handleSave() {
      // ⚠️ still frontend-only (not API yet)
      if (this.isAdding) {
        if (!this.editForm.departmentCode || !this.editForm.departmentName) {
          alert("Please fill all fields before saving.");
          return;
        }

        const nextNumber = this.departments.length + 1;
        const newDepartmentId = `DEP${String(nextNumber).padStart(3, "0")}`;

        const newDepartment = {
          departmentId: newDepartmentId,
          departmentCode: this.editForm.departmentCode,
          departmentName: this.editForm.departmentName,
        };

        this.departments.unshift(newDepartment);
        this.isAdding = false;
        this.resetEditForm();
        return;
      }

      if (!this.editingDepartmentId) return;

      const index = this.departments.findIndex(
        (d) => d.departmentId === this.editingDepartmentId
      );

      if (index === -1) return;

      this.departments[index] = {
        ...this.departments[index],
        departmentCode: this.editForm.departmentCode,
        departmentName: this.editForm.departmentName,
      };

      this.departments = [...this.departments];
      this.editingDepartmentId = null;
      this.resetEditForm();
    },

    handleCancelEdit() {
      this.isAdding = false;
      this.editingDepartmentId = null;
      this.resetEditForm();
    },

    handleDelete() {
      if (this.isEditing) {
        alert("Please save or cancel editing first.");
        return;
      }

      if (this.selectedDepartmentIds.length === 0) {
        alert("Please select at least one department to delete.");
        return;
      }

      const confirmed = window.confirm("Are you sure?");
      if (!confirmed) return;

      this.departments = this.departments.filter(
        (d) => !this.selectedDepartmentIds.includes(d.departmentId)
      );

      this.selectedDepartmentIds = [];
    },

    handleReset() {
      this.searchDepartmentId = "";
      this.searchDepartmentCode = "";
      this.searchDepartmentName = "";
      this.selectedDepartmentIds = [];
      this.currentPage = 1;
      this.handleCancelEdit();
    },

    toggleSelectAllCurrentPage(event) {
      if (this.isEditing) return;

      const currentIds = this.paginatedDepartments.map(
        (d) => d.departmentId
      );

      if (event.target.checked) {
        this.selectedDepartmentIds = [
          ...new Set([...this.selectedDepartmentIds, ...currentIds]),
        ];
      } else {
        this.selectedDepartmentIds = this.selectedDepartmentIds.filter(
          (id) => !currentIds.includes(id)
        );
      }
    },

    goToDepartmentWork(department) {
      this.$router.push(
        `/dashboard/department-work/${department.departmentId}`
      );
    },
  },
};
</script>

<style scoped>
.department-page {
  width: 100%;
}

.top-bar {
  display: flex;
  justify-content: flex-start;
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

.search-input:focus {
  border-color: #8dc52b;
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

.table-wrapper {
  width: 100%;
  overflow-x: auto;
  background: #ffffff;
  border-radius: 14px;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.06);
}

.department-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 800px;
}

.department-table thead {
  background: #f3f4f6;
}

.department-table th,
.department-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #e5e7eb;
  font-size: 14px;
  vertical-align: middle;
}

.department-table th {
  color: #111827;
  font-weight: 600;
}

.department-table td {
  color: #374151;
}

.department-table tbody tr:hover {
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

.clickable-link {
  color: #2563eb;
  cursor: pointer;
  font-weight: 600;
}

.clickable-link:hover {
  text-decoration: underline;
}
</style>
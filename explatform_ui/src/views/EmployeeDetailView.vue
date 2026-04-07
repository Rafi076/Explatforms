<template>
  <div class="employee-page">
    <div class="top-bar">
      <div class="search-group">
        <input
          v-model="searchEmployeeId"
          type="text"
          placeholder="Search by Employee ID"
          class="search-input"
        />

        <input
          v-model="searchDepartmentId"
          type="text"
          placeholder="Search by Department ID"
          class="search-input"
        />

        <input
          v-model="searchEmployeePhone"
          type="text"
          placeholder="Search by Employee Phone"
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
      <table class="employee-table">
        <thead>
          <tr>
            <th class="checkbox-col">
              <input
                type="checkbox"
                :checked="isAllCurrentPageSelected"
                @change="toggleSelectAllCurrentPage"
              />
            </th>
            <th>Employee Name</th>
            <th>Employee ID</th>
            <th>Employee Code</th>
            <th>Employee Phone</th>
            <th>Department Name</th>
            <th>Employee Join Date</th>
            <th>Employee Salary</th>
          </tr>
        </thead>

        <tbody>
          <!-- Add new row -->
          <tr v-if="isAdding" class="editing-row">
            <td class="checkbox-col"></td>

            <td>
              <input
                v-model="editForm.employeeName"
                type="text"
                class="table-edit-input"
                placeholder="Employee Name"
              />
            </td>

            <td>Auto</td>

            <td>
              <input
                v-model="editForm.employeeCode"
                type="text"
                class="table-edit-input"
                placeholder="Employee Code"
              />
            </td>

            <td>
              <input
                v-model="editForm.employeePhone"
                type="text"
                class="table-edit-input"
                placeholder="Employee Phone"
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

            <td>
              <input
                v-model="editForm.joinDate"
                type="date"
                class="table-edit-input"
              />
            </td>

            <td>
              <input
                v-model="editForm.salary"
                type="text"
                class="table-edit-input"
                placeholder="Employee Salary"
              />
            </td>
          </tr>

          <!-- Existing rows -->
          <tr
            v-for="employee in paginatedEmployees"
            :key="employee.employeeId"
            :class="{ 'editing-row': editingEmployeeId === employee.employeeId }"
          >
            <td class="checkbox-col">
              <input
                type="checkbox"
                :value="employee.employeeId"
                v-model="selectedEmployeeIds"
                :disabled="isEditing && editingEmployeeId !== employee.employeeId"
              />
            </td>

            <template v-if="editingEmployeeId === employee.employeeId">
              <td>
                <input
                  v-model="editForm.employeeName"
                  type="text"
                  class="table-edit-input"
                />
              </td>
              <td>{{ employee.employeeId }}</td>
              <td>
                <input
                  v-model="editForm.employeeCode"
                  type="text"
                  class="table-edit-input"
                />
              </td>
              <td>
                <input
                  v-model="editForm.employeePhone"
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
              <td>
                <input
                  v-model="editForm.joinDate"
                  type="date"
                  class="table-edit-input"
                />
              </td>
              <td>
                <input
                  v-model="editForm.salary"
                  type="text"
                  class="table-edit-input"
                />
              </td>
            </template>

            <template v-else>
              <td>{{ employee.employeeName }}</td>
              <td>{{ employee.employeeId }}</td>
              <td>{{ employee.employeeCode }}</td>
              <td>{{ employee.employeePhone }}</td>
              <td>{{ employee.departmentName }}</td>
<td>{{ employee.employeeJoindate }}</td>
<td>{{ employee.employeeSalary }}</td>
            </template>
          </tr>

          <tr v-if="!isAdding && paginatedEmployees.length === 0">
            <td colspan="8" class="no-data">No employee found</td>
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
  name: "EmployeeDetailView",

  components: {
    AppPagination,
  },

  data() {
    return {
      searchEmployeeId: "",
      searchDepartmentId: "",
      searchEmployeePhone: "",
      currentPage: 1,
      itemsPerPage: 5,
      selectedEmployeeIds: [],
      editingEmployeeId: null,
      isAdding: false,

      editForm: {
        employeeName: "",
        employeeCode: "",
        employeePhone: "",
        departmentName: "",
        employeeJoindate: "",   // ✅ FIXED
        employeeSalary: "",     // ✅ FIXED
      },

      employees: [], // ✅ now coming from backend
    };
  },

  // ✅ API CALL HERE
mounted() {
  fetch("/employees")  // note: no localhost:8080, just the relative path
    .then(res => res.json())
    .then(data => {
      console.log(data);
      this.employees = data;
    })
    .catch(err => console.error("Fetch error:", err));
},

  computed: {
    filteredEmployees() {
      return this.employees.filter((employee) => {
        const matchEmployeeId = employee.employeeId
          ?.toLowerCase()
          .includes(this.searchEmployeeId.toLowerCase());

        const matchDepartmentId = employee.departmentName
          ?.toLowerCase()
          .includes(this.searchDepartmentId.toLowerCase());

        const matchEmployeePhone = employee.employeePhone
          ?.toLowerCase()
          .includes(this.searchEmployeePhone.toLowerCase());

        return matchEmployeeId && matchDepartmentId && matchEmployeePhone;
      });
    },

    totalPages() {
      return Math.ceil(this.filteredEmployees.length / this.itemsPerPage) || 1;
    },

    paginatedEmployees() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredEmployees.slice(start, end);
    },

    isAllCurrentPageSelected() {
      if (this.paginatedEmployees.length === 0 || this.isEditing) return false;

      return this.paginatedEmployees.every((employee) =>
        this.selectedEmployeeIds.includes(employee.employeeId)
      );
    },

    isEditing() {
      return this.editingEmployeeId !== null || this.isAdding;
    },
  },

  watch: {
    searchEmployeeId() {
      this.currentPage = 1;
    },
    searchDepartmentId() {
      this.currentPage = 1;
    },
    searchEmployeePhone() {
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
        employeeName: "",
        employeeCode: "",
        employeePhone: "",
        departmentName: "",
        employeeJoindate: "",
        employeeSalary: "",
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
      this.editingEmployeeId = null;
      this.selectedEmployeeIds = [];
      this.resetEditForm();
      this.currentPage = 1;
    },

    handleEdit() {
      if (this.isAdding) {
        alert("Please save or cancel the new row first.");
        return;
      }

      if (this.selectedEmployeeIds.length !== 1) {
        alert("Please select exactly one employee to edit.");
        return;
      }

      const selectedId = this.selectedEmployeeIds[0];
      const employee = this.employees.find(
        (item) => item.employeeId === selectedId
      );

      if (!employee) return;

      this.editingEmployeeId = selectedId;
      this.editForm = {
        employeeName: employee.employeeName,
        employeeCode: employee.employeeCode,
        employeePhone: employee.employeePhone,
        departmentName: employee.departmentName,
        employeeJoindate: employee.employeeJoindate, // ✅ FIXED
        employeeSalary: employee.employeeSalary,     // ✅ FIXED
      };
    },

    handleSave() {
      // ⚠️ still frontend only (not yet API)
      if (this.isAdding) {
        if (
          !this.editForm.employeeName ||
          !this.editForm.employeeCode ||
          !this.editForm.employeePhone ||
          !this.editForm.departmentName ||
          !this.editForm.employeeJoindate ||
          !this.editForm.employeeSalary
        ) {
          alert("Please fill all fields before saving.");
          return;
        }

        const nextNumber = this.employees.length + 1;
        const newEmployeeId = `EMP${String(nextNumber).padStart(3, "0")}`;

        const newEmployee = {
          employeeName: this.editForm.employeeName,
          employeeId: newEmployeeId,
          employeeCode: this.editForm.employeeCode,
          employeePhone: this.editForm.employeePhone,
          departmentName: this.editForm.departmentName,
          employeeJoindate: this.editForm.employeeJoindate,
          employeeSalary: this.editForm.employeeSalary,
        };

        this.employees.unshift(newEmployee);
        this.isAdding = false;
        this.resetEditForm();
        return;
      }

      if (!this.editingEmployeeId) return;

      const index = this.employees.findIndex(
        (e) => e.employeeId === this.editingEmployeeId
      );

      if (index === -1) return;

      this.employees[index] = {
        ...this.employees[index],
        employeeName: this.editForm.employeeName,
        employeeCode: this.editForm.employeeCode,
        employeePhone: this.editForm.employeePhone,
        departmentName: this.editForm.departmentName,
        employeeJoindate: this.editForm.employeeJoindate,
        employeeSalary: this.editForm.employeeSalary,
      };

      this.employees = [...this.employees];
      this.editingEmployeeId = null;
      this.resetEditForm();
    },

    handleCancelEdit() {
      this.isAdding = false;
      this.editingEmployeeId = null;
      this.resetEditForm();
    },

    handleDelete() {
      if (this.isEditing) {
        alert("Please save or cancel editing first.");
        return;
      }

      if (this.selectedEmployeeIds.length === 0) {
        alert("Please select at least one employee to delete.");
        return;
      }

      const confirmed = window.confirm("Are you sure?");
      if (!confirmed) return;

      this.employees = this.employees.filter(
        (e) => !this.selectedEmployeeIds.includes(e.employeeId)
      );

      this.selectedEmployeeIds = [];
    },

    handleReset() {
      this.searchEmployeeId = "";
      this.searchDepartmentId = "";
      this.searchEmployeePhone = "";
      this.selectedEmployeeIds = [];
      this.currentPage = 1;
      this.handleCancelEdit();
    },

    toggleSelectAllCurrentPage(event) {
      if (this.isEditing) return;

      const currentIds = this.paginatedEmployees.map(
        (e) => e.employeeId
      );

      if (event.target.checked) {
        this.selectedEmployeeIds = [
          ...new Set([...this.selectedEmployeeIds, ...currentIds]),
        ];
      } else {
        this.selectedEmployeeIds = this.selectedEmployeeIds.filter(
          (id) => !currentIds.includes(id)
        );
      }
    },
  },
};
</script>
<style scoped>
.employee-page {
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

.employee-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1200px;
}

.employee-table thead {
  background: #f3f4f6;
}

.employee-table th,
.employee-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #e5e7eb;
  font-size: 14px;
  vertical-align: middle;
}

.employee-table th {
  color: #111827;
  font-weight: 600;
}

.employee-table td {
  color: #374151;
}

.employee-table tbody tr:hover {
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
</style>
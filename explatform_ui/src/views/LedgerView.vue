<template>
  <div class="ledger-page">
    <div class="top-bar">
      <div class="search-group">
        <input
          v-model="searchMaterialCode"
          type="text"
          placeholder="Search by Material Code"
          class="search-input"
        />

        <input
          v-model="searchDeviceCode"
          type="text"
          placeholder="Search by Device Code"
          class="search-input"
        />

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
      <table class="ledger-table">
        <thead>
          <tr>
            <th class="checkbox-col">
              <input
                type="checkbox"
                :checked="isAllCurrentPageSelected"
                @change="toggleSelectAllCurrentPage"
              />
            </th>
            <th>Material Name</th>
            <th>Material Code</th>
            <th>Device Code</th>
            <th>Device Model</th>
            <th>Booked for Employee</th>
            <th>Employee ID</th>
            <th>Booked for Department</th>
            <th>Department ID</th>
            <th>Department Code</th>
            <th>Booked Qty</th>
            <th>Booking Date</th>
            <th>Purpose of Use</th>
            <th>Note</th>
          </tr>
        </thead>

        <tbody>
          <!-- add row -->
          <tr v-if="isAdding" class="editing-row">
            <td class="checkbox-col"></td>
            <td><input v-model="editForm.materialName" class="table-edit-input" /></td>
            <td><input v-model="editForm.materialCode" class="table-edit-input" /></td>
            <td><input v-model="editForm.deviceCode" class="table-edit-input" /></td>
            <td><input v-model="editForm.deviceModel" class="table-edit-input" /></td>
            <td><input v-model="editForm.bookedForEmployee" class="table-edit-input" /></td>
            <td><input v-model="editForm.employeeId" class="table-edit-input" /></td>
            <td><input v-model="editForm.bookedForDepartment" class="table-edit-input" /></td>
            <td><input v-model="editForm.departmentId" class="table-edit-input" /></td>
            <td><input v-model="editForm.departmentCode" class="table-edit-input" /></td>
            <td><input v-model="editForm.bookedQty" class="table-edit-input" /></td>
            <td><input v-model="editForm.bookingDate" type="date" class="table-edit-input" /></td>
            <td><input v-model="editForm.purposeOfUse" class="table-edit-input" /></td>
            <td><input v-model="editForm.note" class="table-edit-input" /></td>
          </tr>

          <!-- existing rows -->
          <tr
            v-for="item in paginatedLedgerItems"
            :key="item.id"
            :class="{ 'editing-row': editingLedgerId === item.id }"
          >
            <td class="checkbox-col">
              <input
                type="checkbox"
                :value="item.id"
                v-model="selectedLedgerIds"
                :disabled="isEditing && editingLedgerId !== item.id"
              />
            </td>

            <template v-if="editingLedgerId === item.id">
              <td><input v-model="editForm.materialName" class="table-edit-input" /></td>
              <td><input v-model="editForm.materialCode" class="table-edit-input" /></td>
              <td><input v-model="editForm.deviceCode" class="table-edit-input" /></td>
              <td><input v-model="editForm.deviceModel" class="table-edit-input" /></td>
              <td><input v-model="editForm.bookedForEmployee" class="table-edit-input" /></td>
              <td><input v-model="editForm.employeeId" class="table-edit-input" /></td>
              <td><input v-model="editForm.bookedForDepartment" class="table-edit-input" /></td>
              <td><input v-model="editForm.departmentId" class="table-edit-input" /></td>
              <td><input v-model="editForm.departmentCode" class="table-edit-input" /></td>
              <td><input v-model="editForm.bookedQty" class="table-edit-input" /></td>
              <td><input v-model="editForm.bookingDate" type="date" class="table-edit-input" /></td>
              <td><input v-model="editForm.purposeOfUse" class="table-edit-input" /></td>
              <td><input v-model="editForm.note" class="table-edit-input" /></td>
            </template>

            <template v-else>
              <td>{{ item.materialName }}</td>
              <td>{{ item.materialCode }}</td>
              <td>{{ item.deviceCode }}</td>
              <td>{{ item.deviceModel }}</td>
              <td>{{ item.bookedForEmployee }}</td>
              <td>{{ item.employeeId }}</td>
              <td>{{ item.bookedForDepartment }}</td>
              <td>{{ item.departmentId }}</td>
              <td>{{ item.departmentCode }}</td>
              <td>{{ item.bookedQty }}</td>
              <td>{{ item.bookingDate }}</td>
              <td>{{ item.purposeOfUse }}</td>
              <td>{{ item.note }}</td>
            </template>
          </tr>

          <tr v-if="!isAdding && paginatedLedgerItems.length === 0">
            <td colspan="14" class="no-data">No ledger data found</td>
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
  name: "LedgerView",
  components: {
    AppPagination,
  },
  data() {
    return {
      searchMaterialCode: "",
      searchDeviceCode: "",
      searchEmployeeId: "",
      searchDepartmentId: "",
      searchStartDate: "",
      searchEndDate: "",
      currentPage: 1,
      itemsPerPage: 5,
      selectedLedgerIds: [],
      editingLedgerId: null,
      isAdding: false,
      editForm: {
        materialName: "",
        materialCode: "",
        deviceCode: "",
        deviceModel: "",
        bookedForEmployee: "",
        employeeId: "",
        bookedForDepartment: "",
        departmentId: "",
        departmentCode: "",
        bookedQty: "",
        bookingDate: "",
        purposeOfUse: "",
        note: "",
      },
      ledgerItems: [
        {
          id: 1,
          materialName: "Laptop Charger",
          materialCode: "MAT-001",
          deviceCode: "DEV-101",
          deviceModel: "Dell Latitude 5420",
          bookedForEmployee: "John Smith",
          employeeId: "EMP001",
          bookedForDepartment: "IT",
          departmentId: "DEP003",
          departmentCode: "D-1003",
          bookedQty: "2",
          bookingDate: "2024-01-10",
          purposeOfUse: "Office setup",
          note: "Urgent issue",
        },
        {
          id: 2,
          materialName: "Keyboard",
          materialCode: "MAT-002",
          deviceCode: "DEV-102",
          deviceModel: "HP ProDesk",
          bookedForEmployee: "Sarah Ahmed",
          employeeId: "EMP002",
          bookedForDepartment: "HR",
          departmentId: "DEP002",
          departmentCode: "D-1002",
          bookedQty: "5",
          bookingDate: "2024-01-18",
          purposeOfUse: "New workstations",
          note: "Standard allocation",
        },
        {
          id: 3,
          materialName: "Printer Toner",
          materialCode: "MAT-003",
          deviceCode: "DEV-103",
          deviceModel: "Canon LBP2900",
          bookedForEmployee: "Michael Lee",
          employeeId: "EMP003",
          bookedForDepartment: "Accounts",
          departmentId: "DEP001",
          departmentCode: "D-1001",
          bookedQty: "3",
          bookingDate: "2024-02-04",
          purposeOfUse: "Monthly print use",
          note: "Keep backup",
        },
        {
          id: 4,
          materialName: "Router",
          materialCode: "MAT-004",
          deviceCode: "DEV-104",
          deviceModel: "TP-Link AX1800",
          bookedForEmployee: "Tanvir Hasan",
          employeeId: "EMP007",
          bookedForDepartment: "Operations",
          departmentId: "DEP005",
          departmentCode: "D-1005",
          bookedQty: "1",
          bookingDate: "2024-02-12",
          purposeOfUse: "Branch network",
          note: "Install this week",
        },
        {
          id: 5,
          materialName: "Mouse",
          materialCode: "MAT-005",
          deviceCode: "DEV-105",
          deviceModel: "Logitech M185",
          bookedForEmployee: "Ayesha Noor",
          employeeId: "EMP006",
          bookedForDepartment: "Accounts",
          departmentId: "DEP001",
          departmentCode: "D-1001",
          bookedQty: "10",
          bookingDate: "2024-02-20",
          purposeOfUse: "Desktop replacement",
          note: "Procurement batch",
        },
        {
          id: 6,
          materialName: "Monitor",
          materialCode: "MAT-006",
          deviceCode: "DEV-106",
          deviceModel: "LG 24MK430",
          bookedForEmployee: "Nusrat Jahan",
          employeeId: "EMP004",
          bookedForDepartment: "HR",
          departmentId: "DEP002",
          departmentCode: "D-1002",
          bookedQty: "2",
          bookingDate: "2024-03-01",
          purposeOfUse: "Dual monitor setup",
          note: "Approved by admin",
        },
      ],
    };
  },
  computed: {
    filteredLedgerItems() {
      return this.ledgerItems.filter((item) => {
        const matchMaterialCode = item.materialCode
          .toLowerCase()
          .includes(this.searchMaterialCode.toLowerCase());

        const matchDeviceCode = item.deviceCode
          .toLowerCase()
          .includes(this.searchDeviceCode.toLowerCase());

        const matchEmployeeId = item.employeeId
          .toLowerCase()
          .includes(this.searchEmployeeId.toLowerCase());

        const matchDepartmentId = item.departmentId
          .toLowerCase()
          .includes(this.searchDepartmentId.toLowerCase());

        const matchStartDate =
          !this.searchStartDate || item.bookingDate >= this.searchStartDate;

        const matchEndDate =
          !this.searchEndDate || item.bookingDate <= this.searchEndDate;

        return (
          matchMaterialCode &&
          matchDeviceCode &&
          matchEmployeeId &&
          matchDepartmentId &&
          matchStartDate &&
          matchEndDate
        );
      });
    },

    totalPages() {
      return Math.ceil(this.filteredLedgerItems.length / this.itemsPerPage) || 1;
    },

    paginatedLedgerItems() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredLedgerItems.slice(start, end);
    },

    isAllCurrentPageSelected() {
      if (this.paginatedLedgerItems.length === 0 || this.isEditing) return false;

      return this.paginatedLedgerItems.every((item) =>
        this.selectedLedgerIds.includes(item.id)
      );
    },

    isEditing() {
      return this.editingLedgerId !== null || this.isAdding;
    },
  },
  watch: {
    searchMaterialCode() {
      this.currentPage = 1;
    },
    searchDeviceCode() {
      this.currentPage = 1;
    },
    searchEmployeeId() {
      this.currentPage = 1;
    },
    searchDepartmentId() {
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
    changePage(page) {
      if (page < 1 || page > this.totalPages) return;
      this.currentPage = page;
    },

    resetEditForm() {
      this.editForm = {
        materialName: "",
        materialCode: "",
        deviceCode: "",
        deviceModel: "",
        bookedForEmployee: "",
        employeeId: "",
        bookedForDepartment: "",
        departmentId: "",
        departmentCode: "",
        bookedQty: "",
        bookingDate: "",
        purposeOfUse: "",
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
      this.editingLedgerId = null;
      this.selectedLedgerIds = [];
      this.resetEditForm();
      this.currentPage = 1;
    },

    handleEdit() {
      if (this.isAdding) {
        alert("Please save or cancel the new row first.");
        return;
      }

      if (this.selectedLedgerIds.length === 0) {
        alert("Please select one row to edit.");
        return;
      }

      if (this.selectedLedgerIds.length > 1) {
        alert("Please select only one row to edit.");
        return;
      }

      const selectedId = this.selectedLedgerIds[0];
      const item = this.ledgerItems.find((row) => row.id === selectedId);

      if (!item) return;

      this.editingLedgerId = selectedId;
      this.editForm = {
        materialName: item.materialName,
        materialCode: item.materialCode,
        deviceCode: item.deviceCode,
        deviceModel: item.deviceModel,
        bookedForEmployee: item.bookedForEmployee,
        employeeId: item.employeeId,
        bookedForDepartment: item.bookedForDepartment,
        departmentId: item.departmentId,
        departmentCode: item.departmentCode,
        bookedQty: item.bookedQty,
        bookingDate: item.bookingDate,
        purposeOfUse: item.purposeOfUse,
        note: item.note,
      };
    },

    handleSave() {
      if (this.isAdding) {
        if (
          !this.editForm.materialName ||
          !this.editForm.materialCode ||
          !this.editForm.deviceCode ||
          !this.editForm.deviceModel ||
          !this.editForm.bookedForEmployee ||
          !this.editForm.employeeId ||
          !this.editForm.bookedForDepartment ||
          !this.editForm.departmentId ||
          !this.editForm.departmentCode ||
          !this.editForm.bookedQty ||
          !this.editForm.bookingDate ||
          !this.editForm.purposeOfUse
        ) {
          alert("Please fill all required fields before saving.");
          return;
        }

        const newItem = {
          id: Date.now(),
          materialName: this.editForm.materialName,
          materialCode: this.editForm.materialCode,
          deviceCode: this.editForm.deviceCode,
          deviceModel: this.editForm.deviceModel,
          bookedForEmployee: this.editForm.bookedForEmployee,
          employeeId: this.editForm.employeeId,
          bookedForDepartment: this.editForm.bookedForDepartment,
          departmentId: this.editForm.departmentId,
          departmentCode: this.editForm.departmentCode,
          bookedQty: this.editForm.bookedQty,
          bookingDate: this.editForm.bookingDate,
          purposeOfUse: this.editForm.purposeOfUse,
          note: this.editForm.note,
        };

        this.ledgerItems.unshift(newItem);
        this.isAdding = false;
        this.resetEditForm();
        return;
      }

      if (!this.editingLedgerId) return;

      const index = this.ledgerItems.findIndex(
        (row) => row.id === this.editingLedgerId
      );

      if (index === -1) return;

      this.ledgerItems[index] = {
        ...this.ledgerItems[index],
        materialName: this.editForm.materialName,
        materialCode: this.editForm.materialCode,
        deviceCode: this.editForm.deviceCode,
        deviceModel: this.editForm.deviceModel,
        bookedForEmployee: this.editForm.bookedForEmployee,
        employeeId: this.editForm.employeeId,
        bookedForDepartment: this.editForm.bookedForDepartment,
        departmentId: this.editForm.departmentId,
        departmentCode: this.editForm.departmentCode,
        bookedQty: this.editForm.bookedQty,
        bookingDate: this.editForm.bookingDate,
        purposeOfUse: this.editForm.purposeOfUse,
        note: this.editForm.note,
      };

      this.ledgerItems = [...this.ledgerItems];
      this.editingLedgerId = null;
      this.resetEditForm();
    },

    handleCancelEdit() {
      this.isAdding = false;
      this.editingLedgerId = null;
      this.resetEditForm();
    },

    handleDelete() {
      if (this.isEditing) {
        alert("Please save or cancel editing first.");
        return;
      }

      if (this.selectedLedgerIds.length === 0) {
        alert("Please select at least one row to delete.");
        return;
      }

      const confirmed = window.confirm(
        "Are you sure you want to delete the selected row(s)?"
      );
      if (!confirmed) return;

      this.ledgerItems = this.ledgerItems.filter(
        (row) => !this.selectedLedgerIds.includes(row.id)
      );
      this.selectedLedgerIds = [];

      if (this.currentPage > this.totalPages) {
        this.currentPage = this.totalPages;
      }
    },

    handleReset() {
      this.searchMaterialCode = "";
      this.searchDeviceCode = "";
      this.searchEmployeeId = "";
      this.searchDepartmentId = "";
      this.searchStartDate = "";
      this.searchEndDate = "";
      this.selectedLedgerIds = [];
      this.currentPage = 1;
      this.handleCancelEdit();
    },

    toggleSelectAllCurrentPage(event) {
      if (this.isEditing) return;

      const currentPageIds = this.paginatedLedgerItems.map((item) => item.id);

      if (event.target.checked) {
        const merged = [...this.selectedLedgerIds, ...currentPageIds];
        this.selectedLedgerIds = [...new Set(merged)];
      } else {
        this.selectedLedgerIds = this.selectedLedgerIds.filter(
          (id) => !currentPageIds.includes(id)
        );
      }
    },
  },
};
</script>

<style scoped>
.ledger-page {
  width: 100%;
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

.ledger-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1800px;
}

.ledger-table thead {
  background: #f3f4f6;
}

.ledger-table th,
.ledger-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #e5e7eb;
  font-size: 14px;
  vertical-align: middle;
}

.ledger-table th {
  color: #111827;
  font-weight: 600;
}

.ledger-table td {
  color: #374151;
}

.ledger-table tbody tr:hover {
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
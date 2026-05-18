<template>
  <div class="ledger-page">

    <!-- SEARCH -->
    <div class="top-bar">

      <div class="search-group">
        <input v-model="searchMaterialCode" type="text" placeholder="Search by Material Code" class="search-input"/>
        <input v-model="searchDeviceCode" type="text" placeholder="Search by Device Code" class="search-input"/>
        <input v-model="searchEmployeeId" type="text" placeholder="Search by Employee ID" class="search-input"/>
        <input v-model="searchDepartmentId" type="text" placeholder="Search by Department ID" class="search-input"/>
        <input v-model="searchStartDate" type="date" class="search-input"/>
        <input v-model="searchEndDate" type="date" class="search-input"/>
      </div>

      <!-- BUTTONS -->
      <div class="button-group">
        <button class="action-btn add-btn" @click="handleAdd">Add</button>
        <button class="action-btn search-btn" @click="handleSearch">Search</button>

        <button
          class="action-btn edit-btn"
          @click="handleEdit"
          :disabled="selectedLedgerIds.length !== 1"
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

        <button class="action-btn delete-btn" @click="handleDelete">
          Delete
        </button>

        <button class="action-btn reset-btn" @click="handleReset">
          Reset
        </button>
      </div>

    </div>

    <!-- TABLE -->
    <div class="table-wrapper">

      <table class="ledger-table">

        <thead>
          <tr>
            <th>
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
            <th>Employee</th>
            <th>Employee ID</th>
            <th>Department</th>
            <th>Department ID</th>
            <th>Dept Code</th>
            <th>Qty</th>
            <th>Date</th>
            <th>Purpose</th>
            <th>Note</th>
          </tr>
        </thead>

        <tbody>

          <!-- ADD / EDIT ROW -->
          <tr v-if="isEditing">

            <td></td>

            <td><input v-model="editForm.materialName"/></td>
            <td><input v-model="editForm.materialCode"/></td>
            <td><input v-model="editForm.deviceCode"/></td>
            <td><input v-model="editForm.deviceModel"/></td>
            <td><input v-model="editForm.bookedForEmployee"/></td>
            <td><input v-model="editForm.employeeId"/></td>
            <td><input v-model="editForm.bookedForDepartment"/></td>
            <td><input v-model="editForm.departmentId"/></td>
            <td><input v-model="editForm.departmentCode"/></td>
            <td><input v-model="editForm.bookedQty"/></td>
            <td><input type="date" v-model="editForm.bookingDate"/></td>
            <td><input v-model="editForm.purposeOfUse"/></td>
            <td><input v-model="editForm.note"/></td>

          </tr>

          <!-- DATA ROW -->
          <tr v-for="item in paginatedLedgerItems" :key="item.id">

            <td>
              <input
                type="checkbox"
                :value="item.id"
                v-model="selectedLedgerIds"
              />
            </td>

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

          </tr>

        </tbody>

      </table>

    </div>

    <!-- PAGINATION -->
    <AppPagination
      :currentPage="currentPage"
      :totalPages="totalPages"
      @page-change="changePage"
    />

  </div>
</template>

<script>
import axios from "axios";
import AppPagination from "../components/AppPagination.vue";

export default {
  name: "LedgerView",
  components: { AppPagination },

  data() {
    return {
      ledgerItems: [],
      selectedLedgerIds: [],

      searchMaterialCode: "",
      searchDeviceCode: "",
      searchEmployeeId: "",
      searchDepartmentId: "",
      searchStartDate: "",
      searchEndDate: "",

      currentPage: 1,
      itemsPerPage: 5,

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
        note: ""
      }
    };
  },

  mounted() {
    this.fetchLedger();
  },

  computed: {

    filteredLedgerItems() {
      return this.ledgerItems.filter(item => {

        const itemDate = item.bookingDate ? new Date(item.bookingDate) : null;
        const start = this.searchStartDate ? new Date(this.searchStartDate) : null;
        const end = this.searchEndDate ? new Date(this.searchEndDate) : null;

        return (
          (item.materialCode || "").toLowerCase().includes(this.searchMaterialCode.toLowerCase()) &&
          (item.deviceCode || "").toLowerCase().includes(this.searchDeviceCode.toLowerCase()) &&
          (item.employeeId || "").toLowerCase().includes(this.searchEmployeeId.toLowerCase()) &&
          (item.departmentId || "").toLowerCase().includes(this.searchDepartmentId.toLowerCase()) &&
          (!start || (itemDate && itemDate >= start)) &&
          (!end || (itemDate && itemDate <= end))
        );
      });
    },

    totalPages() {
      return Math.ceil(this.filteredLedgerItems.length / this.itemsPerPage) || 1;
    },

    paginatedLedgerItems() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      return this.filteredLedgerItems.slice(start, start + this.itemsPerPage);
    },

    isEditing() {
      return this.editingLedgerId !== null || this.isAdding;
    },

    isAllCurrentPageSelected() {
      return (
        this.paginatedLedgerItems.length &&
        this.paginatedLedgerItems.every(i =>
          this.selectedLedgerIds.includes(i.id)
        )
      );
    }
  },

  methods: {

    async fetchLedger() {
      try {
        const res = await axios.get("http://localhost:8080/api/ledger");
        this.ledgerItems = res.data;
      } catch (error) {
        console.error(error);
        alert("Backend not connected");
      }
    },

    changePage(page) {
      this.currentPage = page;
    },

    handleSearch() {
      this.currentPage = 1;
    },

    handleAdd() {
      this.isAdding = true;
      this.editingLedgerId = null;
      this.selectedLedgerIds = [];

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
        note: ""
      };
    },

    async handleSave() {

      // VALIDATION
      if (!this.editForm.materialCode || !this.editForm.deviceCode) {
        alert("Material Code & Device Code required");
        return;
      }

      // ADD
      if (this.isAdding) {
        await axios.post("http://localhost:8080/api/ledger", {
          ...this.editForm,
          bookingDate: this.editForm.bookingDate || null
        });

        this.isAdding = false;
        await this.fetchLedger();
        this.currentPage = 1;
        return;
      }

      // UPDATE
      await axios.put(
        `http://localhost:8080/api/ledger/${this.editingLedgerId}`,
        {
          ...this.editForm,
          bookingDate: this.editForm.bookingDate || null
        }
      );

      this.editingLedgerId = null;
      await this.fetchLedger();
      this.currentPage = 1;
    },

    handleEdit() {
      if (this.selectedLedgerIds.length !== 1) return;

      const id = this.selectedLedgerIds[0];
      const row = this.ledgerItems.find(i => i.id === id);

      this.editingLedgerId = id;
      this.isAdding = false;

      this.editForm = { ...row };
    },

    async handleDelete() {

      if (!confirm("Are you sure you want to delete?")) return;

      await Promise.all(
        this.selectedLedgerIds.map(id =>
          axios.delete(`http://localhost:8080/api/ledger/${id}`)
        )
      );

      this.selectedLedgerIds = [];
      await this.fetchLedger();
      this.currentPage = 1;
    },

    handleCancelEdit() {
      this.isAdding = false;
      this.editingLedgerId = null;
    },

    handleReset() {
      this.searchMaterialCode = "";
      this.searchDeviceCode = "";
      this.searchEmployeeId = "";
      this.searchDepartmentId = "";
      this.searchStartDate = "";
      this.searchEndDate = "";
      this.currentPage = 1;
    },

    toggleSelectAllCurrentPage(e) {
      const ids = this.paginatedLedgerItems.map(i => i.id);

      if (e.target.checked) {
        this.selectedLedgerIds = [...new Set([...this.selectedLedgerIds, ...ids])];
      } else {
        this.selectedLedgerIds = this.selectedLedgerIds.filter(id => !ids.includes(id));
      }
    }
  }
};
</script>

<style scoped>
.top-bar{margin-bottom:20px;}
.search-group{display:flex;gap:10px;overflow-x:auto;}
.button-group{margin-top:12px;display:flex;gap:10px;flex-wrap:wrap;}
.search-input{width:200px;height:40px;padding:0 10px;border:1px solid #ccc;border-radius:8px;}

.action-btn{height:40px;padding:0 16px;border:none;border-radius:8px;color:white;cursor:pointer;}
.add-btn{background:#10b981}
.search-btn{background:#3b82f6}
.edit-btn{background:#f59e0b}
.save-btn{background:#10b981}
.cancel-btn{background:#6b7280}
.delete-btn{background:#ef4444}
.reset-btn{background:#6b7280}

.table-wrapper{overflow-x:auto;margin-top:20px;}
.ledger-table{min-width:1200px;border-collapse:collapse;}
.ledger-table th,.ledger-table td{padding:10px;border-bottom:1px solid #eee;}
</style>
<template>
  <div class="state-page">
    <div class="page-header">
      <button class="back-btn" @click="goBack">← Back</button>
      <h2 class="page-title">Warehouse State - {{ warehouseId }}</h2>
    </div>

    <div class="top-bar">
      <div class="search-group">
        <input
          v-model="searchMaterialId"
          type="text"
          placeholder="Search by Material ID"
          class="search-input"
        />

        <input
          v-model="searchBuyerName"
          type="text"
          placeholder="Search by Buyer Name"
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
      <table class="warehouse-state-table">
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
            <th>Material ID</th>
            <th>Purchase Quantity</th>
            <th>Purchase Date</th>
            <th>Available Qty</th>
            <th>Buyer Name</th>
            <th>Note</th>
          </tr>
        </thead>

        <tbody>
          <tr v-if="isAdding" class="editing-row">
            <td class="checkbox-col"></td>
            <td><input v-model="editForm.materialName" class="table-edit-input" /></td>
            <td><input v-model="editForm.materialId" class="table-edit-input" /></td>
            <td><input v-model="editForm.purchaseQuantity" class="table-edit-input" /></td>
            <td><input v-model="editForm.purchaseDate" type="date" class="table-edit-input" /></td>
            <td><input v-model="editForm.availableQty" class="table-edit-input" /></td>
            <td><input v-model="editForm.buyerName" class="table-edit-input" /></td>
            <td><input v-model="editForm.note" class="table-edit-input" /></td>
          </tr>

          <tr
            v-for="item in paginatedStates"
            :key="item.id"
            :class="{ 'editing-row': editingStateId === item.id }"
          >
            <td class="checkbox-col">
              <input
                type="checkbox"
                :value="item.id"
                v-model="selectedStateIds"
                :disabled="isEditing && editingStateId !== item.id"
              />
            </td>

            <template v-if="editingStateId === item.id">
              <td><input v-model="editForm.materialName" class="table-edit-input" /></td>
              <td><input v-model="editForm.materialId" class="table-edit-input" /></td>
              <td><input v-model="editForm.purchaseQuantity" class="table-edit-input" /></td>
              <td><input v-model="editForm.purchaseDate" type="date" class="table-edit-input" /></td>
              <td><input v-model="editForm.availableQty" class="table-edit-input" /></td>
              <td><input v-model="editForm.buyerName" class="table-edit-input" /></td>
              <td><input v-model="editForm.note" class="table-edit-input" /></td>
            </template>

            <template v-else>
              <td>{{ item.materialName }}</td>
              <td>{{ item.materialId }}</td>
              <td>{{ item.purchaseQuantity }}</td>
              <td>{{ item.purchaseDate }}</td>
              <td>{{ item.availableQty }}</td>
              <td>{{ item.buyerName }}</td>
              <td>{{ item.note }}</td>
            </template>
          </tr>

          <tr v-if="!isAdding && paginatedStates.length === 0">
            <td colspan="8" class="no-data">No warehouse state found</td>
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
  name: "WarehouseStateView",
  components: {
    AppPagination,
  },
  data() {
    return {
      searchMaterialId: "",
      searchBuyerName: "",
      searchStartDate: "",
      searchEndDate: "",
      currentPage: 1,
      itemsPerPage: 5,
      selectedStateIds: [],
      editingStateId: null,
      isAdding: false,
      editForm: {
        materialName: "",
        materialId: "",
        purchaseQuantity: "",
        purchaseDate: "",
        availableQty: "",
        buyerName: "",
        note: "",
      },
      states: [
        {
          id: 1,
          warehouseId: "WH001",
          materialName: "Cotton Fabric",
          materialId: "MAT001",
          purchaseQuantity: "500",
          purchaseDate: "2024-01-05",
          availableQty: "320",
          buyerName: "John Buyer",
          note: "Main stock",
        },
        {
          id: 2,
          warehouseId: "WH001",
          materialName: "Blue Dye",
          materialId: "MAT002",
          purchaseQuantity: "120",
          purchaseDate: "2024-01-08",
          availableQty: "70",
          buyerName: "Sarah Buyer",
          note: "For next batch",
        },
        {
          id: 3,
          warehouseId: "WH002",
          materialName: "Packaging Box",
          materialId: "MAT003",
          purchaseQuantity: "1000",
          purchaseDate: "2024-02-01",
          availableQty: "650",
          buyerName: "Michael",
          note: "Dry storage",
        },
      ],
    };
  },
  computed: {
    warehouseId() {
      return this.$route.params.warehouseId;
    },

    filteredStates() {
      return this.states
        .filter((item) => item.warehouseId === this.warehouseId)
        .filter((item) => {
          const matchMaterialId = item.materialId
            .toLowerCase()
            .includes(this.searchMaterialId.toLowerCase());

          const matchBuyerName = item.buyerName
            .toLowerCase()
            .includes(this.searchBuyerName.toLowerCase());

          const matchStartDate =
            !this.searchStartDate || item.purchaseDate >= this.searchStartDate;

          const matchEndDate =
            !this.searchEndDate || item.purchaseDate <= this.searchEndDate;

          return matchMaterialId && matchBuyerName && matchStartDate && matchEndDate;
        });
    },

    totalPages() {
      return Math.ceil(this.filteredStates.length / this.itemsPerPage) || 1;
    },

    paginatedStates() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredStates.slice(start, end);
    },

    isAllCurrentPageSelected() {
      if (this.paginatedStates.length === 0 || this.isEditing) return false;

      return this.paginatedStates.every((item) =>
        this.selectedStateIds.includes(item.id)
      );
    },

    isEditing() {
      return this.editingStateId !== null || this.isAdding;
    },
  },
  watch: {
    searchMaterialId() {
      this.currentPage = 1;
    },
    searchBuyerName() {
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
      this.$router.push("/dashboard/warehouse");
    },

    changePage(page) {
      if (page < 1 || page > this.totalPages) return;
      this.currentPage = page;
    },

    resetEditForm() {
      this.editForm = {
        materialName: "",
        materialId: "",
        purchaseQuantity: "",
        purchaseDate: "",
        availableQty: "",
        buyerName: "",
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
      this.editingStateId = null;
      this.selectedStateIds = [];
      this.resetEditForm();
      this.currentPage = 1;
    },

    handleEdit() {
      if (this.isAdding) {
        alert("Please save or cancel the new row first.");
        return;
      }

      if (this.selectedStateIds.length === 0) {
        alert("Please select one row to edit.");
        return;
      }

      if (this.selectedStateIds.length > 1) {
        alert("Please select only one row to edit.");
        return;
      }

      const selectedId = this.selectedStateIds[0];
      const item = this.states.find((s) => s.id === selectedId);

      if (!item) return;

      this.editingStateId = selectedId;
      this.editForm = {
        materialName: item.materialName,
        materialId: item.materialId,
        purchaseQuantity: item.purchaseQuantity,
        purchaseDate: item.purchaseDate,
        availableQty: item.availableQty,
        buyerName: item.buyerName,
        note: item.note,
      };
    },

    handleSave() {
      if (this.isAdding) {
        if (
          !this.editForm.materialName ||
          !this.editForm.materialId ||
          !this.editForm.purchaseQuantity ||
          !this.editForm.purchaseDate ||
          !this.editForm.availableQty ||
          !this.editForm.buyerName
        ) {
          alert("Please fill required fields before saving.");
          return;
        }

        const newItem = {
          id: Date.now(),
          warehouseId: this.warehouseId,
          materialName: this.editForm.materialName,
          materialId: this.editForm.materialId,
          purchaseQuantity: this.editForm.purchaseQuantity,
          purchaseDate: this.editForm.purchaseDate,
          availableQty: this.editForm.availableQty,
          buyerName: this.editForm.buyerName,
          note: this.editForm.note,
        };

        this.states.unshift(newItem);
        this.isAdding = false;
        this.resetEditForm();
        return;
      }

      if (!this.editingStateId) return;

      const index = this.states.findIndex((s) => s.id === this.editingStateId);
      if (index === -1) return;

      this.states[index] = {
        ...this.states[index],
        materialName: this.editForm.materialName,
        materialId: this.editForm.materialId,
        purchaseQuantity: this.editForm.purchaseQuantity,
        purchaseDate: this.editForm.purchaseDate,
        availableQty: this.editForm.availableQty,
        buyerName: this.editForm.buyerName,
        note: this.editForm.note,
      };

      this.states = [...this.states];
      this.editingStateId = null;
      this.resetEditForm();
    },

    handleCancelEdit() {
      this.isAdding = false;
      this.editingStateId = null;
      this.resetEditForm();
    },

    handleDelete() {
      if (this.isEditing) {
        alert("Please save or cancel editing first.");
        return;
      }

      if (this.selectedStateIds.length === 0) {
        alert("Please select at least one row to delete.");
        return;
      }

      const confirmed = window.confirm(
        "Are you sure you want to delete the selected row(s)?"
      );
      if (!confirmed) return;

      this.states = this.states.filter((s) => !this.selectedStateIds.includes(s.id));
      this.selectedStateIds = [];

      if (this.currentPage > this.totalPages) {
        this.currentPage = this.totalPages;
      }
    },

    handleReset() {
      this.searchMaterialId = "";
      this.searchBuyerName = "";
      this.searchStartDate = "";
      this.searchEndDate = "";
      this.selectedStateIds = [];
      this.currentPage = 1;
      this.handleCancelEdit();
    },

    toggleSelectAllCurrentPage(event) {
      if (this.isEditing) return;

      const currentPageIds = this.paginatedStates.map((item) => item.id);

      if (event.target.checked) {
        const merged = [...this.selectedStateIds, ...currentPageIds];
        this.selectedStateIds = [...new Set(merged)];
      } else {
        this.selectedStateIds = this.selectedStateIds.filter(
          (id) => !currentPageIds.includes(id)
        );
      }
    },
  },
};
</script>

<style scoped>
.state-page {
  width: 100%;
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

.warehouse-state-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1200px;
}

.warehouse-state-table thead {
  background: #f3f4f6;
}

.warehouse-state-table th,
.warehouse-state-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #e5e7eb;
  font-size: 14px;
  vertical-align: middle;
}

.warehouse-state-table tbody tr:hover {
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
</style>
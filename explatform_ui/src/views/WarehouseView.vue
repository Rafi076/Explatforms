<template>
  <div class="warehouse-page">
    <div class="top-bar">
      <div class="search-group">
        <input
          v-model="searchWarehouseId"
          type="text"
          placeholder="Search by Warehouse ID"
          class="search-input"
        />

        <input
          v-model="searchWarehouseCode"
          type="text"
          placeholder="Search by Warehouse Code"
          class="search-input"
        />

        <input
          v-model="searchWarehouseName"
          type="text"
          placeholder="Search by Warehouse Name"
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
      <table class="warehouse-table">
        <thead>
          <tr>
            <th class="checkbox-col">
              <input
                type="checkbox"
                :checked="isAllCurrentPageSelected"
                @change="toggleSelectAllCurrentPage"
              />
            </th>
            <th>Warehouse ID</th>
            <th>Warehouse Code</th>
            <th>Warehouse Name</th>
          </tr>
        </thead>

        <tbody>
          <tr v-if="isAdding" class="editing-row">
            <td class="checkbox-col"></td>
            <td>Auto</td>
            <td>
              <input
                v-model="editForm.warehouseCode"
                type="text"
                class="table-edit-input"
                placeholder="Warehouse Code"
              />
            </td>
            <td>
              <input
                v-model="editForm.warehouseName"
                type="text"
                class="table-edit-input"
                placeholder="Warehouse Name"
              />
            </td>
          </tr>

          <tr
            v-for="warehouse in paginatedWarehouses"
            :key="warehouse.warehouseId"
            :class="{ 'editing-row': editingWarehouseId === warehouse.warehouseId }"
          >
            <td class="checkbox-col">
              <input
                type="checkbox"
                :value="warehouse.warehouseId"
                v-model="selectedWarehouseIds"
                :disabled="isEditing && editingWarehouseId !== warehouse.warehouseId"
              />
            </td>

            <template v-if="editingWarehouseId === warehouse.warehouseId">
              <td>{{ warehouse.warehouseId }}</td>
              <td>
                <input
                  v-model="editForm.warehouseCode"
                  type="text"
                  class="table-edit-input"
                />
              </td>
              <td>
                <input
                  v-model="editForm.warehouseName"
                  type="text"
                  class="table-edit-input"
                />
              </td>
            </template>

            <template v-else>
              <td>{{ warehouse.warehouseId }}</td>
              <td>{{ warehouse.warehouseCode }}</td>
              <td>
                <span
                  class="clickable-link"
                  @click="goToWarehouseState(warehouse)"
                >
                  {{ warehouse.warehouseName }}
                </span>
              </td>
            </template>
          </tr>

          <tr v-if="!isAdding && paginatedWarehouses.length === 0">
            <td colspan="4" class="no-data">No warehouse found</td>
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
  name: "WarehouseView",
  components: {
    AppPagination,
  },
  data() {
    return {
      searchWarehouseId: "",
      searchWarehouseCode: "",
      searchWarehouseName: "",
      currentPage: 1,
      itemsPerPage: 5,
      selectedWarehouseIds: [],
      editingWarehouseId: null,
      isAdding: false,
      editForm: {
        warehouseCode: "",
        warehouseName: "",
      },
      warehouses: [
        {
          warehouseId: "WH001",
          warehouseCode: "W-1001",
          warehouseName: "Main Store",
        },
        {
          warehouseId: "WH002",
          warehouseCode: "W-1002",
          warehouseName: "Raw Material House",
        },
        {
          warehouseId: "WH003",
          warehouseCode: "W-1003",
          warehouseName: "Finished Goods House",
        },
        {
          warehouseId: "WH004",
          warehouseCode: "W-1004",
          warehouseName: "Packaging House",
        },
        {
          warehouseId: "WH005",
          warehouseCode: "W-1005",
          warehouseName: "Backup Storage",
        },
        {
          warehouseId: "WH006",
          warehouseCode: "W-1006",
          warehouseName: "Export Warehouse",
        },
      ],
    };
  },
  computed: {
    filteredWarehouses() {
      return this.warehouses.filter((warehouse) => {
        const matchWarehouseId = warehouse.warehouseId
          .toLowerCase()
          .includes(this.searchWarehouseId.toLowerCase());

        const matchWarehouseCode = warehouse.warehouseCode
          .toLowerCase()
          .includes(this.searchWarehouseCode.toLowerCase());

        const matchWarehouseName = warehouse.warehouseName
          .toLowerCase()
          .includes(this.searchWarehouseName.toLowerCase());

        return matchWarehouseId && matchWarehouseCode && matchWarehouseName;
      });
    },

    totalPages() {
      return Math.ceil(this.filteredWarehouses.length / this.itemsPerPage) || 1;
    },

    paginatedWarehouses() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredWarehouses.slice(start, end);
    },

    isAllCurrentPageSelected() {
      if (this.paginatedWarehouses.length === 0 || this.isEditing) return false;

      return this.paginatedWarehouses.every((warehouse) =>
        this.selectedWarehouseIds.includes(warehouse.warehouseId)
      );
    },

    isEditing() {
      return this.editingWarehouseId !== null || this.isAdding;
    },
  },
  watch: {
    searchWarehouseId() {
      this.currentPage = 1;
    },
    searchWarehouseCode() {
      this.currentPage = 1;
    },
    searchWarehouseName() {
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
        warehouseCode: "",
        warehouseName: "",
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
      this.editingWarehouseId = null;
      this.selectedWarehouseIds = [];
      this.resetEditForm();
      this.currentPage = 1;
    },

    handleEdit() {
      if (this.isAdding) {
        alert("Please save or cancel the new row first.");
        return;
      }

      if (this.selectedWarehouseIds.length === 0) {
        alert("Please select one warehouse to edit.");
        return;
      }

      if (this.selectedWarehouseIds.length > 1) {
        alert("Please select only one warehouse to edit.");
        return;
      }

      const selectedId = this.selectedWarehouseIds[0];
      const warehouse = this.warehouses.find(
        (item) => item.warehouseId === selectedId
      );

      if (!warehouse) return;

      this.editingWarehouseId = selectedId;
      this.editForm = {
        warehouseCode: warehouse.warehouseCode,
        warehouseName: warehouse.warehouseName,
      };
    },

    handleSave() {
      if (this.isAdding) {
        if (!this.editForm.warehouseCode || !this.editForm.warehouseName) {
          alert("Please fill all fields before saving.");
          return;
        }

        const nextNumber = this.warehouses.length + 1;
        const newWarehouseId = `WH${String(nextNumber).padStart(3, "0")}`;

        const newWarehouse = {
          warehouseId: newWarehouseId,
          warehouseCode: this.editForm.warehouseCode,
          warehouseName: this.editForm.warehouseName,
        };

        this.warehouses.unshift(newWarehouse);
        this.isAdding = false;
        this.resetEditForm();
        return;
      }

      if (!this.editingWarehouseId) return;

      const warehouseIndex = this.warehouses.findIndex(
        (warehouse) => warehouse.warehouseId === this.editingWarehouseId
      );

      if (warehouseIndex === -1) return;

      this.warehouses[warehouseIndex] = {
        ...this.warehouses[warehouseIndex],
        warehouseCode: this.editForm.warehouseCode,
        warehouseName: this.editForm.warehouseName,
      };

      this.warehouses = [...this.warehouses];
      this.editingWarehouseId = null;
      this.resetEditForm();
    },

    handleCancelEdit() {
      this.isAdding = false;
      this.editingWarehouseId = null;
      this.resetEditForm();
    },

    handleDelete() {
      if (this.isEditing) {
        alert("Please save or cancel editing first.");
        return;
      }

      if (this.selectedWarehouseIds.length === 0) {
        alert("Please select at least one warehouse to delete.");
        return;
      }

      const confirmed = window.confirm(
        "Are you sure you want to delete the selected warehouse(s)?"
      );

      if (!confirmed) return;

      this.warehouses = this.warehouses.filter(
        (warehouse) =>
          !this.selectedWarehouseIds.includes(warehouse.warehouseId)
      );

      this.selectedWarehouseIds = [];

      if (this.currentPage > this.totalPages) {
        this.currentPage = this.totalPages;
      }
    },

    handleReset() {
      this.searchWarehouseId = "";
      this.searchWarehouseCode = "";
      this.searchWarehouseName = "";
      this.selectedWarehouseIds = [];
      this.currentPage = 1;
      this.handleCancelEdit();
    },

    toggleSelectAllCurrentPage(event) {
      if (this.isEditing) return;

      const currentPageIds = this.paginatedWarehouses.map(
        (warehouse) => warehouse.warehouseId
      );

      if (event.target.checked) {
        const merged = [...this.selectedWarehouseIds, ...currentPageIds];
        this.selectedWarehouseIds = [...new Set(merged)];
      } else {
        this.selectedWarehouseIds = this.selectedWarehouseIds.filter(
          (id) => !currentPageIds.includes(id)
        );
      }
    },

    goToWarehouseState(warehouse) {
      this.$router.push(`/dashboard/warehouse-state/${warehouse.warehouseId}`);
    },
  },
};
</script>

<style scoped>
.warehouse-page {
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

.warehouse-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 800px;
}

.warehouse-table thead {
  background: #f3f4f6;
}

.warehouse-table th,
.warehouse-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #e5e7eb;
  font-size: 14px;
  vertical-align: middle;
}

.warehouse-table th {
  color: #111827;
  font-weight: 600;
}

.warehouse-table td {
  color: #374151;
}

.warehouse-table tbody tr:hover {
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
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

      warehouses: [],
    };
  },

  mounted() {
    this.fetchWarehouses();
  },

  computed: {
    filteredWarehouses() {
      return this.warehouses.filter((warehouse) => {
        return (
          (warehouse.warehouseId || "")
            .toLowerCase()
            .includes(this.searchWarehouseId.toLowerCase()) &&
          (warehouse.warehouseCode || "")
            .toLowerCase()
            .includes(this.searchWarehouseCode.toLowerCase()) &&
          (warehouse.warehouseName || "")
            .toLowerCase()
            .includes(this.searchWarehouseName.toLowerCase())
        );
      });
    },

    totalPages() {
      return Math.ceil(this.filteredWarehouses.length / this.itemsPerPage) || 1;
    },

    paginatedWarehouses() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      return this.filteredWarehouses.slice(start, start + this.itemsPerPage);
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
    // ✅ BASE URL
    BASE_URL() {
      return "http://localhost:8080/api";
    },

    // ✅ FETCH
    fetchWarehouses() {
      fetch(`${this.BASE_URL()}/warehouses`)
        .then((res) => res.json())
        .then((data) => {
          console.log("API DATA:", data);
          this.warehouses = data;
        })
        .catch((err) => console.error("Fetch Error:", err));
    },

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
        alert("Please save or cancel current editing first.");
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
        alert("Please save/cancel new row first.");
        return;
      }

      if (this.selectedWarehouseIds.length !== 1) {
        alert("Select exactly ONE warehouse.");
        return;
      }

      const selectedId = this.selectedWarehouseIds[0];

      const warehouse = this.warehouses.find(
        (w) => w.warehouseId === selectedId
      );

      if (!warehouse) return;

      this.editingWarehouseId = selectedId;

      this.editForm = {
        warehouseCode: warehouse.warehouseCode,
        warehouseName: warehouse.warehouseName,
      };
    },

    // 🔥 MAIN FIX HERE
    handleSave() {

      // ✅ ADD
      if (this.isAdding) {

        // 🔥 AUTO GENERATE ID
        const nextIdNumber = this.warehouses.length + 1;
        const warehouseId = "WH" + String(nextIdNumber).padStart(3, "0");

        const payload = {
          warehouseId: warehouseId,
          warehouseCode: this.editForm.warehouseCode,
          warehouseName: this.editForm.warehouseName,
        };

        fetch(`${this.BASE_URL()}/warehouses`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        })
          .then((res) => res.json())
          .then(() => {
            console.log("Warehouse Added");
            this.fetchWarehouses();
            this.isAdding = false;
          })
          .catch((err) => console.error("POST ERROR:", err));

        return;
      }

      // ✅ UPDATE
      const selected = this.warehouses.find(
        (w) => w.warehouseId === this.editingWarehouseId
      );

      if (!selected) return;

      fetch(`${this.BASE_URL()}/warehouses/${selected.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          ...selected,
          ...this.editForm,
        }),
      })
        .then((res) => res.json())
        .then(() => {
          console.log("Warehouse Updated");
          this.fetchWarehouses();
          this.editingWarehouseId = null;
        })
        .catch((err) => console.error("PUT ERROR:", err));
    },

    handleCancelEdit() {
      this.isAdding = false;
      this.editingWarehouseId = null;
      this.resetEditForm();
    },

    handleDelete() {
      const selected = this.warehouses.filter((w) =>
        this.selectedWarehouseIds.includes(w.warehouseId)
      );

      Promise.all(
        selected.map((w) =>
          fetch(`${this.BASE_URL()}/warehouses/${w.id}`, {
            method: "DELETE",
          })
        )
      ).then(() => {
        this.fetchWarehouses();
        this.selectedWarehouseIds = [];
      });
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
        (w) => w.warehouseId
      );

      if (event.target.checked) {
        this.selectedWarehouseIds = [
          ...new Set([...this.selectedWarehouseIds, ...currentPageIds]),
        ];
      } else {
        this.selectedWarehouseIds = this.selectedWarehouseIds.filter(
          (id) => !currentPageIds.includes(id)
        );
      }
    },

    goToWarehouseState(warehouse) {
      this.$router.push(
        `/dashboard/warehouse-state/${warehouse.warehouseId}`
      );
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
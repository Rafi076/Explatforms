<template>
  <div class="state-page">
    <div class="page-header">
      <button class="back-btn" @click="goBack">← Back</button>
      <h2 class="page-title">Warehouse Work - {{ warehouseId }}</h2>
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
            :key="item.id || item.materialId"
            :class="{ 'editing-row': Number(editingStateId) === Number(item.id) }"
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

      states: [],
    };
  },

  mounted() {
    this.fetchStates();
  },

  computed: {
    // ✅ get from route
    warehouseId() {
      return this.$route.params.warehouseId;
    },

    // ✅ NO NEED extra warehouseId filter (API already filtered)
    filteredStates() {
      return this.states.filter((item) => {
        return (
          (item.materialId || "")
            .toLowerCase()
            .includes(this.searchMaterialId.toLowerCase()) &&

          (item.buyerName || "")
            .toLowerCase()
            .includes(this.searchBuyerName.toLowerCase()) &&

          (!this.searchStartDate ||
            item.purchaseDate >= this.searchStartDate) &&

          (!this.searchEndDate ||
            item.purchaseDate <= this.searchEndDate)
        );
      });
    },

    totalPages() {
      return Math.ceil(this.filteredStates.length / this.itemsPerPage) || 1;
    },

    paginatedStates() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      return this.filteredStates.slice(start, start + this.itemsPerPage);
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
    // ✅ FIXED POSITION
    BASE_URL() {
      return "http://localhost:8080/api";
    },

    // ✅ FETCH DATA
    fetchStates() {
      fetch(`${this.BASE_URL()}/warehouse-works/${this.warehouseId}`)
        .then((res) => res.json())
        .then((data) => {
          console.log("WORK DATA:", data); // debug
          this.states = data; // ✅ correct
        })
        .catch((err) => console.error("Fetch error:", err));
    },

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
        alert("Please save or cancel current editing first.");
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
    alert("Save/cancel new row first.");
    return;
  }

  if (this.selectedStateIds.length !== 1) {
    alert("Select exactly ONE row.");
    return;
  }

  const selectedId = Number(this.selectedStateIds[0]);
  this.selectedStateIds = [];

  console.log("Selected ID:", selectedId);
  console.log("All states:", this.states);

  const item = this.states.find((s) => Number(s.id) === Number(selectedId));

  if (!item) {
    alert("Selected item not found!");
    return;
  }

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
  const payload = {
  materialName: this.editForm.materialName || "",
  materialId: this.editForm.materialId || "",
  purchaseQuantity: parseInt(this.editForm.purchaseQuantity) || 0,
  purchaseDate: this.editForm.purchaseDate || null,
  availableQty: parseInt(this.editForm.availableQty) || 0,
  buyerName: this.editForm.buyerName || "",
  note: this.editForm.note || "",
  warehouseId: this.warehouseId,
};

  // ✅ ADD
  if (this.isAdding) {
    fetch(`${this.BASE_URL()}/warehouse-works`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    })
      .then(() => {
        this.fetchStates();
        this.isAdding = false;
        this.resetEditForm();
        this.selectedStateIds = [];
      })
      .catch(err => console.error("POST ERROR:", err));

    return;
  }

  // ✅ UPDATE
  fetch(`${this.BASE_URL()}/warehouse-works/${this.editingStateId}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  })
    .then(() => {
      this.fetchStates(); // refresh DB data
      this.editingStateId = null;
      this.selectedStateIds = [];
      this.resetEditForm();
    })
    .catch(err => console.error("PUT ERROR:", err));
},

    handleCancelEdit() {
      this.isAdding = false;
      this.editingStateId = null;
      this.resetEditForm();
    },

    handleDelete() {
      Promise.all(
        this.selectedStateIds.map((id) =>
          fetch(`${this.BASE_URL()}/warehouse-works/${id}`, {
            method: "DELETE",
          })
        )
      ).then(() => {
        this.fetchStates();
        this.selectedStateIds = [];
      });
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

      const currentIds = this.paginatedStates.map((item) => item.id);

      if (event.target.checked) {
        this.selectedStateIds = [
          ...new Set([...this.selectedStateIds, ...currentIds]),
        ];
      } else {
        this.selectedStateIds = this.selectedStateIds.filter(
          (id) => !currentIds.includes(id)
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
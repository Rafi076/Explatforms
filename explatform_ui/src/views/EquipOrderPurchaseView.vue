<template>
  <div class="equip-page">
    <div class="tab-top-bar">
      <div class="tab-buttons">
        <button
          class="tab-btn"
          :class="{ active: activeTab === 'order' }"
          @click="switchTab('order')"
        >
          Order
        </button>

        <button
          class="tab-btn"
          :class="{ active: activeTab === 'purchase' }"
          @click="switchTab('purchase')"
        >
          Purchase
        </button>
      </div>
    </div>

    <!-- ORDER PAGE -->
    <div v-if="activeTab === 'order'">
      <div class="top-bar">
        <div class="search-group">
          <input
            v-model="orderSearchOrderId"
            type="text"
            placeholder="Search by Order ID"
            class="search-input"
          />

          <input
            v-model="orderSearchOrderDept"
            type="text"
            placeholder="Search by Order Dept"
            class="search-input"
          />

          <input
            v-model="orderSearchOrderName"
            type="text"
            placeholder="Search by Order Name"
            class="search-input"
          />
        </div>

        <div class="button-group">
          <button class="action-btn add-btn" @click="handleOrderAdd">Add</button>
          <button class="action-btn search-btn" @click="handleOrderSearch">Search</button>
          <button class="action-btn copy-btn" @click="openOrderCopyModal">Copy</button>

          <button
            v-if="!isOrderEditing"
            class="action-btn edit-btn"
            @click="handleOrderEdit"
          >
            Edit
          </button>

          <button
            v-if="isOrderEditing"
            class="action-btn save-btn"
            @click="handleOrderSave"
          >
            Save
          </button>

          <button
            v-if="isOrderEditing"
            class="action-btn cancel-btn"
            @click="handleOrderCancelEdit"
          >
            Cancel
          </button>

          <button class="action-btn delete-btn" @click="handleOrderDelete">Delete</button>
          <button class="action-btn reset-btn" @click="handleOrderReset">Reset</button>
        </div>
      </div>

      <div class="table-wrapper">
        <table class="equip-table">
          <thead>
            <tr>
              <th class="checkbox-col">
                <input
                  type="checkbox"
                  :checked="isAllCurrentOrderPageSelected"
                  @change="toggleSelectAllOrderCurrentPage"
                />
              </th>
              <th>Order ID</th>
              <th>Order Dept</th>
              <th>Order Name</th>
              <th>Order For</th>
              <th>Order Qty</th>
              <th>Get Qty</th>
              <th>Using Qty</th>
              <th>Status</th>
              <th>Note</th>
            </tr>
          </thead>

          <tbody>
            <tr v-if="isOrderAdding" class="editing-row">
              <td></td>
              <td>Auto</td>
              <td><input v-model="orderEditForm.orderDept" class="table-edit-input" /></td>
              <td><input v-model="orderEditForm.orderName" class="table-edit-input" /></td>
              <td><input v-model="orderEditForm.orderFor" class="table-edit-input" /></td>
              <td><input v-model="orderEditForm.orderQty" class="table-edit-input" /></td>
              <td><input v-model="orderEditForm.getQty" class="table-edit-input" /></td>
              <td><input v-model="orderEditForm.usingQty" class="table-edit-input" /></td>
              <td>{{ getOrderStatus(orderEditForm) }}</td>
              <td><input v-model="orderEditForm.note" class="table-edit-input" /></td>
            </tr>

            <tr
              v-for="item in paginatedOrderItems"
              :key="item.id"
              :class="{ 'editing-row': editingOrderId === item.id, 'completed-row': getOrderStatus(item) === 'Complete' }"
            >
              <td class="checkbox-col">
                <input
                  type="checkbox"
                  :value="item.id"
                  v-model="selectedOrderIds"
                  :disabled="
                    (isOrderEditing && editingOrderId !== item.id) ||
                    getOrderStatus(item) === 'Complete'
                  "
                />
              </td>

              <template v-if="editingOrderId === item.id">
                <td>{{ item.orderId }}</td>
                <td><input v-model="orderEditForm.orderDept" class="table-edit-input" /></td>
                <td><input v-model="orderEditForm.orderName" class="table-edit-input" /></td>
                <td><input v-model="orderEditForm.orderFor" class="table-edit-input" /></td>
                <td><input v-model="orderEditForm.orderQty" class="table-edit-input" /></td>
                <td><input v-model="orderEditForm.getQty" class="table-edit-input" /></td>
                <td><input v-model="orderEditForm.usingQty" class="table-edit-input" /></td>
                <td>{{ getOrderStatus(orderEditForm) }}</td>
                <td><input v-model="orderEditForm.note" class="table-edit-input" /></td>
              </template>

              <template v-else>
                <td>{{ item.orderId }}</td>
                <td>{{ item.orderDept }}</td>
                <td>{{ item.orderName }}</td>
                <td>{{ item.orderFor }}</td>

                <td>
                  <div class="qty-control">
                    <button
                      class="qty-btn minus-btn"
                      @click="changeOrderQty(item.id, -1)"
                      :disabled="getOrderStatus(item) === 'Complete'"
                    >
                      -
                    </button>
                    <span class="qty-value">{{ item.orderQty }}</span>
                    <button
                      class="qty-btn plus-btn"
                      @click="changeOrderQty(item.id, 1)"
                      :disabled="getOrderStatus(item) === 'Complete'"
                    >
                      +
                    </button>
                  </div>
                </td>

                <td>{{ item.getQty }}</td>
                <td>{{ item.usingQty }}</td>
                <td>
                  <span class="status-badge" :class="statusClass(getOrderStatus(item))">
                    {{ getOrderStatus(item) }}
                  </span>
                </td>
                <td>{{ item.note }}</td>
              </template>
            </tr>

            <tr v-if="!isOrderAdding && paginatedOrderItems.length === 0">
              <td colspan="10" class="no-data">No order data found</td>
            </tr>
          </tbody>
        </table>
      </div>

      <AppPagination
        :currentPage="orderCurrentPage"
        :totalPages="orderTotalPages"
        @page-change="changeOrderPage"
      />
    </div>

    <!-- PURCHASE PAGE -->
    <div v-else>
      <div class="top-bar">
        <div class="search-group">
          <input
            v-model="purchaseSearchPurchaseId"
            type="text"
            placeholder="Search by Purchase ID"
            class="search-input"
          />

          <input
            v-model="purchaseSearchMaterial"
            type="text"
            placeholder="Search by Purchase Material"
            class="search-input"
          />
        </div>

        <div class="button-group">
          <button class="action-btn add-btn" @click="handlePurchaseAdd">Add</button>
          <button class="action-btn search-btn" @click="handlePurchaseSearch">Search</button>
          <button class="action-btn copy-btn" @click="handlePurchaseCopy">Copy</button>

          <button
            v-if="!isPurchaseEditing"
            class="action-btn edit-btn"
            @click="handlePurchaseEdit"
          >
            Edit
          </button>

          <button
            v-if="isPurchaseEditing"
            class="action-btn save-btn"
            @click="handlePurchaseSave"
          >
            Save
          </button>

          <button
            v-if="isPurchaseEditing"
            class="action-btn cancel-btn"
            @click="handlePurchaseCancelEdit"
          >
            Cancel
          </button>

          <button class="action-btn delete-btn" @click="handlePurchaseDelete">Delete</button>
          <button class="action-btn reset-btn" @click="handlePurchaseReset">Reset</button>
        </div>
      </div>

      <div class="table-wrapper">
        <table class="equip-table">
          <thead>
            <tr>
              <th class="checkbox-col">
                <input
                  type="checkbox"
                  :checked="isAllCurrentPurchasePageSelected"
                  @change="toggleSelectAllPurchaseCurrentPage"
                />
              </th>
              <th>Purchase Id</th>
              <th>Purchase Material</th>
              <th>Purchase Qty</th>
              <th>Deliver Quantity</th>
              <th>Note</th>
            </tr>
          </thead>

          <tbody>
            <tr v-if="isPurchaseAdding" class="editing-row">
              <td></td>
              <td>Auto</td>
              <td><input v-model="purchaseEditForm.purchaseMaterial" class="table-edit-input" /></td>
              <td><input v-model="purchaseEditForm.purchaseQty" class="table-edit-input" /></td>
              <td><input v-model="purchaseEditForm.deliverQuantity" class="table-edit-input" /></td>
              <td><input v-model="purchaseEditForm.note" class="table-edit-input" /></td>
            </tr>

            <tr
              v-for="item in paginatedPurchaseItems"
              :key="item.id"
              :class="{ 'editing-row': editingPurchaseId === item.id }"
            >
              <td class="checkbox-col">
                <input
                  type="checkbox"
                  :value="item.id"
                  v-model="selectedPurchaseIds"
                  :disabled="isPurchaseEditing && editingPurchaseId !== item.id"
                />
              </td>

              <template v-if="editingPurchaseId === item.id">
                <td>{{ item.purchaseId }}</td>
                <td><input v-model="purchaseEditForm.purchaseMaterial" class="table-edit-input" /></td>
                <td><input v-model="purchaseEditForm.purchaseQty" class="table-edit-input" /></td>
                <td><input v-model="purchaseEditForm.deliverQuantity" class="table-edit-input" /></td>
                <td><input v-model="purchaseEditForm.note" class="table-edit-input" /></td>
              </template>

              <template v-else>
                <td>{{ item.purchaseId }}</td>
                <td>{{ item.purchaseMaterial }}</td>
                <td>{{ item.purchaseQty }}</td>
                <td>{{ item.deliverQuantity }}</td>
                <td>{{ item.note }}</td>
              </template>
            </tr>

            <tr v-if="!isPurchaseAdding && paginatedPurchaseItems.length === 0">
              <td colspan="6" class="no-data">No purchase data found</td>
            </tr>
          </tbody>
        </table>
      </div>

      <AppPagination
        :currentPage="purchaseCurrentPage"
        :totalPages="purchaseTotalPages"
        @page-change="changePurchasePage"
      />
    </div>

    <!-- ORDER COPY MODAL -->
    <div v-if="showOrderCopyModal" class="modal-overlay" @click.self="closeOrderCopyModal">
      <div class="modal-box">
        <h3>Copy Order</h3>

        <div class="modal-grid">
          <div class="modal-field">
            <label>Order ID</label>
            <input :value="copyOrderForm.orderId" class="table-edit-input" disabled />
          </div>

          <div class="modal-field">
            <label>Order Dept</label>
            <input v-model="copyOrderForm.orderDept" class="table-edit-input" />
          </div>

          <div class="modal-field">
            <label>Order Name</label>
            <input v-model="copyOrderForm.orderName" class="table-edit-input" />
          </div>

          <div class="modal-field">
            <label>Order For</label>
            <input v-model="copyOrderForm.orderFor" class="table-edit-input" />
          </div>

          <div class="modal-field">
            <label>Order Qty</label>
            <input v-model="copyOrderForm.orderQty" class="table-edit-input" />
          </div>

          <div class="modal-field">
            <label>Get Qty</label>
            <input v-model="copyOrderForm.getQty" class="table-edit-input" />
          </div>

          <div class="modal-field">
            <label>Using Qty</label>
            <input v-model="copyOrderForm.usingQty" class="table-edit-input" />
          </div>

          <div class="modal-field">
            <label>Note</label>
            <input v-model="copyOrderForm.note" class="table-edit-input" />
          </div>
        </div>

        <div class="modal-actions">
          <button class="action-btn save-btn" @click="confirmOrderCopy">Create Copy</button>
          <button class="action-btn cancel-btn" @click="closeOrderCopyModal">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AppPagination from "../components/AppPagination.vue";

export default {
  name: "EquipOrderPurchaseView",
  components: {
    AppPagination,
  },
  data() {
    return {
      activeTab: "order",

      orderSearchOrderId: "",
      orderSearchOrderDept: "",
      orderSearchOrderName: "",
      orderCurrentPage: 1,
      orderItemsPerPage: 5,
      selectedOrderIds: [],
      editingOrderId: null,
      isOrderAdding: false,
      showOrderCopyModal: false,
      copyOrderForm: {
        orderId: "",
        orderDept: "",
        orderName: "",
        orderFor: "",
        orderQty: "",
        getQty: "",
        usingQty: "",
        note: "",
      },
      orderEditForm: {
        orderDept: "",
        orderName: "",
        orderFor: "",
        orderQty: "",
        getQty: "",
        usingQty: "",
        note: "",
      },
      orderItems: [
        {
          id: 1,
          orderId: "ORD001",
          orderDept: "IT",
          orderName: "Laptop",
          orderFor: "New Joiner",
          orderQty: 10,
          getQty: 7,
          usingQty: 5,
          note: "Urgent order",
        },
        {
          id: 2,
          orderId: "ORD002",
          orderDept: "HR",
          orderName: "Chair",
          orderFor: "Office Setup",
          orderQty: 20,
          getQty: 12,
          usingQty: 10,
          note: "Second batch",
        },
        {
          id: 3,
          orderId: "ORD003",
          orderDept: "Accounts",
          orderName: "Printer",
          orderFor: "Billing Section",
          orderQty: 3,
          getQty: 0,
          usingQty: 1,
          note: "Approved",
        },
      ],

      purchaseSearchPurchaseId: "",
      purchaseSearchMaterial: "",
      purchaseCurrentPage: 1,
      purchaseItemsPerPage: 5,
      selectedPurchaseIds: [],
      editingPurchaseId: null,
      isPurchaseAdding: false,
      purchaseEditForm: {
        purchaseMaterial: "",
        purchaseQty: "",
        deliverQuantity: "",
        note: "",
      },
      purchaseItems: [
        {
          id: 1,
          purchaseId: "PUR001",
          purchaseMaterial: "Laptop",
          purchaseQty: 25,
          deliverQuantity: 20,
          note: "Partial delivery",
        },
        {
          id: 2,
          purchaseId: "PUR002",
          purchaseMaterial: "Office Chair",
          purchaseQty: 40,
          deliverQuantity: 35,
          note: "Need follow-up",
        },
        {
          id: 3,
          purchaseId: "PUR003",
          purchaseMaterial: "Keyboard",
          purchaseQty: 50,
          deliverQuantity: 50,
          note: "Completed",
        },
      ],
    };
  },
  computed: {
    filteredOrderItems() {
      return this.orderItems.filter((item) => {
        const matchOrderId = item.orderId
          .toLowerCase()
          .includes(this.orderSearchOrderId.toLowerCase());

        const matchOrderDept = item.orderDept
          .toLowerCase()
          .includes(this.orderSearchOrderDept.toLowerCase());

        const matchOrderName = item.orderName
          .toLowerCase()
          .includes(this.orderSearchOrderName.toLowerCase());

        return matchOrderId && matchOrderDept && matchOrderName;
      });
    },

    orderTotalPages() {
      return Math.ceil(this.filteredOrderItems.length / this.orderItemsPerPage) || 1;
    },

    paginatedOrderItems() {
      const start = (this.orderCurrentPage - 1) * this.orderItemsPerPage;
      const end = start + this.orderItemsPerPage;
      return this.filteredOrderItems.slice(start, end);
    },

    isOrderEditing() {
      return this.editingOrderId !== null || this.isOrderAdding;
    },

    isAllCurrentOrderPageSelected() {
      if (this.paginatedOrderItems.length === 0 || this.isOrderEditing) return false;

      const selectableRows = this.paginatedOrderItems.filter(
        (item) => this.getOrderStatus(item) !== "Complete"
      );

      if (selectableRows.length === 0) return false;

      return selectableRows.every((item) => this.selectedOrderIds.includes(item.id));
    },

    filteredPurchaseItems() {
      return this.purchaseItems.filter((item) => {
        const matchPurchaseId = item.purchaseId
          .toLowerCase()
          .includes(this.purchaseSearchPurchaseId.toLowerCase());

        const matchMaterial = item.purchaseMaterial
          .toLowerCase()
          .includes(this.purchaseSearchMaterial.toLowerCase());

        return matchPurchaseId && matchMaterial;
      });
    },

    purchaseTotalPages() {
      return Math.ceil(this.filteredPurchaseItems.length / this.purchaseItemsPerPage) || 1;
    },

    paginatedPurchaseItems() {
      const start = (this.purchaseCurrentPage - 1) * this.purchaseItemsPerPage;
      const end = start + this.purchaseItemsPerPage;
      return this.filteredPurchaseItems.slice(start, end);
    },

    isPurchaseEditing() {
      return this.editingPurchaseId !== null || this.isPurchaseAdding;
    },

    isAllCurrentPurchasePageSelected() {
      if (this.paginatedPurchaseItems.length === 0 || this.isPurchaseEditing) return false;

      return this.paginatedPurchaseItems.every((item) =>
        this.selectedPurchaseIds.includes(item.id)
      );
    },
  },
  methods: {
    switchTab(tab) {
      this.activeTab = tab;
    },

    getOrderStatus(item) {
      const orderQty = Number(item.orderQty) || 0;
      const getQty = Number(item.getQty) || 0;

      if (getQty <= 0) return "Incomplete";
      if (getQty >= orderQty) return "Complete";
      return "Partially Complete";
    },

    statusClass(status) {
      if (status === "Complete") return "status-complete";
      if (status === "Partially Complete") return "status-partial";
      return "status-incomplete";
    },

    changeOrderPage(page) {
      if (page < 1 || page > this.orderTotalPages) return;
      this.orderCurrentPage = page;
    },

    resetOrderEditForm() {
      this.orderEditForm = {
        orderDept: "",
        orderName: "",
        orderFor: "",
        orderQty: "",
        getQty: "",
        usingQty: "",
        note: "",
      };
    },

    handleOrderSearch() {
      this.orderCurrentPage = 1;
    },

    handleOrderAdd() {
      if (this.isOrderEditing) {
        alert("Please save or cancel the current editing first.");
        return;
      }

      this.isOrderAdding = true;
      this.editingOrderId = null;
      this.selectedOrderIds = [];
      this.resetOrderEditForm();
      this.orderCurrentPage = 1;
    },

    handleOrderEdit() {
      if (this.isOrderAdding) {
        alert("Please save or cancel the new row first.");
        return;
      }

      if (this.selectedOrderIds.length === 0) {
        alert("Please select one row to edit.");
        return;
      }

      if (this.selectedOrderIds.length > 1) {
        alert("Please select only one row to edit.");
        return;
      }

      const selectedId = this.selectedOrderIds[0];
      const item = this.orderItems.find((row) => row.id === selectedId);

      if (!item) return;

      if (this.getOrderStatus(item) === "Complete") {
        alert("Completed orders cannot be edited.");
        return;
      }

      this.editingOrderId = selectedId;
      this.orderEditForm = {
        orderDept: item.orderDept,
        orderName: item.orderName,
        orderFor: item.orderFor,
        orderQty: item.orderQty,
        getQty: item.getQty,
        usingQty: item.usingQty,
        note: item.note,
      };
    },

    handleOrderSave() {
      if (this.isOrderAdding) {
        if (
          !this.orderEditForm.orderDept ||
          !this.orderEditForm.orderName ||
          !this.orderEditForm.orderFor ||
          this.orderEditForm.orderQty === "" ||
          this.orderEditForm.getQty === "" ||
          this.orderEditForm.usingQty === ""
        ) {
          alert("Please fill all required fields before saving.");
          return;
        }

        const newItem = {
          id: Date.now(),
          orderId: `ORD${String(this.orderItems.length + 1).padStart(3, "0")}`,
          orderDept: this.orderEditForm.orderDept,
          orderName: this.orderEditForm.orderName,
          orderFor: this.orderEditForm.orderFor,
          orderQty: Number(this.orderEditForm.orderQty),
          getQty: Number(this.orderEditForm.getQty),
          usingQty: Number(this.orderEditForm.usingQty),
          note: this.orderEditForm.note,
        };

        this.orderItems.unshift(newItem);
        this.isOrderAdding = false;
        this.resetOrderEditForm();
        return;
      }

      if (!this.editingOrderId) return;

      const index = this.orderItems.findIndex((row) => row.id === this.editingOrderId);
      if (index === -1) return;

      this.orderItems[index] = {
        ...this.orderItems[index],
        orderDept: this.orderEditForm.orderDept,
        orderName: this.orderEditForm.orderName,
        orderFor: this.orderEditForm.orderFor,
        orderQty: Number(this.orderEditForm.orderQty),
        getQty: Number(this.orderEditForm.getQty),
        usingQty: Number(this.orderEditForm.usingQty),
        note: this.orderEditForm.note,
      };

      this.orderItems = [...this.orderItems];
      this.editingOrderId = null;
      this.resetOrderEditForm();
    },

    handleOrderCancelEdit() {
      this.isOrderAdding = false;
      this.editingOrderId = null;
      this.resetOrderEditForm();
    },

    handleOrderDelete() {
      if (this.isOrderEditing) {
        alert("Please save or cancel editing first.");
        return;
      }

      if (this.selectedOrderIds.length === 0) {
        alert("Please select at least one row to delete.");
        return;
      }

      const confirmed = window.confirm(
        "Are you sure you want to delete the selected row(s)?"
      );
      if (!confirmed) return;

      this.orderItems = this.orderItems.filter(
        (row) => !this.selectedOrderIds.includes(row.id)
      );
      this.selectedOrderIds = [];
    },

    handleOrderReset() {
      this.orderSearchOrderId = "";
      this.orderSearchOrderDept = "";
      this.orderSearchOrderName = "";
      this.selectedOrderIds = [];
      this.orderCurrentPage = 1;
      this.handleOrderCancelEdit();
    },

    toggleSelectAllOrderCurrentPage(event) {
      if (this.isOrderEditing) return;

      const currentPageIds = this.paginatedOrderItems
        .filter((item) => this.getOrderStatus(item) !== "Complete")
        .map((item) => item.id);

      if (event.target.checked) {
        const merged = [...this.selectedOrderIds, ...currentPageIds];
        this.selectedOrderIds = [...new Set(merged)];
      } else {
        this.selectedOrderIds = this.selectedOrderIds.filter(
          (id) => !currentPageIds.includes(id)
        );
      }
    },

    changeOrderQty(id, delta) {
      const index = this.orderItems.findIndex((item) => item.id === id);
      if (index === -1) return;

      if (this.getOrderStatus(this.orderItems[index]) === "Complete") return;

      const currentQty = Number(this.orderItems[index].orderQty) || 0;
      const newQty = currentQty + delta;

      this.orderItems[index].orderQty = newQty < 0 ? 0 : newQty;
      this.orderItems = [...this.orderItems];
    },

    openOrderCopyModal() {
      if (this.isOrderEditing) {
        alert("Please save or cancel editing first.");
        return;
      }

      if (this.selectedOrderIds.length !== 1) {
        alert("Please select exactly one order row to copy.");
        return;
      }

      const selectedId = this.selectedOrderIds[0];
      const source = this.orderItems.find((item) => item.id === selectedId);

      if (!source) return;

      this.copyOrderForm = {
        orderId: `ORD${String(this.orderItems.length + 1).padStart(3, "0")}`,
        orderDept: source.orderDept,
        orderName: source.orderName,
        orderFor: source.orderFor,
        orderQty: source.orderQty,
        getQty: source.getQty,
        usingQty: source.usingQty,
        note: source.note,
      };

      this.showOrderCopyModal = true;
    },

    closeOrderCopyModal() {
      this.showOrderCopyModal = false;
    },

    confirmOrderCopy() {
      const newItem = {
        id: Date.now(),
        orderId: this.copyOrderForm.orderId,
        orderDept: this.copyOrderForm.orderDept,
        orderName: this.copyOrderForm.orderName,
        orderFor: this.copyOrderForm.orderFor,
        orderQty: Number(this.copyOrderForm.orderQty),
        getQty: Number(this.copyOrderForm.getQty),
        usingQty: Number(this.copyOrderForm.usingQty),
        note: this.copyOrderForm.note,
      };

      this.orderItems.unshift(newItem);
      this.selectedOrderIds = [];
      this.orderCurrentPage = 1;
      this.closeOrderCopyModal();
    },

    changePurchasePage(page) {
      if (page < 1 || page > this.purchaseTotalPages) return;
      this.purchaseCurrentPage = page;
    },

    resetPurchaseEditForm() {
      this.purchaseEditForm = {
        purchaseMaterial: "",
        purchaseQty: "",
        deliverQuantity: "",
        note: "",
      };
    },

    handlePurchaseSearch() {
      this.purchaseCurrentPage = 1;
    },

    handlePurchaseAdd() {
      if (this.isPurchaseEditing) {
        alert("Please save or cancel the current editing first.");
        return;
      }

      this.isPurchaseAdding = true;
      this.editingPurchaseId = null;
      this.selectedPurchaseIds = [];
      this.resetPurchaseEditForm();
      this.purchaseCurrentPage = 1;
    },

    handlePurchaseEdit() {
      if (this.isPurchaseAdding) {
        alert("Please save or cancel the new row first.");
        return;
      }

      if (this.selectedPurchaseIds.length === 0) {
        alert("Please select one row to edit.");
        return;
      }

      if (this.selectedPurchaseIds.length > 1) {
        alert("Please select only one row to edit.");
        return;
      }

      const selectedId = this.selectedPurchaseIds[0];
      const item = this.purchaseItems.find((row) => row.id === selectedId);

      if (!item) return;

      this.editingPurchaseId = selectedId;
      this.purchaseEditForm = {
        purchaseMaterial: item.purchaseMaterial,
        purchaseQty: item.purchaseQty,
        deliverQuantity: item.deliverQuantity,
        note: item.note,
      };
    },

    handlePurchaseSave() {
      if (this.isPurchaseAdding) {
        if (
          !this.purchaseEditForm.purchaseMaterial ||
          this.purchaseEditForm.purchaseQty === "" ||
          this.purchaseEditForm.deliverQuantity === ""
        ) {
          alert("Please fill all required fields before saving.");
          return;
        }

        const newItem = {
          id: Date.now(),
          purchaseId: `PUR${String(this.purchaseItems.length + 1).padStart(3, "0")}`,
          purchaseMaterial: this.purchaseEditForm.purchaseMaterial,
          purchaseQty: Number(this.purchaseEditForm.purchaseQty),
          deliverQuantity: Number(this.purchaseEditForm.deliverQuantity),
          note: this.purchaseEditForm.note,
        };

        this.purchaseItems.unshift(newItem);
        this.isPurchaseAdding = false;
        this.resetPurchaseEditForm();
        return;
      }

      if (!this.editingPurchaseId) return;

      const index = this.purchaseItems.findIndex(
        (row) => row.id === this.editingPurchaseId
      );
      if (index === -1) return;

      this.purchaseItems[index] = {
        ...this.purchaseItems[index],
        purchaseMaterial: this.purchaseEditForm.purchaseMaterial,
        purchaseQty: Number(this.purchaseEditForm.purchaseQty),
        deliverQuantity: Number(this.purchaseEditForm.deliverQuantity),
        note: this.purchaseEditForm.note,
      };

      this.purchaseItems = [...this.purchaseItems];
      this.editingPurchaseId = null;
      this.resetPurchaseEditForm();
    },

    handlePurchaseCancelEdit() {
      this.isPurchaseAdding = false;
      this.editingPurchaseId = null;
      this.resetPurchaseEditForm();
    },

    handlePurchaseDelete() {
      if (this.isPurchaseEditing) {
        alert("Please save or cancel editing first.");
        return;
      }

      if (this.selectedPurchaseIds.length === 0) {
        alert("Please select at least one row to delete.");
        return;
      }

      const confirmed = window.confirm(
        "Are you sure you want to delete the selected row(s)?"
      );
      if (!confirmed) return;

      this.purchaseItems = this.purchaseItems.filter(
        (row) => !this.selectedPurchaseIds.includes(row.id)
      );
      this.selectedPurchaseIds = [];
    },

    handlePurchaseReset() {
      this.purchaseSearchPurchaseId = "";
      this.purchaseSearchMaterial = "";
      this.selectedPurchaseIds = [];
      this.purchaseCurrentPage = 1;
      this.handlePurchaseCancelEdit();
    },

    toggleSelectAllPurchaseCurrentPage(event) {
      if (this.isPurchaseEditing) return;

      const currentPageIds = this.paginatedPurchaseItems.map((item) => item.id);

      if (event.target.checked) {
        const merged = [...this.selectedPurchaseIds, ...currentPageIds];
        this.selectedPurchaseIds = [...new Set(merged)];
      } else {
        this.selectedPurchaseIds = this.selectedPurchaseIds.filter(
          (id) => !currentPageIds.includes(id)
        );
      }
    },

    handlePurchaseCopy() {
      if (this.isPurchaseEditing) {
        alert("Please save or cancel editing first.");
        return;
      }

      if (this.selectedPurchaseIds.length !== 1) {
        alert("Please select exactly one purchase row to copy.");
        return;
      }

      const selectedId = this.selectedPurchaseIds[0];
      const source = this.purchaseItems.find((item) => item.id === selectedId);

      if (!source) return;

      const newItem = {
        ...source,
        id: Date.now(),
        purchaseId: `PUR${String(this.purchaseItems.length + 1).padStart(3, "0")}`,
      };

      this.purchaseItems.unshift(newItem);
      this.selectedPurchaseIds = [];
      this.purchaseCurrentPage = 1;
    },
  },
};
</script>

<style scoped>
.equip-page {
  width: 100%;
  min-width: 0;
}

.tab-top-bar {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.tab-buttons {
  display: flex;
  gap: 10px;
}

.tab-btn {
  height: 44px;
  padding: 0 20px;
  border: none;
  border-radius: 12px;
  background: #e5e7eb;
  cursor: pointer;
  font-size: 14px;
}

.tab-btn.active {
  background: #3b82f6;
  color: white;
}

.top-bar {
  width: 100%;
  margin-bottom: 24px;
}

.search-group {
  display: flex;
  gap: 12px;
  flex-wrap: nowrap;
  align-items: center;
  overflow-x: auto;
  overflow-y: hidden;
  padding-bottom: 6px;
}

.button-group {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  align-items: center;
  margin-top: 14px;
}

.search-group::-webkit-scrollbar,
.table-wrapper::-webkit-scrollbar {
  height: 8px;
}

.search-group::-webkit-scrollbar-thumb,
.table-wrapper::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 6px;
}

.search-input {
  width: 220px;
  min-width: 220px;
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
  flex-shrink: 0;
}

.add-btn {
  background: #10b981;
}

.search-btn {
  background: #3b82f6;
}

.copy-btn {
  background: #8b5cf6;
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
  overflow-y: hidden;
  background: #ffffff;
  border-radius: 14px;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.06);
  display: block;
}

.equip-table {
  border-collapse: collapse;
  min-width: 1200px;
  width: 100%;
}

.equip-table thead {
  background: #f3f4f6;
}

.equip-table th,
.equip-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #e5e7eb;
  font-size: 14px;
  vertical-align: middle;
  white-space: nowrap;
}

.equip-table th {
  color: #111827;
  font-weight: 600;
}

.equip-table td {
  color: #374151;
}

.equip-table tbody tr:hover {
  background: #f9fafb;
}

.checkbox-col {
  width: 50px;
  min-width: 50px;
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

.completed-row {
  opacity: 0.7;
}

.qty-control {
  display: flex;
  align-items: center;
  gap: 10px;
}

.qty-btn {
  width: 28px;
  height: 28px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  color: white;
  font-weight: bold;
}

.qty-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.minus-btn {
  background: #ef4444;
}

.plus-btn {
  background: #10b981;
}

.qty-value {
  min-width: 24px;
  text-align: center;
  display: inline-block;
}

.status-badge {
  padding: 6px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
}

.status-complete {
  background: #dcfce7;
  color: #166534;
}

.status-partial {
  background: #fef3c7;
  color: #92400e;
}

.status-incomplete {
  background: #fee2e2;
  color: #991b1b;
}

.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.modal-box {
  width: 700px;
  max-width: 95%;
  background: white;
  border-radius: 16px;
  padding: 24px;
}

.modal-box h3 {
  margin-top: 0;
  margin-bottom: 18px;
}

.modal-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
}

.modal-field label {
  display: block;
  margin-bottom: 6px;
  font-size: 14px;
}

.modal-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
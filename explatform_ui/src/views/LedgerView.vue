<template>
  <div class="ledger-page">

    <!-- SEARCH -->
    <div class="top-bar">

      <div class="search-group">
        <input v-model="searchMaterialCode" type="text" placeholder="Search by Material Code" class="search-input" clearable/>
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

        <button v-if="!isEditing" class="action-btn edit-btn" @click="handleEdit">
          Edit
        </button>

        <button v-if="isEditing" class="action-btn save-btn" @click="handleSave">
          Save
        </button>

        <button v-if="isEditing" class="action-btn cancel-btn" @click="handleCancelEdit">
          Cancel
        </button>

        <button class="action-btn delete-btn" @click="handleDelete">Delete</button>
        <button class="action-btn reset-btn" @click="handleReset">Reset</button>
      </div>

    </div>


    <!-- TABLE -->
    <div class="table-wrapper">

      <table class="ledger-table">

        <thead>
          <tr>
            <th class="checkbox-col">
              <input type="checkbox"
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

          <!-- ADD ROW -->
          <tr v-if="isAdding" class="editing-row">

            <td></td>

            <td><input v-model="editForm.materialName" class="table-edit-input"/></td>
            <td><input v-model="editForm.materialCode" class="table-edit-input"/></td>
            <td><input v-model="editForm.deviceCode" class="table-edit-input"/></td>
            <td><input v-model="editForm.deviceModel" class="table-edit-input"/></td>
            <td><input v-model="editForm.bookedForEmployee" class="table-edit-input"/></td>
            <td><input v-model="editForm.employeeId" class="table-edit-input"/></td>
            <td><input v-model="editForm.bookedForDepartment" class="table-edit-input"/></td>
            <td><input v-model="editForm.departmentId" class="table-edit-input"/></td>
            <td><input v-model="editForm.departmentCode" class="table-edit-input"/></td>
            <td><input v-model="editForm.bookedQty" class="table-edit-input"/></td>
            <td><input v-model="editForm.bookingDate" type="date" class="table-edit-input"/></td>
            <td><input v-model="editForm.purposeOfUse" class="table-edit-input"/></td>
            <td><input v-model="editForm.note" class="table-edit-input"/></td>

          </tr>


          <!-- DATA ROW -->
          <tr v-for="item in paginatedLedgerItems"
              :key="item.id"
              :class="{ 'editing-row': editingLedgerId === item.id }">

            <td class="checkbox-col">
              <input type="checkbox"
                :value="item.id"
                v-model="selectedLedgerIds"
              />
            </td>


            <template v-if="editingLedgerId === item.id">

              <td><input v-model="editForm.materialName" class="table-edit-input"/></td>
              <td><input v-model="editForm.materialCode" class="table-edit-input"/></td>
              <td><input v-model="editForm.deviceCode" class="table-edit-input"/></td>
              <td><input v-model="editForm.deviceModel" class="table-edit-input"/></td>
              <td><input v-model="editForm.bookedForEmployee" class="table-edit-input"/></td>
              <td><input v-model="editForm.employeeId" class="table-edit-input"/></td>
              <td><input v-model="editForm.bookedForDepartment" class="table-edit-input"/></td>
              <td><input v-model="editForm.departmentId" class="table-edit-input"/></td>
              <td><input v-model="editForm.departmentCode" class="table-edit-input"/></td>
              <td><input v-model="editForm.bookedQty" class="table-edit-input"/></td>
              <td><input v-model="editForm.bookingDate" type="date" class="table-edit-input"/></td>
              <td><input v-model="editForm.purposeOfUse" class="table-edit-input"/></td>
              <td><input v-model="editForm.note" class="table-edit-input"/></td>

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

import AppPagination from "../components/AppPagination.vue";

export default {

  name: "LedgerView",

  components:{
    AppPagination
  },

  data(){

    return{

      searchMaterialCode:"",
      searchDeviceCode:"",
      searchEmployeeId:"",
      searchDepartmentId:"",
      searchStartDate:"",
      searchEndDate:"",

      currentPage:1,
      itemsPerPage:5,

      selectedLedgerIds:[],

      editingLedgerId:null,
      isAdding:false,

      editForm:{
        materialName:"",
        materialCode:"",
        deviceCode:"",
        deviceModel:"",
        bookedForEmployee:"",
        employeeId:"",
        bookedForDepartment:"",
        departmentId:"",
        departmentCode:"",
        bookedQty:"",
        bookingDate:"",
        purposeOfUse:"",
        note:""
      },

      ledgerItems:[
        {
          id:1,
          materialName:"Laptop Charger",
          materialCode:"MAT-001",
          deviceCode:"DEV-101",
          deviceModel:"Dell Latitude",
          bookedForEmployee:"John Smith",
          employeeId:"EMP001",
          bookedForDepartment:"IT",
          departmentId:"DEP001",
          departmentCode:"D-01",
          bookedQty:"2",
          bookingDate:"2024-01-10",
          purposeOfUse:"Office setup",
          note:"Urgent"
        },{
          id:1,
          materialName:"Laptop Charger",
          materialCode:"MAT-001",
          deviceCode:"DEV-101",
          deviceModel:"Dell Latitude",
          bookedForEmployee:"John Smith",
          employeeId:"EMP001",
          bookedForDepartment:"IT",
          departmentId:"DEP001",
          departmentCode:"D-01",
          bookedQty:"2",
          bookingDate:"2024-01-10",
          purposeOfUse:"Office setup",
          note:"Urgent"
        },
        {
          id:1,
          materialName:"Laptop Charger",
          materialCode:"MAT-001",
          deviceCode:"DEV-101",
          deviceModel:"Dell Latitude",
          bookedForEmployee:"John Smith",
          employeeId:"EMP001",
          bookedForDepartment:"IT",
          departmentId:"DEP001",
          departmentCode:"D-01",
          bookedQty:"2",
          bookingDate:"2024-01-10",
          purposeOfUse:"Office setup",
          note:"Urgent"
        },
        {
          id:1,
          materialName:"Laptop Charger",
          materialCode:"MAT-001",
          deviceCode:"DEV-101",
          deviceModel:"Dell Latitude",
          bookedForEmployee:"John Smith",
          employeeId:"EMP001",
          bookedForDepartment:"IT",
          departmentId:"DEP001",
          departmentCode:"D-01",
          bookedQty:"2",
          bookingDate:"2024-01-10",
          purposeOfUse:"Office setup",
          note:"Urgent"
        },{
          id:1,
          materialName:"Laptop Charger",
          materialCode:"MAT-001",
          deviceCode:"DEV-101",
          deviceModel:"Dell Latitude",
          bookedForEmployee:"John Smith",
          employeeId:"EMP001",
          bookedForDepartment:"IT",
          departmentId:"DEP001",
          departmentCode:"D-01",
          bookedQty:"2",
          bookingDate:"2024-01-10",
          purposeOfUse:"Office setup",
          note:"Urgent"
        },
        {
          id:1,
          materialName:"Laptop Charger",
          materialCode:"MAT-001",
          deviceCode:"DEV-101",
          deviceModel:"Dell Latitude",
          bookedForEmployee:"John Smith",
          employeeId:"EMP001",
          bookedForDepartment:"IT",
          departmentId:"DEP001",
          departmentCode:"D-01",
          bookedQty:"2",
          bookingDate:"2024-01-10",
          purposeOfUse:"Office setup",
          note:"Urgent"
        },
        {
          id:1,
          materialName:"Laptop Charger",
          materialCode:"MAT-001",
          deviceCode:"DEV-101",
          deviceModel:"Dell Latitude",
          bookedForEmployee:"John Smith",
          employeeId:"EMP001",
          bookedForDepartment:"IT",
          departmentId:"DEP001",
          departmentCode:"D-01",
          bookedQty:"2",
          bookingDate:"2024-01-10",
          purposeOfUse:"Office setup",
          note:"Urgent"
        },
        {
          id:1,
          materialName:"Laptop Charger",
          materialCode:"MAT-001",
          deviceCode:"DEV-101",
          deviceModel:"Dell Latitude",
          bookedForEmployee:"John Smith",
          employeeId:"EMP001",
          bookedForDepartment:"IT",
          departmentId:"DEP001",
          departmentCode:"D-01",
          bookedQty:"2",
          bookingDate:"2024-01-10",
          purposeOfUse:"Office setup",
          note:"Urgent"
        },
        {
          id:1,
          materialName:"Laptop Charger",
          materialCode:"MAT-001",
          deviceCode:"DEV-101",
          deviceModel:"Dell Latitude",
          bookedForEmployee:"John Smith",
          employeeId:"EMP001",
          bookedForDepartment:"IT",
          departmentId:"DEP001",
          departmentCode:"D-01",
          bookedQty:"2",
          bookingDate:"2024-01-10",
          purposeOfUse:"Office setup",
          note:"Urgent"
        },
        {
          id:1,
          materialName:"Laptop Charger",
          materialCode:"MAT-001",
          deviceCode:"DEV-101",
          deviceModel:"Dell Latitude",
          bookedForEmployee:"John Smith",
          employeeId:"EMP001",
          bookedForDepartment:"IT",
          departmentId:"DEP001",
          departmentCode:"D-01",
          bookedQty:"2",
          bookingDate:"2024-01-10",
          purposeOfUse:"Office setup",
          note:"Urgent"
        },
        {
          id:1,
          materialName:"Laptop Charger",
          materialCode:"MAT-001",
          deviceCode:"DEV-101",
          deviceModel:"Dell Latitude",
          bookedForEmployee:"John Smith",
          employeeId:"EMP001",
          bookedForDepartment:"IT",
          departmentId:"DEP001",
          departmentCode:"D-01",
          bookedQty:"2",
          bookingDate:"2024-01-10",
          purposeOfUse:"Office setup",
          note:"Urgent"
        }
      ]

    }

  },


  computed:{

    filteredLedgerItems(){

      return this.ledgerItems.filter(item=>{

        return(

          item.materialCode.toLowerCase().includes(this.searchMaterialCode.toLowerCase()) &&
          item.deviceCode.toLowerCase().includes(this.searchDeviceCode.toLowerCase()) &&
          item.employeeId.toLowerCase().includes(this.searchEmployeeId.toLowerCase()) &&
          item.departmentId.toLowerCase().includes(this.searchDepartmentId.toLowerCase())

        )

      })

    },


    totalPages(){
      return Math.ceil(this.filteredLedgerItems.length/this.itemsPerPage) || 1
    },


    paginatedLedgerItems(){

      const start=(this.currentPage-1)*this.itemsPerPage
      const end=start+this.itemsPerPage

      return this.filteredLedgerItems.slice(start,end)

    },


    isEditing(){
      return this.editingLedgerId!==null || this.isAdding
    },


    isAllCurrentPageSelected(){

      if(this.paginatedLedgerItems.length===0) return false

      return this.paginatedLedgerItems.every(item =>
        this.selectedLedgerIds.includes(item.id)
      )

    }

  },


  methods:{

    changePage(page){

      if(page<1 || page>this.totalPages) return

      this.currentPage=page

    },


    handleSearch(){
      this.currentPage=1
    },


    handleAdd(){

      if(this.isEditing){
        alert("Finish editing first")
        return
      }

      this.isAdding=true

      this.editForm={}

    },


    handleEdit(){

      if(this.selectedLedgerIds.length!==1){

        alert("Select one row")

        return
      }

      const id=this.selectedLedgerIds[0]

      const row=this.ledgerItems.find(i=>i.id===id)

      this.editingLedgerId=id

      this.editForm={...row}

    },


    handleSave(){

      if(this.isAdding){

        this.ledgerItems.unshift({
          id:Date.now(),
          ...this.editForm
        })

        this.isAdding=false

        return

      }


      const index=this.ledgerItems.findIndex(i=>i.id===this.editingLedgerId)

      this.ledgerItems[index]={

        ...this.ledgerItems[index],
        ...this.editForm

      }

      this.editingLedgerId=null

    },


    handleCancelEdit(){

      this.isAdding=false
      this.editingLedgerId=null

    },


    handleDelete(){

      this.ledgerItems=this.ledgerItems.filter(
        item=>!this.selectedLedgerIds.includes(item.id)
      )

      this.selectedLedgerIds=[]

    },


    handleReset(){

      this.searchMaterialCode=""
      this.searchDeviceCode=""
      this.searchEmployeeId=""
      this.searchDepartmentId=""
      this.searchStartDate=""
      this.searchEndDate=""

    },


    toggleSelectAllCurrentPage(event){

      const ids=this.paginatedLedgerItems.map(i=>i.id)

      if(event.target.checked){

        this.selectedLedgerIds=[...new Set([...this.selectedLedgerIds,...ids])]

      }
      else{

        this.selectedLedgerIds=this.selectedLedgerIds.filter(
          id=>!ids.includes(id)
        )

      }

    }

  }

}
</script>


<style scoped>

.top-bar{
margin-bottom:20px;
}

.search-group{
display:flex;
gap:10px;
overflow-x:auto;
}

.button-group{
margin-top:12px;
display:flex;
gap:10px;
flex-wrap:wrap;
}

.search-input{
width:200px;
height:40px;
padding:0 10px;
border-radius:8px;
border:1px solid #ccc;
}

.action-btn{
height:40px;
padding:0 16px;
border:none;
border-radius:8px;
color:white;
cursor:pointer;
}

.add-btn{background:#10b981}
.search-btn{background:#3b82f6}
.edit-btn{background:#f59e0b}
.save-btn{background:#10b981}
.cancel-btn{background:#6b7280}
.delete-btn{background:#ef4444}
.reset-btn{background:#6b7280}

.table-wrapper{
overflow-x:auto;
margin-top:20px;
}

.ledger-table{
min-width:1800px;
border-collapse:collapse;
}

.ledger-table th,
.ledger-table td{
padding:12px;
border-bottom:1px solid #eee;
}

.table-edit-input{
width:120px;
height:36px;
}

.editing-row{
background:#fff7ed;
}

</style>
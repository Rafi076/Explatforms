<template>
  <router-view />
</template>

<script>
export default {
  name: "App",
};
</script>

<style>
html,
body,
#app {
  margin: 0;
  padding: 0;
  width: 100%;
  min-height: 100%;
}

body {
  background: #f5f7fb;
  color: #111827;
  transition: background 0.3s ease, color 0.3s ease;
}

body.dark-mode {
  background: #111827 !important;
  color: #f9fafb !important;
}

body.dark-mode .top-navbar {
  background: #1f2937 !important;
  border-bottom: 1px solid #374151 !important;
}

body.dark-mode .sidebar {
  background: #0f172a !important;
  color: #f9fafb !important;
}

body.dark-mode .dashboard-page,
body.dark-mode .dashboard-content {
  background: #111827 !important;
  color: #f9fafb !important;
}

body.dark-mode .dashboard-card {
  background: #1f2937 !important;
  color: #f9fafb !important;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.3) !important;
}

body.dark-mode .dashboard-card h3,
body.dark-mode .dashboard-card p,
body.dark-mode .dashboard-content h1 {
  color: #f9fafb !important;
}

body.dark-mode .nav-btn {
  background: #374151 !important;
  color: #f9fafb !important;
}

body.dark-mode .nav-btn:hover {
  background: #4b5563 !important;
}

body.dark-mode .sub-item {
  background: #1e293b !important;
  color: #f9fafb !important;
}

body.dark-mode .table-wrapper {
  background: #1f2937 !important;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.3) !important;
}

body.dark-mode .employee-table thead {
  background: #374151 !important;
}

body.dark-mode .employee-table th {
  color: #f9fafb !important;
  border-bottom: 1px solid #4b5563 !important;
}

body.dark-mode .employee-table td {
  color: #e5e7eb !important;
  border-bottom: 1px solid #374151 !important;
}

body.dark-mode .employee-table tbody tr:hover {
  background: #273549 !important;
}

body.dark-mode .search-input {
  background: #1f2937 !important;
  color: #f9fafb !important;
  border: 1px solid #4b5563 !important;
}

body.dark-mode .search-input::placeholder {
  color: #cbd5e1 !important;
}

body.dark-mode .page-btn {
  background: #374151 !important;
  color: #f9fafb !important;
}

body.dark-mode .page-btn:hover:not(:disabled) {
  background: #4b5563 !important;
}

body.dark-mode .page-btn.active {
  background: #8dc52b !important;
  color: white !important;
}

body.dark-mode .no-data {
  color: #cbd5e1 !important;
}

body.dark-mode .search-btn {
  background: #2563eb !important;
}

body.dark-mode .edit-btn {
  background: #d97706 !important;
}

body.dark-mode .delete-btn {
  background: #dc2626 !important;
}

body.dark-mode .reset-btn {
  background: #4b5563 !important;
}

body.dark-mode input[type="checkbox"] {
  accent-color: #8dc52b;
}

body.dark-mode .table-edit-input {
  background: #111827 !important;
  color: #f9fafb !important;
  border: 1px solid #4b5563 !important;
}

body.dark-mode .editing-row {
  background: #2b3340 !important;
}

body.dark-mode .save-btn {
  background: #059669 !important;
}

body.dark-mode .cancel-btn {
  background: #4b5563 !important;
}

body.dark-mode .table-edit-input {
  background: #111827 !important;
  color: #f9fafb !important;
  border: 1px solid #4b5563 !important;
}

body.dark-mode .editing-row {
  background: #2b3340 !important;
}

body.dark-mode .save-btn {
  background: #059669 !important;
}

body.dark-mode .cancel-btn {
  background: #4b5563 !important;
}

body.dark-mode .add-btn {
  background: #059669 !important;
}

body.dark-mode .department-table thead {
  background: #374151 !important;
}

body.dark-mode .department-table th {
  color: #f9fafb !important;
  border-bottom: 1px solid #4b5563 !important;
}

body.dark-mode .department-table td {
  color: #e5e7eb !important;
  border-bottom: 1px solid #374151 !important;
}

body.dark-mode .department-table tbody tr:hover {
  background: #273549 !important;
}
body.dark-mode .clickable-link {
  color: #60a5fa !important;
}

body.dark-mode .department-table thead {
  background: #374151 !important;
}

body.dark-mode .department-table th {
  color: #f9fafb !important;
  border-bottom: 1px solid #4b5563 !important;
}

body.dark-mode .department-table td {
  color: #e5e7eb !important;
  border-bottom: 1px solid #374151 !important;
}

body.dark-mode .department-table tbody tr:hover {
  background: #273549 !important;
}

body.dark-mode .back-btn {
  background: #2563eb !important;
}

body.dark-mode .back-btn:hover {
  background: #1d4ed8 !important;
}

body.dark-mode .warehouse-table thead,
body.dark-mode .warehouse-state-table thead {
  background: #374151 !important;
}

body.dark-mode .warehouse-table th,
body.dark-mode .warehouse-state-table th {
  color: #f9fafb !important;
  border-bottom: 1px solid #4b5563 !important;
}

body.dark-mode .warehouse-table td,
body.dark-mode .warehouse-state-table td {
  color: #e5e7eb !important;
  border-bottom: 1px solid #374151 !important;
}

body.dark-mode .warehouse-table tbody tr:hover,
body.dark-mode .warehouse-state-table tbody tr:hover {
  background: #273549 !important;
}

body.dark-mode .clickable-link {
  color: #60a5fa !important;
}

body.dark-mode .back-btn {
  background: #2563eb !important;
}

body.dark-mode .back-btn:hover {
  background: #1d4ed8 !important;
}

body.dark-mode .ledger-table thead {
  background: #374151 !important;
}

body.dark-mode .ledger-table th {
  color: #f9fafb !important;
  border-bottom: 1px solid #4b5563 !important;
}

body.dark-mode .ledger-table td {
  color: #e5e7eb !important;
  border-bottom: 1px solid #374151 !important;
}

body.dark-mode .ledger-table tbody tr:hover {
  background: #273549 !important;
}
</style>
package com.explatform.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ledger")
public class Ledger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String materialName;
    private String materialCode;
    private String deviceCode;
    private String deviceModel;

    private String bookedForEmployee;
    private String employeeId;

    private String bookedForDepartment;
    private String departmentId;
    private String departmentCode;

    private Integer bookedQty;

    private LocalDate bookingDate;

    private String purposeOfUse;
    private String note;

    // ===== Getters & Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public String getMaterialCode() { return materialCode; }
    public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }

    public String getDeviceCode() { return deviceCode; }
    public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode; }

    public String getDeviceModel() { return deviceModel; }
    public void setDeviceModel(String deviceModel) { this.deviceModel = deviceModel; }

    public String getBookedForEmployee() { return bookedForEmployee; }
    public void setBookedForEmployee(String bookedForEmployee) { this.bookedForEmployee = bookedForEmployee; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getBookedForDepartment() { return bookedForDepartment; }
    public void setBookedForDepartment(String bookedForDepartment) { this.bookedForDepartment = bookedForDepartment; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public String getDepartmentCode() { return departmentCode; }
    public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }

    public Integer getBookedQty() { return bookedQty; }
    public void setBookedQty(Integer bookedQty) { this.bookedQty = bookedQty; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public String getPurposeOfUse() { return purposeOfUse; }
    public void setPurposeOfUse(String purposeOfUse) { this.purposeOfUse = purposeOfUse; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
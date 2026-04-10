package com.explatform.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "department_work")
public class DepartmentWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "on_process_work")
    private String onProcessWork;

    @Column(name = "process_code")
    private String processCode;

    private String buyer;

    @Column(name = "purchase_date")
    private String purchaseDate;

    @Column(name = "delivery_date")
    private String deliveryDate;

    private String quantity;

    private String details;

    private String note;

    // ✅ GETTERS & SETTERS

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public String getOnProcessWork() { return onProcessWork; }
    public void setOnProcessWork(String onProcessWork) { this.onProcessWork = onProcessWork; }

    public String getProcessCode() { return processCode; }
    public void setProcessCode(String processCode) { this.processCode = processCode; }

    public String getBuyer() { return buyer; }
    public void setBuyer(String buyer) { this.buyer = buyer; }

    public String getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }

    public String getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
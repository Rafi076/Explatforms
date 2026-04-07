package com.explatform.backend.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

    public void setId(int id) {
    }

    // getters & setters
}

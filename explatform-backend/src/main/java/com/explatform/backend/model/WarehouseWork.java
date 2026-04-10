package com.explatform.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "warehouse_work")
public class WarehouseWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String warehouseId;
    private String materialName;
    private String materialId;
    private int purchaseQuantity;
    private String purchaseDate;
    private int availableQty;
    private String buyerName;
    private String note;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }

    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public String getMaterialId() { return materialId; }
    public void setMaterialId(String materialId) { this.materialId = materialId; }

    public int getPurchaseQuantity() { return purchaseQuantity; }
    public void setPurchaseQuantity(int purchaseQuantity) { this.purchaseQuantity = purchaseQuantity; }

    public String getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }

    public int getAvailableQty() { return availableQty; }
    public void setAvailableQty(int availableQty) { this.availableQty = availableQty; }

    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
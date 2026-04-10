package com.explatform.backend.controller;

import com.explatform.backend.model.Warehouse;
import com.explatform.backend.model.WarehouseWork;
import com.explatform.backend.repository.WarehouseRepository;
import com.explatform.backend.repository.WarehouseWorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")   // ✅ IMPORTANT
@CrossOrigin
public class WarehouseController {

    @Autowired
    private WarehouseRepository warehouseRepo;

    @Autowired
    private WarehouseWorkRepository workRepo;

    // 🔹 Get all warehouses
    @GetMapping("/warehouses")
    public List<Warehouse> getWarehouses() {
        return warehouseRepo.findAll();
    }

    // 🔹 Add warehouse
    @PostMapping("/warehouses")
    public Warehouse addWarehouse(@RequestBody Warehouse warehouse) {

        long count = warehouseRepo.count() + 1;
        String generatedId = "WH" + String.format("%03d", count);

        warehouse.setWarehouseId(generatedId);

        return warehouseRepo.save(warehouse);
    }

    // 🔹 Update warehouse
    @PutMapping("/warehouses/{id}")
    public Warehouse updateWarehouse(@PathVariable int id, @RequestBody Warehouse warehouse) {
        warehouse.setId(id);
        return warehouseRepo.save(warehouse);
    }

    // 🔹 Delete warehouse
    @DeleteMapping("/warehouses/{id}")
    public void deleteWarehouse(@PathVariable int id) {
        warehouseRepo.deleteById(id);
    }

    // 🔹 Get works by warehouseId
    @GetMapping("/warehouse-works/{warehouseId}")
    public List<WarehouseWork> getWorks(@PathVariable String warehouseId) {
        return workRepo.findByWarehouseId(warehouseId);
    }

    // 🔹 Add work
    @PostMapping("/warehouse-works")
    public WarehouseWork addWork(@RequestBody WarehouseWork work) {
        System.out.println("SAVING WORK: " + work.getMaterialName());
        return workRepo.save(work);
    }

    // 🔹 Update work
    @PutMapping("/warehouse-works/{id}")
    public WarehouseWork updateWork(@PathVariable int id, @RequestBody WarehouseWork updatedWork) {

        WarehouseWork existing = workRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Work not found"));

        if (updatedWork.getMaterialName() != null)
            existing.setMaterialName(updatedWork.getMaterialName());

        if (updatedWork.getMaterialId() != null)
            existing.setMaterialId(updatedWork.getMaterialId());

        existing.setPurchaseQuantity(updatedWork.getPurchaseQuantity());
        existing.setAvailableQty(updatedWork.getAvailableQty());

        if (updatedWork.getPurchaseDate() != null)
            existing.setPurchaseDate(updatedWork.getPurchaseDate());

        if (updatedWork.getBuyerName() != null)
            existing.setBuyerName(updatedWork.getBuyerName());

        if (updatedWork.getNote() != null)
            existing.setNote(updatedWork.getNote());

        existing.setWarehouseId(updatedWork.getWarehouseId());

        return workRepo.save(existing);
    }

    // 🔹 Delete work
    @DeleteMapping("/warehouse-works/{id}")
    public void deleteWork(@PathVariable int id) {
        workRepo.deleteById(id);
    }
}
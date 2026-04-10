package com.explatform.backend.repository;

import com.explatform.backend.model.WarehouseWork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseWorkRepository extends JpaRepository<WarehouseWork, Integer> {
    List<WarehouseWork> findByWarehouseId(String warehouseId);
}
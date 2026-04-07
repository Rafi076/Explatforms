package com.explatform.backend.repository;

import com.explatform.backend.model.DepartmentWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentWorkRepository extends JpaRepository<DepartmentWork, Integer> {
    List<DepartmentWork> findByDepartmentId(String departmentId);
}
package com.explatform.backend.repository;

import com.explatform.backend.model.DepartmentWork;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DepartmentWorkRepository extends JpaRepository<DepartmentWork, Integer> {

    List<DepartmentWork> findByDepartmentId(String departmentId);
}
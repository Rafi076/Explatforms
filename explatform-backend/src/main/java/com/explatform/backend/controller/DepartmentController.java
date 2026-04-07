package com.explatform.backend.controller;

import com.explatform.backend.model.Department;
import com.explatform.backend.model.DepartmentWork;
import com.explatform.backend.repository.DepartmentRepository;
import com.explatform.backend.repository.DepartmentWorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepo;

    @Autowired
    private DepartmentWorkRepository workRepo;

    // 🔹 Get all departments
    @GetMapping("/departments")
    public List<Department> getDepartments() {
        return departmentRepo.findAll();
    }

    // 🔹 Get works by departmentId
    @GetMapping("/department-works/{departmentId}")
    public List<DepartmentWork> getWorkByDepartment(@PathVariable String departmentId) {
        return workRepo.findByDepartmentId(departmentId);
    }

    @PostMapping("/department-works")
    public DepartmentWork addWork(@RequestBody DepartmentWork work) {
        return workRepo.save(work);
    }

    @PutMapping("/department-works/{id}")
    public DepartmentWork updateWork(@PathVariable int id, @RequestBody DepartmentWork work) {
        work.setId(id);
        return workRepo.save(work);
    }

    @DeleteMapping("/department-works/{id}")
    public void deleteWork(@PathVariable int id) {
        workRepo.deleteById(id);
    }
}
package com.explatform.backend.Service;

import com.explatform.backend.model.Ledger;
import com.explatform.backend.repository.LedgerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LedgerService {

    @Autowired
    private LedgerRepository repository;

    // GET ALL
    public List<Ledger> getAll() {
        return repository.findAll();
    }

    // GET BY ID
    public Ledger getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ledger not found with id: " + id));
    }

    // CREATE
    public Ledger create(Ledger ledger) {
        return repository.save(ledger);
    }

    // UPDATE
    public Ledger update(Long id, Ledger ledger) {
        Ledger existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ledger not found with id: " + id));

        // update fields (safe approach)
        existing.setMaterialName(ledger.getMaterialName());
        existing.setMaterialCode(ledger.getMaterialCode());
        existing.setDeviceCode(ledger.getDeviceCode());
        existing.setDeviceModel(ledger.getDeviceModel());

        existing.setBookedForEmployee(ledger.getBookedForEmployee());
        existing.setEmployeeId(ledger.getEmployeeId());

        existing.setBookedForDepartment(ledger.getBookedForDepartment());
        existing.setDepartmentId(ledger.getDepartmentId());
        existing.setDepartmentCode(ledger.getDepartmentCode());

        existing.setBookedQty(ledger.getBookedQty());
        existing.setBookingDate(ledger.getBookingDate());

        existing.setPurposeOfUse(ledger.getPurposeOfUse());
        existing.setNote(ledger.getNote());

        return repository.save(existing);
    }

    // DELETE
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Ledger not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
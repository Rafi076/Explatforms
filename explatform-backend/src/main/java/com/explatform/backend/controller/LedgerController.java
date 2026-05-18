package com.explatform.backend.controller;

import com.explatform.backend.model.Ledger;
import com.explatform.backend.Service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ledger")
@CrossOrigin(origins = "*")
public class LedgerController {

    @Autowired
    private LedgerService service;

    @GetMapping
    public List<Ledger> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Ledger create(@RequestBody Ledger ledger) {
        return service.create(ledger);
    }

    @PutMapping("/{id}")
    public Ledger update(@PathVariable Long id, @RequestBody Ledger ledger) {
        return service.update(id, ledger);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
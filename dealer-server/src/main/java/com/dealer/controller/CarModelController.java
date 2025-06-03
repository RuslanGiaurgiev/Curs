package com.dealer.controller;

import com.dealer.model.CarModel;
import com.dealer.repository.CarModelRepository;
import com.dealer.repository.BrandRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/models")
public class CarModelController {
    private final CarModelRepository repo;
    private final BrandRepository brands;

    public CarModelController(CarModelRepository repo, BrandRepository brands) {
        this.repo = repo;
        this.brands = brands;
    }

    @GetMapping
    public List<CarModel> list() {
        return repo.findAll();
    }

    @PostMapping
    public CarModel create(@RequestBody CarModel m) {
        m.setId(null);
        m.setBrand(brands.findById(m.getBrand().getId()).orElse(null));
        return repo.save(m);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarModel> update(@PathVariable Long id, @RequestBody CarModel m) {
        return repo.findById(id)
                .map(ex -> {
                    ex.setName(m.getName());
                    ex.setPrice(m.getPrice());
                    ex.setBrand(brands.findById(m.getBrand().getId()).orElse(null));
                    return ResponseEntity.ok(repo.save(ex));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

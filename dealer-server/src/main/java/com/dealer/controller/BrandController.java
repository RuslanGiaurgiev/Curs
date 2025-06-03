package com.dealer.controller;

import com.dealer.model.Brand;
import com.dealer.repository.BrandRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {
    private final BrandRepository repo;

    public BrandController(BrandRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Brand> list() {
        return repo.findAll();
    }

    @PostMapping
    public Brand create(@RequestBody Brand b) {
        b.setId(null);
        return repo.save(b);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> update(@PathVariable Long id, @RequestBody Brand b) {
        return repo.findById(id)
                .map(ex -> {
                    ex.setName(b.getName());
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

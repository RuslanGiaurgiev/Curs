package com.example.carshop.repository;

import com.example.carshop.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByAvailableTrue();
    List<Car> findByBrandContainingIgnoreCase(String brand);
}
package com.example.carshop.service;

import com.example.carshop.model.Car;
import com.example.carshop.model.Purchase;
import com.example.carshop.model.User;
import com.example.carshop.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private CarService carService;

    @Transactional
    public Purchase createPurchase(User user, Long carId) {
        System.out.println("🛒 Создание покупки для пользователя: " + user.getEmail() + ", автомобиль: " + carId);

        Car car = carService.getCarById(carId);
        if (car == null) {
            System.out.println("❌ Автомобиль не найден: " + carId);
            throw new RuntimeException("Автомобиль не найден");
        }

        if (!car.isAvailable()) {
            System.out.println("❌ Автомобиль уже продан: " + carId);
            throw new RuntimeException("Автомобиль уже продан");
        }

        System.out.println("✅ Автомобиль доступен: " + car.getBrand() + " " + car.getModel());

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setCar(car);
        purchase.setPurchaseDate(java.time.LocalDateTime.now());
        purchase.setTotalPrice(car.getPrice());
        purchase.setStatus("completed");

        // Помечаем автомобиль как проданный
        car.setAvailable(false);
        carService.saveCar(car);
        System.out.println("🚗 Автомобиль помечен как проданный: " + carId);

        Purchase savedPurchase = purchaseRepository.save(purchase);
        System.out.println("💾 Покупка сохранена в БД с ID: " + savedPurchase.getId());

        return savedPurchase;
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public List<Purchase> getPurchasesByUserId(Long userId) {
        return purchaseRepository.findByUserId(userId);
    }

    public List<Purchase> getPurchasesByUserEmail(String email) {
        return purchaseRepository.findByUserEmail(email);
    }

    public void deletePurchase(Long id) {
        purchaseRepository.deleteById(id);
    }
}
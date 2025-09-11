package com.example.carshop.controller;

import com.example.carshop.model.Car;
import com.example.carshop.model.User;
import com.example.carshop.service.CarService;
import com.example.carshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showCars(Principal principal, Model model) {
        // Проверяем аутентификацию
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            if (user != null) {
                model.addAttribute("currentUser", user);
                System.out.println("User authenticated: " + user.getEmail());
            }
        }

        model.addAttribute("cars", carService.getAvailableCars());
        return "cars";
    }

    // Страница добавления автомобиля
    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddCarForm(Model model) {
        model.addAttribute("car", new Car());
        return "admin/add-car";
    }

    // Обработка добавления автомобиля - ИСПРАВЛЕННЫЙ МЕТОД
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addCar(@RequestParam String brand,
                         @RequestParam String modelName, // Изменил имя параметра
                         @RequestParam int year,
                         @RequestParam String color,
                         @RequestParam BigDecimal price) {

        // Валидация данных
        if (brand == null || brand.trim().isEmpty()) {
            return "redirect:/cars/add?error=Укажите марку автомобиля";
        }
        if (modelName == null || modelName.trim().isEmpty()) {
            return "redirect:/cars/add?error=Укажите модель автомобиля";
        }
        if (year < 1900 || year > java.time.Year.now().getValue()) {
            return "redirect:/cars/add?error=Некорректный год выпуска";
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            return "redirect:/cars/add?error=Некорректная цена";
        }

        Car car = new Car(brand.trim(), modelName.trim(), year, color.trim(), price);
        carService.saveCar(car);

        return "redirect:/cars?success=Автомобиль добавлен";
    }

    // Страница управления автомобилями
    @GetMapping("/manage")
    @PreAuthorize("hasRole('ADMIN')")
    public String manageCars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "admin/manage-cars";
    }

    // Удаление автомобиля
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCar(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        if (car != null && !car.isAvailable()) {
            return "redirect:/cars/manage?error=Нельзя удалить проданный автомобиль";
        }
        carService.deleteCar(id);
        return "redirect:/cars/manage?success=Автомобиль удален";
    }

    // Изменение статуса автомобиля
    @PostMapping("/toggle/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String toggleCarStatus(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        if (car != null) {
            car.setAvailable(!car.isAvailable());
            carService.saveCar(car);
        }
        return "redirect:/cars/manage";
    }
}
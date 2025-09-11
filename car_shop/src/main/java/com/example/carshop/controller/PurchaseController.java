package com.example.carshop.controller;

import com.example.carshop.model.User;
import com.example.carshop.service.PurchaseService;
import com.example.carshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UserService userService;

    @GetMapping("/buy/{carId}")
    @PreAuthorize("hasRole('USER')")
    public String showBuyForm(@PathVariable Long carId, Principal principal, Model model) {
        System.out.println("🛒 Показ формы покупки для автомобиля ID: " + carId);

        if (principal == null) {
            System.out.println("❌ Пользователь не аутентифицирован");
            return "redirect:/auth/login";
        }

        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            System.out.println("❌ Пользователь не найден в базе данных");
            return "redirect:/auth/login";
        }

        System.out.println("✅ Пользователь найден: " + user.getEmail() + " (ID: " + user.getId() + ")");

        model.addAttribute("carId", carId);
        model.addAttribute("user", user);

        System.out.println("📋 Возвращаем шаблон: buy-car.html");
        return "buy-car";
    }

    @PostMapping("/buy")
    @PreAuthorize("hasRole('USER')")
    public String buyCar(@RequestParam Long carId, Principal principal, Model model) {
        System.out.println("🚗 Обработка покупки автомобиля ID: " + carId);

        if (principal == null) {
            System.out.println("❌ Пользователь не аутентифицирован при покупке");
            return "redirect:/auth/login";
        }

        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            System.out.println("❌ Пользователь не найден при покупке");
            return "redirect:/auth/login";
        }

        System.out.println("👤 Покупка для пользователя: " + user.getEmail());

        try {
            purchaseService.createPurchase(user, carId);
            System.out.println("✅ Покупка успешно создана");
            return "redirect:/purchases/success";
        } catch (Exception e) {
            System.out.println("❌ Ошибка при покупке: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "redirect:/cars?error=" + e.getMessage();
        }
    }

    @GetMapping("/success")
    @PreAuthorize("hasRole('USER')")
    public String showSuccess(Principal principal, Model model) {
        System.out.println("🎉 Показ страницы успешной покупки");

        if (principal == null) {
            return "redirect:/auth/login";
        }

        User user = userService.findByEmail(principal.getName());
        if (user != null) {
            var purchases = purchaseService.getPurchasesByUserId(user.getId());
            if (!purchases.isEmpty()) {
                model.addAttribute("lastPurchase", purchases.get(purchases.size() - 1));
                System.out.println("📦 Последняя покупка: " + purchases.get(purchases.size() - 1).getId());
            }
        }

        System.out.println("📋 Возвращаем шаблон: purchase-success.html");
        return "purchase-success";
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public String myPurchases(Principal principal, Model model) {
        System.out.println("📋 Показ истории покупок");

        if (principal == null) {
            System.out.println("❌ Пользователь не аутентифицирован при просмотре истории");
            return "redirect:/auth/login";
        }

        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            System.out.println("❌ Пользователь не найден при просмотре истории");
            return "redirect:/auth/login";
        }

        System.out.println("👤 История покупок для пользователя: " + user.getEmail());

        var purchases = purchaseService.getPurchasesByUserId(user.getId());
        model.addAttribute("purchases", purchases);
        model.addAttribute("totalPurchases", purchases.size());

        System.out.println("📊 Найдено покупок: " + purchases.size());
        System.out.println("📋 Возвращаем шаблон: user/purchases.html");

        return "user/purchases";
    }
}
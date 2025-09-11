package com.example.carshop.controller;

import com.example.carshop.service.PurchaseService;
import com.example.carshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @PostMapping("/users/create-admin")
    public String createAdmin(@RequestParam String username,
                              @RequestParam String email,
                              @RequestParam String password) {
        userService.createAdmin(username, email, password);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/update-role/{id}")
    public String updateUserRole(@PathVariable Long id,
                                 @RequestParam String role) {
        userService.updateUserRole(id, role);
        return "redirect:/admin/users";
    }

    @GetMapping("/purchases")
    public String allPurchases(Model model) {
        model.addAttribute("purchases", purchaseService.getAllPurchases());
        return "admin/all-purchases";
    }

    @PostMapping("/purchases/delete/{id}")
    public String deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return "redirect:/admin/purchases";
    }
}
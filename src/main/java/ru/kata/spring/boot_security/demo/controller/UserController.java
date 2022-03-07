package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping
    public String redirectUnmapped() {
        return "redirect:/user";
    }

    @GetMapping("user")
    public String user(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "user";
    }

    @GetMapping("admin")
    public String index(Model model) {
        model.addAttribute("users", userService.get());
        return "admin";
    }

    @GetMapping("admin/user/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping("admin/user")
    public String create(@ModelAttribute("user") User user) {
        userService.create(user);
        return "redirect:/admin";
    }

    @GetMapping("admin/user/{id}/edit")
    public String edit(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.get(id));
        return "edit";
    }

    @PatchMapping("admin/user/{id}")
    public String update(@PathVariable("id") long id, @ModelAttribute("user") User user) {
        userService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("admin/user/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.remove(id);
        return "redirect:/admin";
    }
}

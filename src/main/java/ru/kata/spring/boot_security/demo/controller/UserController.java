package ru.kata.spring.boot_security.demo.controller;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("allUsers", userService.get());
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", roleService.get());
        return "index";
    }

    @PostMapping("user")
    public String create(@ModelAttribute("user") User user) {
        userService.create(user);
        return "redirect:/";
    }

    @PatchMapping("user/{id}")
    public String update(@PathVariable("id") long id, @ModelAttribute("user") User user) {
        userService.update(id, user);
        return "redirect:/";
    }

    @DeleteMapping("user/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.remove(id);
        return "redirect:/";
    }
}

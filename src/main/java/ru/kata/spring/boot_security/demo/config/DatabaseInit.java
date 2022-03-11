package ru.kata.spring.boot_security.demo.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;

/**
 * Populates DB with roles and users.
 */
@Component
public class DatabaseInit implements InitializingBean {
    private final UserService userService;
    private final RoleService roleService;

    private final static String admEmail = "admin@mail.ru";
    private final static String usrEmail = "user@mail.ru";

    @Autowired
    public DatabaseInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void afterPropertiesSet() {
        List<Role> dbRoles = roleService.get();
        List<User> dbUsers = userService.get();

        if (dbRoles.stream().noneMatch(r -> r.getName().equals("ADMIN"))) {
            Role adminRole = new Role("ADMIN");
            roleService.create(adminRole);
        }

        if (dbRoles.stream().noneMatch(r -> r.getName().equals("USER"))) {
            Role userRole = new Role("USER");
            roleService.create(userRole);
        }

        dbRoles = roleService.get();

        Role adminRole = (Role) dbRoles.stream().filter(r -> r.getName().equals("ADMIN")).toArray()[0];
        Role userRole = (Role) dbRoles.stream().filter(r -> r.getName().equals("USER")).toArray()[0];

        if (dbUsers.stream().noneMatch(r -> r.getUsername().equals(admEmail))) {
            User adminUser = new User(admEmail, "admin", "Иван", "Иванов", Set.of(adminRole, userRole));
            userService.create(adminUser);
        }

        if (dbUsers.stream().noneMatch(r -> r.getUsername().equals(usrEmail))) {
            User regularUser = new User(usrEmail, "user", "Марья", "Марьева", Set.of(userRole));
            userService.create(regularUser);
        }
    }
}
package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao dao;
    private final PasswordEncoder encoder;

    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserDao dao, PasswordEncoder encoder, RoleService roleService) {
        this.dao = dao;
        this.encoder = encoder;
        this.roleService = roleService;
    }

    @Override
    public List<User> get() {
        return dao.get();
    }

    @Override
    public User get(long id) {
        return dao.get(id);
    }

    @Override
    public User get(String email) {
        return dao.get(email);
    }

    @Override
    @Transactional
    public void create(User user) {
        // Encode password (it's surely raw cause user is new).
        user.setPassword(encoder.encode(user.getPassword()));

        // Set specified roles or USER role.
        if (user.getAuthorities() != null && user.getAuthorities().size() != 0) {
            user.setRoles(
                user.getAuthorities().stream()
                    .map(a -> (Role) a)
                    .map(r -> roleService.get(r.getName()))
                    .collect(Collectors.toSet()));
        } else {
            user.setRoles(Set.of(roleService.get("USER")));
        }

        dao.create(user);
    }

    @Override
    @Transactional
    public void remove(long id) {
        dao.remove(id);
    }

    @Override
    @Transactional
    public void update(long id, User user) {
        // Encode password if it was changed (means it's raw).
        String dbPass = get(id).getPassword();
        if (user.getPassword().equals("")) {
            user.setPassword(dbPass);
        } else {
            if (!user.getPassword().equals(dbPass)) {
                user.setPassword(encoder.encode(user.getPassword()));
            }
        }

        // Set specified roles or roles from DB.
        if (user.getAuthorities() != null && user.getAuthorities().size() != 0) {
            user.setRoles(
                user.getAuthorities().stream()
                    .map(a -> (Role) a)
                    .map(r -> roleService.get(r.getName()))
                    .collect(Collectors.toSet()));
        } else {
            user.setRoles(get(id).getRoles());
        }

        dao.update(id, user);
    }
}

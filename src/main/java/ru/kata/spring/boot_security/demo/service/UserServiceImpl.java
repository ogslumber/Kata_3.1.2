package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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
    public User get(String username) {
        return dao.get(username);
    }

    @Override
    @Transactional
    public void create(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        if (user.getAuthorities() == null || user.getAuthorities().size() == 0) {
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
        // Encode password if it was changed.
        User dbUser = get(id);
        if (dbUser != null && !dbUser.getPassword().equals(user.getPassword())) {
            user.setPassword(encoder.encode(user.getPassword()));
        }

        dao.update(id, user);
    }
}

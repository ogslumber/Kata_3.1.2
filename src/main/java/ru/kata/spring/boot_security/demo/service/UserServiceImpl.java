package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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

        // Get roles from DB.
        setRoles(user);

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
        if (!user.getPassword().equals("")) {
            if (dbUser != null && !dbUser.getPassword().equals(user.getPassword())) {
                user.setPassword(encoder.encode(user.getPassword()));
            }
        } else {
            user.setPassword(dbUser.getPassword());
        }

        // Get roles from DB.
        setRoles(user);

        dao.update(id, user);
    }

    private void setRoles(User user) {
        if (user.getAuthorities() != null && user.getAuthorities().size() != 0) {
            Set<Role> roles = new HashSet<>();
            for (Role role : (Set<Role>) user.getAuthorities()) {
                roles.add(roleService.get(role.getName()));
            }
            user.setRoles(roles);
        } else {
            Role userRole = roleService.get("USER");
            user.setRoles(Set.of(userRole));
        }
    }
}

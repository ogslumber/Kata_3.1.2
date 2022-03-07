package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleDao dao;

    @Autowired
    public RoleServiceImpl(RoleDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Role> get() {
        return dao.get();
    }

    @Override
    public Role get(String roleName) {
        return dao.get(roleName);
    }

    @Transactional
    @Override
    public void create(Role role) {
        dao.create(role);
    }
}

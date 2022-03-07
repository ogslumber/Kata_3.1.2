package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class RoleDaoImpl implements RoleDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Role> get() {
        return em.createQuery("select r from Role r").getResultList();
    }

    @Override
    public Role get(String roleName) {
        List<Role> roles = em.createQuery("select r from Role r where r.name = :roleName")
                .setParameter("roleName", roleName)
                .getResultList();
        return roles.size() == 0 ? null : roles.get(0);
    }

    @Override
    public void create(Role role) {
        em.persist(role);
    }
}

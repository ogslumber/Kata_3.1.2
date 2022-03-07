package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    List<User> get();
    User get(long id);
    User get(String username);
    void remove(long id);
    void update(long id, User user);
    void create(User user);
}

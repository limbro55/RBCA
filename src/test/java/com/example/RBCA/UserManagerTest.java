package com.example.RBCA;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {

    @Test
    void addUser() {
        UserManager manager = new UserManager();

        User user = User.validate("john", "John Doe", "john@mail.com");

        manager.add(user);

        assertEquals(1, manager.count());
        assertTrue(manager.exists("john"));
    }

    @Test
    void findByName() {

        UserManager manager = new UserManager();

        User user = User.validate("john","John Doe","john@test.com");

        manager.add(user);   // ОБЯЗАТЕЛЬНО

        assertTrue(manager.findByUsername("john").isPresent());
    }

    @Test
    void removeUser() {
        UserManager manager = new UserManager();

        User user = User.validate("max", "Max Payne", "max@mail.com");
        manager.add(user);

        manager.remove(user);

        assertEquals(0, manager.count());
    }

    @Test
    void filterUsers() {
        UserManager manager = new UserManager();

        manager.add(User.validate("john", "John Doe", "john@mail.com"));
        manager.add(User.validate("anna", "Anna Smith", "anna@mail.com"));

        List<User> result = manager.findByFilter(
                UserFilters.byUsernameContains("jo")
        );

        assertEquals(1, result.size());
    }
}
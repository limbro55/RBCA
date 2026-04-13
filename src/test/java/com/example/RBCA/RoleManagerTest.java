package com.example.RBCA;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleManagerTest {

    @Test
    void addRole() {
        RoleManager manager = new RoleManager();

        Role role = new Role("1", "ADMIN");

        manager.add(role);

        assertEquals(1, manager.count());
    }

    @Test
    void findByName() {

        RoleManager manager = new RoleManager();

        Role role = new Role("ADMIN","Admin role");

        manager.add(role);   // ОБЯЗАТЕЛЬНО

        assertTrue(manager.findByName("ADMIN").isPresent());
    }

    @Test
    void addPermissionToRole() {

        RoleManager manager = new RoleManager();

        Role role = new Role("ADMIN","Admin role");

        manager.add(role);

        Permission permission =
                new Permission("READ","FILES","ALLOW");

        manager.addPermissionToRole("ADMIN", permission);

        assertEquals(1, role.getPermissions().size());
    }
}
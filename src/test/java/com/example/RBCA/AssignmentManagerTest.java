package com.example.RBCA;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentManagerTest {

    @Test
    void addAssignment() {

        AssignmentManager manager = new AssignmentManager();

        User user = User.validate("john", "John Doe", "john@mail.com");
        Role role = new Role("1", "ADMIN");

        AssignmentMetadata meta =
                AssignmentMetadata.now("system", "test");

        RoleAssignment assignment =
                new PermanentAssignment(user, role, meta);

        manager.add(assignment);

        assertEquals(1, manager.count());
    }

    @Test
    void userHasRole() {

        AssignmentManager manager = new AssignmentManager();

        User user = User.validate("john", "John Doe", "john@mail.com");
        Role role = new Role("1", "ADMIN");

        AssignmentMetadata meta =
                AssignmentMetadata.now("system", "test");

        RoleAssignment assignment =
                new PermanentAssignment(user, role, meta);

        manager.add(assignment);

        assertTrue(manager.userHasRole(user, role));
    }

    @Test
    void getUserPermissions() {

        AssignmentManager manager = new AssignmentManager();

        User user = User.validate("john", "John Doe", "john@mail.com");
        Role role = new Role("1", "ADMIN");

        Permission permission = new Permission("READ", "FILES", "ALLOW");
        role.addPermission(permission);

        AssignmentMetadata meta =
                AssignmentMetadata.now("system", "test");

        RoleAssignment assignment =
                new PermanentAssignment(user, role, meta);

        manager.add(assignment);

        Set<Permission> permissions =
                manager.getUserPermissions(user);

        assertEquals(1, permissions.size());
    }
}
package com.example.RBCA;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RBACSystemTest {
    private RBACSystem system;

    @BeforeEach
    void setUp() {
        system = new RBACSystem();
        system.initialize();
    }

    @Test
    void testInitialize() {
        assertTrue(system.getRoleManager().findByName("Admin").isPresent());
        assertTrue(system.getUserManager().findByUsername("admin").isPresent());

        User admin = system.getUserManager().findByUsername("admin").get();
        assertFalse(system.getAssignmentManager().findByUser(admin).isEmpty());
    }

    @Test
    void testGenerateStatistics() {
        String stats = system.generateStatistics();

        assertNotNull(stats);
        assertTrue(stats.contains("Пользователей:  1"));
        assertTrue(stats.contains("Ролей:          2")); // Admin и Viewer
    }
}
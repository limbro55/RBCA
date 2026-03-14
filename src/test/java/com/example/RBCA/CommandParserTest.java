package com.example.RBCA;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {
    private CommandParser parser;
    private RBACSystem system;

    @BeforeEach
    void setUp() {
        parser = new CommandParser();
        system = new RBACSystem();
        system.initialize();
        CommandRegistry.registerAll(parser);
    }

    @Test
    void testUserCreateCommand() {
        String input = "tester\nTest User\ntest@mail.com\n";
        Scanner scanner = new Scanner(input);

        parser.parseAndExecute("user-create", scanner, system);

        boolean exists = system.getUserManager().exists("tester");
        assertTrue(exists, "Пользователь 'tester' должен быть создан");
    }

    @Test
    void testRoleDeleteCommandWithConfirmation() {
        Role role = new Role("TempRole", "To be deleted");
        system.getRoleManager().add(role);

        Scanner scanner = new Scanner("TempRole\n");
        parser.parseAndExecute("role-delete", scanner, system);

        assertFalse(system.getRoleManager().findByName("TempRole").isPresent());
    }

    @Test
    void testAssignRolePermanent() {
        User user = User.validate("worker", "Worker", "work@mail.com");
        system.getUserManager().add(user);

        String input = "worker\nViewer\n1\nTesting\n";
        Scanner scanner = new Scanner(input);

        parser.parseAndExecute("assign-role", scanner, system);

        Role viewer = system.getRoleManager().findByName("Viewer").get();
        assertTrue(system.getAssignmentManager().userHasRole(user, viewer));
    }

    @Test
    void testPermissionCheck() {
        String input = "admin\nREAD\nusers\n";
        Scanner scanner = new Scanner(input);

        assertDoesNotThrow(() -> parser.parseAndExecute("permissions-check", scanner, system));
    }

    @Test
    void testUnknownCommandDoesNotCrash() {
        assertDoesNotThrow(() ->
                parser.parseAndExecute("invalid-command-123", new Scanner(""), system)
        );
    }
}
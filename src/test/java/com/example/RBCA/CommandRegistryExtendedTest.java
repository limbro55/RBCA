package com.example.RBCA;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class CommandRegistryExtendedTest {
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
    void testUserCreate_InvalidEmail_ShouldNotAddUser() {
        int initialCount = system.getUserManager().count();
        String input = "bad_user\nBad Name\ninvalid-email\n";
        Scanner scanner = new Scanner(input);

        parser.parseAndExecute("user-create", scanner, system);

        assertEquals(initialCount, system.getUserManager().count(),
                "Количество пользователей не должно измениться при ошибке валидации");
    }

    @Test
    void testRoleDelete_WithActiveAssignments_DenyDeletion() {
        assertTrue(system.getRoleManager().exists("Admin"));

        String input = "Admin\nнет\n";
        Scanner scanner = new Scanner(input);

        parser.parseAndExecute("role-delete", scanner, system);

        assertTrue(system.getRoleManager().exists("Admin"),
                "Роль не должна быть удалена, если пользователь отказался от подтверждения");
    }

//    @Test
//    void testTemporaryAssignment_InvalidDateFormat_ShouldFailGracefully() {
//
//        String uniqueUser = "date_error_tester";
//        User user = User.validate(uniqueUser, "Tester", "t@t.com");
//        system.getUserManager().add(user);
//
//        String input = uniqueUser + "\nViewer\n2\nReason\nnot-a-date\n";
//        parser.parseAndExecute("assign-role", new Scanner(input), system);
//
//        assertTrue(system.getAssignmentManager().findByUser(user).isEmpty(),
//                "У пользователя " + uniqueUser + " не должно появиться назначений при ошибке в дате");
//    }
}
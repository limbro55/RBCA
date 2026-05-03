package com.example.RBCA;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RBACSystem {
    private final UserManager userManager = new UserManager();
    private final RoleManager roleManager = new RoleManager();
    private final AssignmentManager assignmentManager = new AssignmentManager();
    private String currentUser = "system_admin";
    private final ReportGenerator reportGenerator = new ReportGenerator();

    public void initialize() {
        // 1. Создаем права
        Permission readUsers = new Permission("READ", "users", "Просмотр пользователей");
        Permission writeUsers = new Permission("WRITE", "users", "Редактирование пользователей");
        Permission deleteUsers = new Permission("DELETE", "users", "Удаление пользователей");

        // 2. Создаем роли
        Role adminRole = new Role("Admin", "Полный доступ ко всему");
        adminRole.addPermission(readUsers);
        adminRole.addPermission(writeUsers);
        adminRole.addPermission(deleteUsers);

        Role viewerRole = new Role("Viewer", "Только чтение");
        viewerRole.addPermission(readUsers);

        roleManager.add(adminRole);
        roleManager.add(viewerRole);

        // 3. Создаем тестового администратора
        User adminUser = User.validate("admin", "System Administrator", "admin@corp.com");
        userManager.add(adminUser);

        // 4. Назначаем роль
        assignmentManager.add(new PermanentAssignment(adminUser, adminRole,
                AssignmentMetadata.now(currentUser, "Initial setup")));
    }

    public String generateStatistics() {
        long totalAssignments = assignmentManager.count();
        long active = assignmentManager.getActiveAssignments().size();

        return String.format("""
            ======= СТАТИСТИКА СИСТЕМЫ =======
            Пользователей:  %d
            Ролей:          %d
            Назначений:     %d (Активных: %d, Истекших: %d)
            ==================================
            """,
                userManager.count(),
                roleManager.count(),
                totalAssignments,
                active,
                totalAssignments - active);
    }

    // Геттеры и сеттеры
    public UserManager getUserManager() { return userManager; }
    public RoleManager getRoleManager() { return roleManager; }
    public AssignmentManager getAssignmentManager() { return assignmentManager; }
    public String getCurrentUser() { return currentUser; }
    public void setCurrentUser(String username) { this.currentUser = username; }
    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }
    public List<String> getFullUserReport() {
        return reportGenerator.buildUserReportParallel(userManager.findAll());
    }
}

@FunctionalInterface
interface Command {
    void execute(Scanner scanner, RBACSystem system);
}
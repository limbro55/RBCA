package com.example.RBCA;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class ReportGenerator {

    public String generateUserReport(UserManager userManager, AssignmentManager assignmentManager) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s | %-30s | %s%n", "Username", "Full Name", "Assigned Roles"));
        sb.append("-".repeat(80)).append("\n");

        for (User user : userManager.findAll()) {
            String roles = assignmentManager.findByUser(user).stream()
                    .map(a -> a.role().getName())
                    .collect(Collectors.joining(", "));

            sb.append(String.format("%-20s | %-30s | %s%n",
                    user.username(), user.fullName(), roles.isEmpty() ? "No roles" : roles));
        }
        return sb.toString();
    }

    public String generateRoleReport(RoleManager roleManager, AssignmentManager assignmentManager) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s | %-15s | %s%n", "Role Name", "Users Count", "Description"));
        sb.append("-".repeat(70)).append("\n");

        for (Role role : roleManager.findAll()) {
            long count = assignmentManager.findAll().stream()
                    .filter(a -> a.role().equals(role))
                    .map(a -> a.user().username())
                    .distinct()
                    .count();

            sb.append(String.format("%-20s | %-15d | %s%n",
                    role.getName(), count, role.getDescription()));
        }
        return sb.toString();
    }

    public String generatePermissionMatrix(UserManager userManager, AssignmentManager assignmentManager) {
        StringBuilder sb = new StringBuilder();
        List<User> users = userManager.findAll();

        Set<String> resources = assignmentManager.findAll().stream()
                .flatMap(a -> a.role().getPermissions().stream())
                .map(Permission::resource)
                .collect(Collectors.toCollection(TreeSet::new));

        sb.append(String.format("%-15s", "User \\ Resource"));
        for (String res : resources) {
            sb.append(String.format(" | %-12s", res));
        }
        sb.append("\n").append("-".repeat(15 + resources.size() * 15)).append("\n");

        for (User user : users) {
            sb.append(String.format("%-15s", user.username()));
            var userPerms = assignmentManager.getUserPermissions(user);

            for (String res : resources) {
                String actions = userPerms.stream()
                        .filter(p -> p.resource().equals(res))
                        .map(Permission::name)
                        .collect(Collectors.joining(","));

                sb.append(String.format(" | %-12s", actions.isEmpty() ? "-" : actions));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void exportToFile(String report, String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.print(report);
            System.out.println("Отчёт успешно экспортирован в файл: " + filename);
        } catch (Exception e) {
            System.err.println("Ошибка при экспорте отчёта: " + e.getMessage());
        }
    }
}
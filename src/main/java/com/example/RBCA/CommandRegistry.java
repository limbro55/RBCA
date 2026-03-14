package com.example.RBCA;

import java.util.*;
import com.example.RBCA.*;

public class CommandRegistry {

    public static void registerAll(CommandParser parser) {
        parser.registerCommand("help", "Вывести список всех команд", (s, sys) -> parser.printHelp());

        parser.registerCommand("stats", "Показать статистику системы", (s, sys) -> {
            System.out.println(sys.generateStatistics());
        });

        parser.registerCommand("clear", "Очистить экран", (s, sys) -> {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            for(int i=0; i<50; i++) System.out.println();
        });

        parser.registerCommand("exit", "Выход из системы", (s, sys) -> {
            System.out.print("Вы уверены, что хотите выйти? (да/нет): ");
            if (s.nextLine().equalsIgnoreCase("да")) {
                System.out.println("Завершение работы...");
                System.exit(0);
            }
        });

        parser.registerCommand("user-list", "Список всех пользователей", (s, sys) -> {
            List<User> users = sys.getUserManager().findAll();
            printUserTable(users);
        });

        parser.registerCommand("user-view", "Детальная информация о пользователе", (s, sys) -> {
            System.out.print("Введите username: ");
            String username = s.nextLine();
            sys.getUserManager().findByUsername(username).ifPresentOrElse(user -> {
                System.out.println("\n=== ПРОФИЛЬ ПОЛЬЗОВАТЕЛЯ ===");
                System.out.println(user.format());

                System.out.println("\nНазначенные роли:");
                var assignments = sys.getAssignmentManager().findByUser(user);
                if (assignments.isEmpty()) System.out.println(" - Роли не назначены");
                else assignments.forEach(a -> System.out.println(" - " + a.summary()));

                System.out.println("\nИтоговые разрешения:");
                var perms = sys.getAssignmentManager().getUserPermissions(user);
                perms.forEach(p -> System.out.println(" [!] " + p.format()));
            }, () -> System.out.println("Пользователь не найден."));
        });

        parser.registerCommand("role-create", "Создать новую роль", (s, sys) -> {
            System.out.print("Название роли: "); String name = s.nextLine();
            System.out.print("Описание: "); String desc = s.nextLine();

            Role newRole = new Role(name, desc);

            System.out.println("Добавить права доступа? (да/нет)");
            while(s.nextLine().equalsIgnoreCase("да")) {
                System.out.print("Имя права (напр. READ): "); String pName = s.nextLine();
                System.out.print("Ресурс (напр. Files): "); String pRes = s.nextLine();
                System.out.print("Описание права: "); String pDesc = s.nextLine();

                try {
                    newRole.addPermission(new Permission(pName, pRes, pDesc));
                    System.out.println("Право добавлено.");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
                System.out.print("Добавить еще одно? (да/нет): ");
            }
            sys.getRoleManager().add(newRole);
            System.out.println("Роль '" + name + "' сохранена.");
        });

        parser.registerCommand("role-delete", "Удалить роль (с проверкой зависимостей)", (s, sys) -> {
            System.out.print("Введите название роли: ");
            String name = s.nextLine();
            sys.getRoleManager().findByName(name).ifPresentOrElse(role -> {
                // Проверка, назначена ли роль кому-то
                var affectedUsers = sys.getAssignmentManager().findAll().stream()
                        .filter(a -> a.role().equals(role))
                        .map(a -> a.user().username())
                        .distinct()
                        .toList();

                if (!affectedUsers.isEmpty()) {
                    System.out.println("ВНИМАНИЕ! Роль назначена пользователям: " + affectedUsers);
                    System.out.print("Всё равно удалить? Это аннулирует их доступы. (да/нет): ");
                    if (!s.nextLine().equalsIgnoreCase("да")) return;

                    // Удаляем все назначения с этой ролью
                    sys.getAssignmentManager().findAll().stream()
                            .filter(a -> a.role().equals(role))
                            .forEach(a -> sys.getAssignmentManager().remove(a));
                }
                sys.getRoleManager().remove(role);
                System.out.println("Роль удалена.");
            }, () -> System.out.println("Роль не найдена."));
        });

        parser.registerCommand("assign-role", "Назначить роль пользователю", (s, sys) -> {
            try {
                System.out.print("Username: "); String un = s.nextLine();
                User user = sys.getUserManager().findByUsername(un).orElseThrow();

                System.out.println("Доступные роли: " + sys.getRoleManager().findAll().stream().map(Role::getName).toList());
                System.out.print("Выберите роль: "); String rn = s.nextLine();
                Role role = sys.getRoleManager().findByName(rn).orElseThrow();

                System.out.print("Тип (1 - Постоянная, 2 - Временная): ");
                String type = s.nextLine();

                System.out.print("Причина назначения: ");
                String reason = s.nextLine();
                AssignmentMetadata meta = AssignmentMetadata.now(sys.getCurrentUser(), reason);

                if (type.equals("2")) {
                    System.out.print("Дата истечения (напр. 2026-12-31T23:59): ");
                    String expire = s.nextLine();
                    try {
                        TemporaryAssignment temp = new TemporaryAssignment(user, role, meta, expire, false);
                        sys.getAssignmentManager().add(temp);
                        System.out.println("Временная роль успешно назначена.");
                    } catch (Exception e) {
                        System.out.println("Ошибка формата даты! Назначение отменено.");
                    }
                } else {
                    sys.getAssignmentManager().add(new PermanentAssignment(user, role, meta));
                    System.out.println("Постоянная роль успешно назначена.");
                }

            } catch (Exception e) {
                System.out.println("Ошибка при назначении: " + e.getMessage());
            }
        });

        parser.registerCommand("user-create", "Создать нового пользователя", (s, sys) -> {
            try {
                System.out.print("Username: "); String un = s.nextLine();
                System.out.print("Full Name: "); String fn = s.nextLine();
                System.out.print("Email: "); String em = s.nextLine();

                User newUser = User.validate(un, fn, em);
                sys.getUserManager().add(newUser);
                System.out.println("Пользователь успешно создан!");
            } catch (Exception e) {
                System.out.println("Ошибка валидации: " + e.getMessage());
            }
        });

        parser.registerCommand("assignment-list", "Список всех назначений", (s, sys) -> {
            System.out.printf("%-10s | %-12s | %-10s | %-10s | %-20s%n",
                    "ID", "User", "Role", "Status", "Date");
            System.out.println("-".repeat(70));
            sys.getAssignmentManager().findAll().forEach(a -> {
                System.out.printf("%-10s | %-12s | %-10s | %-10s | %-20s%n",
                        a.assignmentId(), a.user().username(), a.role().getName(),
                        (a.isActive() ? "ACTIVE" : "INACTIVE"), a.metadata().assignedAt().substring(0, 16));
            });
        });

        parser.registerCommand("permissions-check", "Проверить доступ", (s, sys) -> {
            System.out.print("Username: "); String un = s.nextLine();
            System.out.print("Permission (напр. WRITE): "); String perm = s.nextLine();
            System.out.print("Resource (напр. users): "); String res = s.nextLine();

            boolean hasAccess = sys.getAssignmentManager().userHasPermission(
                    sys.getUserManager().findByUsername(un).orElseThrow(), perm, res
            );

            if (hasAccess) {
                System.out.println(">>> ДОСТУП РАЗРЕШЕН");
            } else {
                System.out.println(">>> ДОСТУП ЗАПРЕЩЕН");
            }
        });
    }

    private static void printUserTable(List<User> users) {
        System.out.printf("%-15s | %-25s | %-25s%n", "Username", "Full Name", "Email");
        System.out.println("-".repeat(70));
        users.forEach(u -> System.out.printf("%-15s | %-25s | %-25s%n",
                u.username(), u.fullName(), u.email()));
    }
}
package com.example.RBCA;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class AuditLog {

    public record AuditEntry(
            String timestamp,
            String action,
            String performer,
            String target,
            String details
    ) {
        @Override
        public String toString() {
            return String.format("[%s] %-15s | By: %-12s | Target: %-12s | %s",
                    timestamp, action, performer, target, details);
        }
    }

    private final List<AuditEntry> entries = new ArrayList<>();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void log(String action, String performer, String target, String details) {
        entries.add(new AuditEntry(
                LocalDateTime.now().format(FORMATTER),
                action,
                performer,
                target,
                details
        ));
    }

    public List<AuditEntry> getAll() {
        return new ArrayList<>(entries);
    }

    public List<AuditEntry> getByPerformer(String performer) {
        return entries.stream()
                .filter(e -> e.performer().equalsIgnoreCase(performer))
                .collect(Collectors.toList());
    }

    public List<AuditEntry> getByAction(String action) {
        return entries.stream()
                .filter(e -> e.action().equalsIgnoreCase(action))
                .collect(Collectors.toList());
    }

    public void printLog() {
        if (entries.isEmpty()) {
            System.out.println("Журнал аудита пуст.");
            return;
        }
        System.out.println("\n" + "=".repeat(90));
        System.out.println("ПОЛНЫЙ ЖУРНАЛ СОБЫТИЙ СИСТЕМЫ");
        System.out.println("=".repeat(90));
        entries.forEach(System.out::println);
        System.out.println("=".repeat(90));
    }

    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Timestamp;Action;Performer;Target;Details"); // Заголовок CSV
            for (AuditEntry e : entries) {
                writer.printf("%s;%s;%s;%s;%s%n",
                        e.timestamp(), e.action(), e.performer(), e.target(), e.details());
            }
            System.out.println("Лог успешно сохранен в файл: " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении лога: " + e.getMessage());
        }
    }
}
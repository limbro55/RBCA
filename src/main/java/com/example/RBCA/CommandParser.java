package com.example.RBCA;

import java.util.*;

public class CommandParser {
    private final Map<String, Command> commands = new LinkedHashMap<>();
    private final Map<String, String> commandDescriptions = new LinkedHashMap<>();

    public void registerCommand(String name, String description, Command command) {
        commands.put(name.toLowerCase(), command);
        commandDescriptions.put(name.toLowerCase(), description);
    }

    public void parseAndExecute(String input, Scanner scanner, RBACSystem system) {
        if (input == null || input.isBlank()) return;

        String commandName = input.trim().split("\\s+")[0].toLowerCase();

        Command cmd = commands.get(commandName);
        if (cmd != null) {
            cmd.execute(scanner, system);
        } else {
            System.out.println("Команда '" + commandName + "' не найдена.");
        }
    }

    public void printHelp() {
        System.out.println("\n=== ДОСТУПНЫЕ КОМАНДЫ ===");
        commandDescriptions.forEach((name, desc) ->
                System.out.printf("%-20s - %s%n", name, desc));
    }
}
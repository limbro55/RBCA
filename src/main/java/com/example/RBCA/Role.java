package com.example.RBCA;

import java.util.HashSet;
import java.util.Set;

public class Role {

    private static int counter = 1;

    private final String id;
    private final String name;
    private final String description;
    private final Set<Permission> permissions = new HashSet<>();

    public Role(String name, String description) {
        this.id = "role_" + counter++;
        this.name = name;
        this.description = description;
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        permissions.remove(permission);
    }

    public boolean hasPermission(String permissionName, String resource) {
        for (Permission p : permissions) {
            if (p.name().equalsIgnoreCase(permissionName)
                    && p.resource().equalsIgnoreCase(resource)) {
                return true;
            }
        }
        return false;
    }

    public Set<Permission> getPermissions() {
        return new HashSet<>(permissions); // копия
    }

    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Role other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String format() {
        String result = "Role: " + name + " [ID: " + id + "]\n";
        result += "Description: " + description + "\n";
        result += "Permissions (" + permissions.size() + "):\n";

        for (Permission p : permissions) {
            result += " - " + p.format() + "\n";
        }

        return result;
    }
}
package com.example.RBCA;

public final class RoleFilters {

    private RoleFilters() {}

    public static RoleFilter byName(String name) {
        return role -> role.getName().equalsIgnoreCase(name);
    }

    public static RoleFilter byNameContains(String substring) {
        return role -> role.getName()
                .toLowerCase()
                .contains(substring.toLowerCase());
    }

    public static RoleFilter hasPermission(Permission permission) {
        return role -> role.getPermissions().contains(permission);
    }

    public static RoleFilter hasPermission(String permissionName,
                                           String resource) {

        return role -> role.getPermissions().stream()
                .anyMatch(p ->
                        p.name().equalsIgnoreCase(permissionName)
                                && p.resource().equalsIgnoreCase(resource));
    }

    public static RoleFilter hasAtLeastNPermissions(int n) {
        return role -> role.getPermissions().size() >= n;
    }
}
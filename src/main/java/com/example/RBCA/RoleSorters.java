package com.example.RBCA;

import java.util.Comparator;

public final class RoleSorters {

    private RoleSorters() {}

    public static Comparator<Role> byName() {
        return Comparator.comparing(r -> r.getName().toLowerCase());
    }

    public static Comparator<Role> byPermissionCount() {
        return Comparator.comparingInt(r -> r.getPermissions().size());
    }
}
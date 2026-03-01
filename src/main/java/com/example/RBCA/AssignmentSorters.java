package com.example.RBCA;

import java.util.Comparator;

public final class AssignmentSorters {

    private AssignmentSorters() {}

    public static Comparator<RoleAssignment> byUsername() {
        return Comparator.comparing(a -> a.user().username().toLowerCase());
    }

    public static Comparator<RoleAssignment> byRoleName() {
        return Comparator.comparing(a -> a.role().getName().toLowerCase());
    }

    public static Comparator<RoleAssignment> byAssignmentDate() {
        return Comparator.comparing(a -> a.metadata().assignedAt());
    }
}
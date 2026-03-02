package com.example.RBCA;

import java.time.LocalDateTime;

public final class AssignmentFilters {

    private AssignmentFilters() {}

    public static AssignmentFilter byUser(User user) {
        return a -> a.user().equals(user);
    }

    public static AssignmentFilter byUsername(String username) {
        return a -> a.user().username().equalsIgnoreCase(username);
    }

    public static AssignmentFilter byRole(Role role) {
        return a -> a.role().equals(role);
    }

    public static AssignmentFilter byRoleName(String roleName) {
        return a -> a.role().getName()
                .equalsIgnoreCase(roleName);
    }

    public static AssignmentFilter activeOnly() {
        return RoleAssignment::isActive;
    }

    public static AssignmentFilter inactiveOnly() {
        return a -> !a.isActive();
    }

    public static AssignmentFilter byType(String type) {
        return a -> a.assignmentType().equalsIgnoreCase(type);
    }

    public static AssignmentFilter assignedBy(String username) {
        return a -> a.metadata().assignedBy()
                .equalsIgnoreCase(username);
    }

    public static AssignmentFilter assignedAfter(String date) {
        LocalDateTime d = LocalDateTime.parse(date);
        return a -> {
            LocalDateTime assignedAt = LocalDateTime.parse(a.metadata().assignedAt());
            return assignedAt.isAfter(d);
        };
    }

}
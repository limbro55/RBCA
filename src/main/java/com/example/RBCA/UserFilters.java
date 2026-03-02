package com.example.RBCA;

public final class UserFilters {

    private UserFilters() {}

    public static UserFilter byUsername(String username) {
        return user -> user.username().equalsIgnoreCase(username);
    }

    public static UserFilter byEmail(String email) {
        return user -> user.email().equalsIgnoreCase(email);
    }

    public static UserFilter byEmailDomain(String domain) {
        return user -> user.email().toLowerCase().endsWith("@" + domain.toLowerCase());
    }

    public static UserFilter byFullNameContains(String text) {
        return user -> user.fullName().toLowerCase().contains(text.toLowerCase());
    }
}
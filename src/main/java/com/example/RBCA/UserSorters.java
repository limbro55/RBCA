package com.example.RBCA;

import java.util.Comparator;

public final class UserSorters {

    private UserSorters() {}

    public static Comparator<User> byUsername() {
        return Comparator.comparing(u -> u.username().toLowerCase());
    }

    public static Comparator<User> byFullName() {
        return Comparator.comparing(u -> u.fullName().toLowerCase());
    }

    public static Comparator<User> byEmail() {
        return Comparator.comparing(u -> u.email().toLowerCase());
    }
}
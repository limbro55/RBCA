package com.example.RBCA;

public record User(String username, String fullName, String email) {

    public static User validate(String username, String fullName, String email) {

        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username cannot be empty");

        if (username.length() < 3 || username.length() > 20)
            throw new IllegalArgumentException("Username must be 3-20 characters");

        if (!username.matches("[a-zA-Z0-9_]+"))
            throw new IllegalArgumentException("Username must contain only letters, digits and _");

        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("Full name cannot be empty");

        if (email == null || !email.contains("@") || !email.contains("."))
            throw new IllegalArgumentException("Invalid email");

        return new User(username, fullName, email);
    }

    public String format() {
        return username + " (" + fullName + ") <" + email + ">";
    }
}
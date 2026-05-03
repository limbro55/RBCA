package com.example.RBCA;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserManager implements Repository<User> {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public void add(User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        // Используем putIfAbsent для атомарной проверки и добавления
        if (users.putIfAbsent(user.username(), user) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
    }

    @Override
    public boolean remove(User user) {
        if (user == null) return false;
        return users.remove(user.username()) != null;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public int count() {
        return users.size();
    }

    @Override
    public void clear() {
        users.clear();
    }


    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public Optional<User> findByEmail(String email) {
        return users.values()
                .stream()
                .filter(u -> u.email().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<User> findByFilter(UserFilter filter) {
        return users.values()
                .stream()
                .filter(filter::test)
                .toList();
    }

    public List<User> findAll(UserFilter filter, Comparator<User> sorter) {
        return users.values()
                .stream()
                .filter(filter::test)
                .sorted(sorter)
                .toList();
    }

    public boolean exists(String username) {
        return users.containsKey(username);
    }

    public void update(String username, String newFullName, String newEmail) {
        User existing = users.get(username);

        if (existing == null)
            throw new NoSuchElementException("User not found");

        User updated = User.validate(username, newFullName, newEmail);
        users.put(username, updated);
    }
}
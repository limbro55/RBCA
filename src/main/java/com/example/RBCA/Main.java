package com.example.RBCA;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== CREATE USER ===");
        User user = User.validate(
                "admin_1",
                "Safonov Danil",
                "Safonov@mail.com"
        );
        System.out.println(user.format());

        System.out.println("\nCREATE PERMISSIONS");
        Permission readUsers =
                new Permission("read", "users", "Can view users");
        Permission writeUsers =
                new Permission("write", "users", "Can edit users");

        System.out.println(readUsers.format());
        System.out.println(writeUsers.format());

        System.out.println("\nCREATE ROLE ");
        Role adminRole =
                new Role("Administrator", "Full system access");

        adminRole.addPermission(readUsers);
        adminRole.addPermission(writeUsers);

        System.out.println(adminRole.format());

        System.out.println("\nPERMANENT ASSIGNMENT");
        AssignmentMetadata metadata1 =
                AssignmentMetadata.now("system", "Initial setup");

        PermanentAssignment permanent =
                new PermanentAssignment(user, adminRole, metadata1);

        System.out.println(permanent.summary());

        System.out.println("\nRevoking role...");
        permanent.revoke();
        System.out.println(permanent.summary());

        System.out.println("\nTEMPORARY ASSIGNMENT");

        String futureDate =
                LocalDateTime.now().plusDays(1).toString();

        AssignmentMetadata metadata2 =
                AssignmentMetadata.now("admin", "Temporary access");

        TemporaryAssignment temporary =
                new TemporaryAssignment(
                        user,
                        adminRole,
                        metadata2,
                        futureDate,
                        false
                );

        System.out.println(temporary.summary());

        System.out.println("\nIs expired? " + temporary.isExpired());
        System.out.println("Is active? " + temporary.isActive());

        System.out.println("\nExtending temporary role...");
        temporary.extend(LocalDateTime.now().plusDays(3).toString());

        System.out.println("Is active after extend? " + temporary.isActive());
    }
}
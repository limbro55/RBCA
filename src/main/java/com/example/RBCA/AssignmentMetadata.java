package com.example.RBCA;

import java.time.LocalDateTime;

public record AssignmentMetadata(String assignedBy,
                                 String assignedAt,
                                 String reason) {

    public static AssignmentMetadata now(String assignedBy, String reason) {
        return new AssignmentMetadata(
                assignedBy,
                LocalDateTime.now().toString(),
                reason
        );
    }

    public String format() {
        return "Assigned by " + assignedBy +
                " at " + assignedAt +
                "\nReason: " + reason;
    }
}
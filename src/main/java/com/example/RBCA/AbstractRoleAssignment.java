package com.example.RBCA;

public abstract class AbstractRoleAssignment implements RoleAssignment {

    private static int counter = 1;

    private final String assignmentId;
    private final User user;
    private final Role role;
    private final AssignmentMetadata metadata;

    public AbstractRoleAssignment(User user,
                                  Role role,
                                  AssignmentMetadata metadata) {
        this.assignmentId = "assign_" + counter++;
        this.user = user;
        this.role = role;
        this.metadata = metadata;
    }

    public String assignmentId() { return assignmentId; }
    public User user() { return user; }
    public Role role() { return role; }
    public AssignmentMetadata metadata() { return metadata; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AbstractRoleAssignment other)) return false;
        return assignmentId.equals(other.assignmentId);
    }

    @Override
    public int hashCode() {
        return assignmentId.hashCode();
    }

    public String summary() {
        return "[" + assignmentType() + "] "
                + role.getName() + " assigned to "
                + user.username()
                + "\nReason: " + metadata.reason()
                + "\nStatus: " + (isActive() ? "ACTIVE" : "INACTIVE");
    }
}
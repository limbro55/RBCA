package com.example.RBCA;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReportGenerator {

    public List<String> buildUserReportParallel(Collection<User> users) {
        return users.parallelStream()
                .map(user -> "USER_REPORT: " + user.format())
                .collect(Collectors.toList());
    }

    public List<String> buildPermissionsMatrixParallel(AssignmentManager assignmentManager, Collection<User> users) {
        return users.parallelStream()
                .map(user -> {
                    var perms = assignmentManager.getUserPermissions(user);
                    String permsList = perms.stream()
                            .map(Permission::name)
                            .collect(Collectors.joining(", "));
                    return user.username() + " permissions: [" + permsList + "]";
                })
                .collect(Collectors.toList());
    }
}
package com.taskflow.tms.dto.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskAssignedEvent {
    private UUID taskId;
    private UUID assigneeId;
    private String title;
    private String description;
    private String priority;
    private java.time.LocalDateTime dueDate;

    private UserDetails assignee;
    private String assignedBy;
    private String projectName;
    private java.time.LocalDateTime timestamp;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserDetails {
        private String name;
        private String email;
    }
}

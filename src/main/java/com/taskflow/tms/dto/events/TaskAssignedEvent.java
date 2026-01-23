package com.taskflow.tms.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignedEvent {
    private UUID taskId;
    private UUID assigneeId;
    private String taskTitle;
    private String assignedBy;
    private UUID projectId;
    private LocalDateTime timestamp;
}

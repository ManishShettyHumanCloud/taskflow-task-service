package com.taskflow.tms.dtos;

import com.taskflow.tms.enums.Priority;
import com.taskflow.tms.enums.TaskType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TaskResponse(
        UUID taskId,
        String title,
        String description,
        TaskType taskType,
        UUID parentTaskId,
        UUID projectId,
        UUID statusId,
        UUID assigneeId,
        Priority priority,
        LocalDate dueDate,
        LocalDate startDate,
        Instant createdAt,
        Instant updatedAt
) {
}
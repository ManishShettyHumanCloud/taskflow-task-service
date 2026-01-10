package com.taskflow.tms.dtos;

import com.taskflow.tms.enums.Priority;
import com.taskflow.tms.enums.TaskType;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateTaskRequest(
        String title,
        String description,
        UUID projectId,
        UUID statusId,
        UUID assigneeId,
        Priority priority,
        LocalDate startDate,
        LocalDate dueDate
) {}

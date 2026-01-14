package com.taskflow.tms.dtos;

import com.taskflow.tms.enums.Priority;
import com.taskflow.tms.enums.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;
//fields in record are final
// contructor is auto generated
//provides immutablity
public record CreateTaskRequest(

        @NotBlank(message = "Title is required")
        String title,

        String description,

        @NotNull(message = "Task type is required")
        TaskType taskType,

        UUID parentTaskId,

        @NotNull(message = "Project ID is required")
        UUID projectId,

        @NotNull(message = "Status ID is required")
        UUID statusId,

        UUID assigneeId,

        @NotNull(message = "Priority is required")
        Priority priority,

        LocalDate dueDate,
        
        LocalDate startDate
) {
}

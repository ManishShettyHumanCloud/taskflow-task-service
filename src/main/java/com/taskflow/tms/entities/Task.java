package com.taskflow.tms.entities;

import com.taskflow.tms.enums.Priority;
import com.taskflow.tms.enums.TaskType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.UUID;

@Entity
@Table(name="tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID taskId;

    @Column(nullable=false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType;


    @Column(name="parent_task_id")
    private UUID parentTaskId;

    //external references
    @Column(nullable = false)
    private UUID projectId;

    @Column(nullable = false)
    private UUID statusId;

    private UUID assigneeId;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private LocalDate dueDate;

    private LocalDate startDate;

    private Instant createdAt;
    private Instant updatedAt;


}

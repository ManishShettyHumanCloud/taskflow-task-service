package com.taskflow.tms.dtos;

import java.util.UUID;

public record MoveTaskRequest(
        UUID statusId,
        Integer position
) {}

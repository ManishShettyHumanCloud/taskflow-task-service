package com.taskflow.tms.repository;

import com.taskflow.tms.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {


    List<Task> findByAssigneeId(UUID assigneeId);

    List<Task> findByProjectId(UUID projectId);

    boolean existsByParentTaskId(UUID taskId);

    List<Task> findByProjectIdAndStatusIdOrderByPosition(
            UUID projectId,
            UUID statusId
    );

    List<Task> findByProjectIdAndStatusIdAndPositionGreaterThanEqual(
            UUID projectId,
            UUID statusId,
            Integer position
    );
    @org.springframework.data.jpa.repository.Query("SELECT MAX(t.position) FROM Task t WHERE t.projectId = :projectId AND t.statusId = :statusId")
    java.util.Optional<Integer> findMaxPosition(@org.springframework.data.repository.query.Param("projectId") UUID projectId, @org.springframework.data.repository.query.Param("statusId") UUID statusId);

    List<Task> findByParentTaskId(UUID parentTaskId);
}

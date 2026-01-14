package com.taskflow.tms.services;


import com.taskflow.tms.clients.ProjectServiceClient;
import com.taskflow.tms.dtos.*;
import com.taskflow.tms.entities.Task;
import com.taskflow.tms.enums.TaskType;
import com.taskflow.tms.exceptions.ProjectNotFoundException;
import com.taskflow.tms.repository.TaskRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class  TaskService {

    private final TaskRepository taskRepository;
    private final ProjectServiceClient projectServiceClient;

    public TaskResponse createTask(CreateTaskRequest createTaskRequest){
        // Validate project exists
        validateProjectExists(createTaskRequest.projectId());
        validateHierarchy(createTaskRequest);
        validateParentType(createTaskRequest);
        
        Integer position = taskRepository.findMaxPosition(
                createTaskRequest.projectId(), 
                createTaskRequest.statusId()
        ).orElse(0) + 1;

        Task task= Task.builder()
                .title(createTaskRequest.title())
                .description(createTaskRequest.description())
                .taskType(createTaskRequest.taskType())
                .parentTaskId(createTaskRequest.parentTaskId())
                .projectId(createTaskRequest.projectId())
                .statusId(createTaskRequest.statusId())
                .assigneeId(createTaskRequest.assigneeId())
                .dueDate(createTaskRequest.dueDate())
                .startDate(createTaskRequest.startDate())
                .priority(createTaskRequest.priority())
                .position(position)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        Task saved=taskRepository.save(task);
        return mapToResponse(saved);


    }

    public TaskResponse mapToResponse(Task task){
        return new TaskResponse(
                task.getTaskId(),
                task.getTitle(),
                task.getDescription(),
                task.getTaskType(),
                task.getParentTaskId(),
                task.getProjectId(),
                task.getStatusId(),
                task.getAssigneeId(),
                task.getPriority(),
                task.getDueDate(),
                task.getStartDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    private void validateHierarchy(CreateTaskRequest request){
        if(request.taskType() == TaskType.EPIC && request.parentTaskId() != null){
            throw new IllegalArgumentException("Epic cannot have parent task");
        }
        if(request.taskType()==TaskType.USER_STORY && request.parentTaskId()==null){
            throw new IllegalArgumentException("User story must belong to an epic");
        }
        if (request.taskType() == TaskType.SUBTASK && request.parentTaskId() == null) {
            throw new IllegalArgumentException("Subtask must belong to a user story");
        }
    }

    @Transactional
    public void moveTask(UUID taskId, MoveTaskRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        UUID oldStatusId = task.getStatusId();
        UUID newStatusId = request.statusId();

        // 1️⃣ Close gap in old column
        List<Task> oldColumnTasks =
                taskRepository.findByProjectIdAndStatusIdAndPositionGreaterThanEqual(
                        task.getProjectId(),
                        oldStatusId,
                        task.getPosition()
                );

        for (Task t : oldColumnTasks) {
            t.setPosition(t.getPosition() - 1);
        }

        // 2️⃣ Make space in new column
        List<Task> newColumnTasks =
                taskRepository.findByProjectIdAndStatusIdAndPositionGreaterThanEqual(
                        task.getProjectId(),
                        newStatusId,
                        request.position()
                );

        for (Task t : newColumnTasks) {
            t.setPosition(t.getPosition() + 1);
        }

        // 3️⃣ Move task
        task.setStatusId(newStatusId);
        task.setPosition(request.position());

        taskRepository.save(task);
    }

    public void validateParentType(CreateTaskRequest request){
        if (request.parentTaskId() == null) return;

        Task parent = taskRepository.findById(request.parentTaskId())
                .orElseThrow(() -> new IllegalArgumentException("Parent task not found"));

        if (request.taskType() == TaskType.USER_STORY && parent.getTaskType() != TaskType.EPIC) {
            throw new IllegalArgumentException("User story must be under an epic");
        }

        if (request.taskType() == TaskType.SUBTASK && parent.getTaskType() != TaskType.USER_STORY) {
            throw new IllegalArgumentException("Subtask must be under a user story");
        }

    }

    public TaskResponse getTaskById(UUID id) {
        Optional<Task> task=taskRepository.findById(id);
        return mapToResponse(task.get());
    }

    public List<Task> getAllTasks() {

        return taskRepository.findAll();
    }

    public TaskResponse updateTask(UUID taskId, UpdateTaskRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        // Validate project if it's being changed
        if (request.projectId() != null && !request.projectId().equals(task.getProjectId())) {
            validateProjectExists(request.projectId());
            task.setProjectId(request.projectId());
        }

        if (request.title() != null)
            task.setTitle(request.title());

        if (request.description() != null)
            task.setDescription(request.description());

        if (request.statusId() != null)
            task.setStatusId(request.statusId());

        if (request.assigneeId() != null)
            task.setAssigneeId(request.assigneeId());

        if (request.priority() != null)
            task.setPriority(request.priority());

        if (request.startDate() != null)
            task.setStartDate(request.startDate());

        if (request.dueDate() != null)
            task.setDueDate(request.dueDate());

        task.setUpdatedAt(Instant.now());

        return mapToResponse(taskRepository.save(task));
    }


    public void deleteTask(UUID taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        boolean hasChildren = taskRepository.existsByParentTaskId(taskId);
        if (hasChildren) {
            throw new IllegalStateException("Cannot delete task with child tasks");
        }

        taskRepository.delete(task);
    }


    public List<TaskResponse> getTasksByUser(UUID userId) {
        return taskRepository.findByAssigneeId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<TaskResponse> getTasksByProject(UUID projectId) {
        validateProjectExists(projectId);
        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<TaskResponse> getSubtasks(UUID parentTaskId) {
        return taskRepository.findByParentTaskId(parentTaskId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void validateProjectExists(UUID projectId) {
        try {
            ProjectDTO project = projectServiceClient.getProjectById(projectId);
            if (project == null) {
                throw new ProjectNotFoundException("Project with ID " + projectId + " does not exist");
            }
        } catch (FeignException.NotFound e) {
            throw new ProjectNotFoundException("Project with ID " + projectId + " does not exist");
        }
    }

}

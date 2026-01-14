package com.taskflow.tms.controllers;

import com.taskflow.tms.dtos.CreateTaskRequest;
import com.taskflow.tms.dtos.MoveTaskRequest;
import com.taskflow.tms.dtos.TaskResponse;
import com.taskflow.tms.dtos.UpdateTaskRequest;
import com.taskflow.tms.entities.Task;
import com.taskflow.tms.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name="Tasks",description = "Taks Management Api's")
public class TaskController {
   private final TaskService taskService;

   @Operation(summary = "Create a task (Epic / User Story / Subtask)")
   @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody  CreateTaskRequest createTaskRequest){
       return ResponseEntity.ok(taskService.createTask(createTaskRequest));
   }

   @Operation(summary="Get task by ID")
   @GetMapping("/{id}")
   public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID id){
      return ResponseEntity.ok(taskService.getTaskById(id));
   }

   @Operation(summary="Get subtasks by parent ID")
   @GetMapping("/{id}/subtasks")
   public ResponseEntity<List<TaskResponse>> getSubtasks(@PathVariable UUID id){
       return ResponseEntity.ok(taskService.getSubtasks(id));
   }


   @Operation(summary="Get All tasks")
   @GetMapping("/all")
   public ResponseEntity<List<Task>> getAll(){
      return ResponseEntity.ok(taskService.getAllTasks());
   }
   @Operation(summary = "Update task by ID")
   @PatchMapping("/{taskId}")
   public ResponseEntity<TaskResponse> updateTask(
           @PathVariable UUID taskId,
           @RequestBody UpdateTaskRequest request
   ) {
      return ResponseEntity.ok(taskService.updateTask(taskId, request));
   }


   @Operation(summary = "Delete task by ID")
   @DeleteMapping("/{taskId}")
   public ResponseEntity<Void> deleteTask(@PathVariable UUID taskId) {
      taskService.deleteTask(taskId);
      return ResponseEntity.noContent().build();
   }

   @Operation(summary = "Get tasks assigned to a user")
   @GetMapping(params = "assigneeId")
   public List<TaskResponse> getTasksByUser(
           @Parameter(
                   description = "User UUID",
                   example = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                   schema = @Schema(type = "string", format = "uuid")
           )
           @RequestParam UUID assigneeId
   ) {
      return taskService.getTasksByUser(assigneeId);
   }

   @Operation(summary = "Get tasks by project ID")
   @GetMapping(params = "projectId")
   public List<TaskResponse> getTasksByProject(@RequestParam UUID projectId) {
      return taskService.getTasksByProject(projectId);
   }



   @PutMapping("/{taskId}/move")
   public void moveTask(
           @PathVariable UUID taskId,
           @RequestBody MoveTaskRequest request
   ) {
      taskService.moveTask(taskId, request);
   }


}

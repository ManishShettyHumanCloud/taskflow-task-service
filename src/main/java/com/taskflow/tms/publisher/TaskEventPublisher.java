package com.taskflow.tms.publisher;

import com.taskflow.tms.dto.events.TaskAssignedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskEventPublisher {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    /**
     * Publish task assigned event to Kafka
     */
    public void publishTaskAssigned(UUID taskId, UUID assigneeId, String taskTitle, 
                                     String assignedBy, UUID projectId) {
        try {
            TaskAssignedEvent event = new TaskAssignedEvent(
                    taskId,
                    assigneeId,
                    taskTitle,
                    assignedBy,
                    projectId,
                    LocalDateTime.now()
            );
            
            log.info("Publishing TaskAssignedEvent for task: {} to user: {}", taskId, assigneeId);
            kafkaTemplate.send("task-assigned", event);
            log.info("Successfully published TaskAssignedEvent to Kafka");
            
        } catch (Exception e) {
            log.error("Failed to publish TaskAssignedEvent: {}", e.getMessage(), e);
        }
    }
}

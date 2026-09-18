package com.anjali.smarttask.dto;
import com.anjali.smarttask.model.*;
import java.time.Instant;
public record TaskResponse(Long id, String title, String description, TaskPriority priority,
                           TaskStatus status, Instant createdAt, Instant updatedAt) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(),
            task.getPriority(), task.getStatus(), task.getCreatedAt(), task.getUpdatedAt());
    }
}

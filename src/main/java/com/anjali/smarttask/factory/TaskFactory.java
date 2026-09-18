package com.anjali.smarttask.factory;
import com.anjali.smarttask.dto.TaskRequest;
import com.anjali.smarttask.model.*;
import org.springframework.stereotype.Component;
@Component
public class TaskFactory {
    public Task create(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title().trim());
        task.setDescription(request.description());
        task.setPriority(request.priority());
        task.setStatus(TaskStatus.TODO);
        return task;
    }
}

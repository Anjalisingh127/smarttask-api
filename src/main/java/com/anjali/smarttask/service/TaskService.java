package com.anjali.smarttask.service;
import com.anjali.smarttask.dto.*;
import com.anjali.smarttask.model.*;
import java.util.List;
public interface TaskService {
    TaskResponse create(TaskRequest request);
    List<TaskResponse> list(TaskStatus status, TaskPriority priority);
    TaskResponse get(Long id);
    TaskResponse update(Long id, TaskRequest request);
    TaskResponse updateStatus(Long id, TaskStatus status);
    void delete(Long id);
}

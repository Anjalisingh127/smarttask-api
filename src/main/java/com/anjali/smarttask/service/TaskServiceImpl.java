package com.anjali.smarttask.service;
import com.anjali.smarttask.dto.*;
import com.anjali.smarttask.exception.TaskNotFoundException;
import com.anjali.smarttask.factory.TaskFactory;
import com.anjali.smarttask.model.*;
import com.anjali.smarttask.repository.TaskRepository;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final TaskFactory factory;
    public TaskServiceImpl(TaskRepository repository, TaskFactory factory) {
        this.repository = repository; this.factory = factory;
    }
    public TaskResponse create(TaskRequest request) {
        return TaskResponse.from(repository.saveAndFlush(factory.create(request)));
    }
    @Transactional(readOnly = true)
    public List<TaskResponse> list(TaskStatus status, TaskPriority priority) {
        Specification<Task> filter = (root, query, cb) -> cb.conjunction();
        if (status != null) filter = filter.and((root, query, cb) -> cb.equal(root.get("status"), status));
        if (priority != null) filter = filter.and((root, query, cb) -> cb.equal(root.get("priority"), priority));
        return repository.findAll(filter).stream().map(TaskResponse::from).toList();
    }
    @Transactional(readOnly = true)
    public TaskResponse get(Long id) { return TaskResponse.from(find(id)); }
    public TaskResponse update(Long id, TaskRequest request) {
        Task task = find(id);
        task.setTitle(request.title().trim()); task.setDescription(request.description());
        task.setPriority(request.priority());
        return TaskResponse.from(repository.saveAndFlush(task));
    }
    public TaskResponse updateStatus(Long id, TaskStatus status) {
        Task task = find(id);
        task.setStatus(status);
        return TaskResponse.from(repository.saveAndFlush(task));
    }
    public void delete(Long id) { repository.delete(find(id)); }
    private Task find(Long id) {
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }
}

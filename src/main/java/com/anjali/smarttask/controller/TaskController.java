package com.anjali.smarttask.controller;
import com.anjali.smarttask.dto.*;
import com.anjali.smarttask.model.*;
import com.anjali.smarttask.service.TaskService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService service;
    public TaskController(TaskService service) { this.service = service; }
    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) {
        TaskResponse saved = service.create(request);
        return ResponseEntity.created(URI.create("/api/tasks/" + saved.id())).body(saved);
    }
    @GetMapping
    public List<TaskResponse> list(@RequestParam(required = false) TaskStatus status,
                                   @RequestParam(required = false) TaskPriority priority) {
        return service.list(status, priority);
    }
    @GetMapping("/{id}") public TaskResponse get(@PathVariable Long id) { return service.get(id); }
    @PutMapping("/{id}") public TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return service.update(id, request);
    }
    @PatchMapping("/{id}/status") public TaskResponse status(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
        return service.updateStatus(id, request.status());
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id); return ResponseEntity.noContent().build();
    }
}

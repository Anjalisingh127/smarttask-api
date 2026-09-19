package com.anjali.smarttask.controller;
import com.anjali.smarttask.dto.*;
import com.anjali.smarttask.exception.GlobalExceptionHandler.ApiError;
import com.anjali.smarttask.model.*;
import com.anjali.smarttask.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Create, retrieve, filter, update, and delete tasks")
public class TaskController {
    private static final String BAD_REQUEST_EXAMPLE = """
        {"status":400,"message":"title: must not be blank","timestamp":"2026-09-20T00:00:00Z"}
        """;
    private static final String NOT_FOUND_EXAMPLE = """
        {"status":404,"message":"Task not found: 999","timestamp":"2026-09-20T00:00:00Z"}
        """;
    private final TaskService service;
    public TaskController(TaskService service) { this.service = service; }
    @PostMapping
    @Operation(summary = "Create a task")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Task created",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TaskResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiError.class),
                examples = @ExampleObject(value = BAD_REQUEST_EXAMPLE)))
    })
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) {
        TaskResponse saved = service.create(request);
        return ResponseEntity.created(URI.create("/api/tasks/" + saved.id())).body(saved);
    }
    @GetMapping
    @Operation(summary = "List tasks", description = "Optionally filter by status, priority, or both")
    @ApiResponse(responseCode = "200", description = "Tasks returned")
    public List<TaskResponse> list(@RequestParam(required = false) TaskStatus status,
                                   @RequestParam(required = false) TaskPriority priority) {
        return service.list(status, priority);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Task returned",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TaskResponse.class))),
        @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiError.class),
                examples = @ExampleObject(value = NOT_FOUND_EXAMPLE)))
    })
    public TaskResponse get(@PathVariable Long id) { return service.get(id); }
    @PutMapping("/{id}")
    @Operation(summary = "Update a task")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Task updated",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TaskResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiError.class),
                examples = @ExampleObject(value = BAD_REQUEST_EXAMPLE))),
        @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiError.class),
                examples = @ExampleObject(value = NOT_FOUND_EXAMPLE)))
    })
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return service.update(id, request);
    }
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update a task status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status updated",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TaskResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid status",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiError.class),
                examples = @ExampleObject(value = BAD_REQUEST_EXAMPLE))),
        @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiError.class),
                examples = @ExampleObject(value = NOT_FOUND_EXAMPLE)))
    })
    public TaskResponse status(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
        return service.updateStatus(id, request.status());
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Task deleted"),
        @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiError.class),
                examples = @ExampleObject(value = NOT_FOUND_EXAMPLE)))
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id); return ResponseEntity.noContent().build();
    }
}

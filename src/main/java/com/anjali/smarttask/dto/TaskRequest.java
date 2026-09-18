package com.anjali.smarttask.dto;
import com.anjali.smarttask.model.TaskPriority;
import jakarta.validation.constraints.*;
public record TaskRequest(
    @NotBlank @Size(max = 120) String title,
    @Size(max = 2000) String description,
    @NotNull TaskPriority priority
) {}

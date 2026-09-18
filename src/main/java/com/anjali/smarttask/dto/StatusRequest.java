package com.anjali.smarttask.dto;
import com.anjali.smarttask.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
public record StatusRequest(@NotNull TaskStatus status) {}

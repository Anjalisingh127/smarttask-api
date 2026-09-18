package com.anjali.smarttask.service;
import com.anjali.smarttask.dto.*;
import com.anjali.smarttask.exception.TaskNotFoundException;
import com.anjali.smarttask.factory.TaskFactory;
import com.anjali.smarttask.model.*;
import com.anjali.smarttask.repository.TaskRepository;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock TaskRepository repository;
    TaskServiceImpl service;
    @BeforeEach void setup() { service = new TaskServiceImpl(repository, new TaskFactory()); }
    Task task() { Task task = new Task(); task.setTitle("Fix API"); task.setPriority(TaskPriority.HIGH); task.setStatus(TaskStatus.TODO); return task; }
    TaskRequest request() { return new TaskRequest("  Fix API  ", "Document", TaskPriority.HIGH); }
    @Test void createAppliesDefaultsAndTrimsTitle() {
        when(repository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));
        TaskResponse response = service.create(request());
        assertEquals("Fix API", response.title()); assertEquals(TaskStatus.TODO, response.status());
        verify(repository).saveAndFlush(any(Task.class));
    }
    @Test void getExistingTask() { when(repository.findById(1L)).thenReturn(Optional.of(task())); assertEquals("Fix API", service.get(1L).title()); }
    @Test void getMissingTask() { when(repository.findById(9L)).thenReturn(Optional.empty()); assertThrows(TaskNotFoundException.class, () -> service.get(9L)); }
    @Test void updateExistingTask() {
        when(repository.findById(1L)).thenReturn(Optional.of(task()));
        when(repository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));
        assertEquals("Document", service.update(1L, request()).description());
    }
    @Test void updateMissingTask() { assertThrows(TaskNotFoundException.class, () -> service.update(9L, request())); }
    @Test void statusChanges() {
        when(repository.findById(1L)).thenReturn(Optional.of(task()));
        when(repository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));
        assertEquals(TaskStatus.COMPLETED, service.updateStatus(1L, TaskStatus.COMPLETED).status());
    }
    @Test void statusMissingTask() { assertThrows(TaskNotFoundException.class, () -> service.updateStatus(9L, TaskStatus.TODO)); }
    @Test void deleteExistingTask() { Task task = task(); when(repository.findById(1L)).thenReturn(Optional.of(task)); service.delete(1L); verify(repository).delete(task); }
    @Test void deleteMissingTask() { assertThrows(TaskNotFoundException.class, () -> service.delete(9L)); verify(repository, never()).delete(any()); }
    @Test void listWithoutFilters() { when(repository.findAll(ArgumentMatchers.<Specification<Task>>any())).thenReturn(List.of(task())); assertEquals(1, service.list(null, null).size()); }
    @Test void listWithStatusFilter() { when(repository.findAll(ArgumentMatchers.<Specification<Task>>any())).thenReturn(List.of(task())); assertEquals(TaskStatus.TODO, service.list(TaskStatus.TODO, null).get(0).status()); }
    @Test void listWithPriorityFilter() { when(repository.findAll(ArgumentMatchers.<Specification<Task>>any())).thenReturn(List.of(task())); assertEquals(TaskPriority.HIGH, service.list(null, TaskPriority.HIGH).get(0).priority()); }
    @Test void listWithBothFiltersEmpty() { when(repository.findAll(ArgumentMatchers.<Specification<Task>>any())).thenReturn(List.of()); assertTrue(service.list(TaskStatus.TODO, TaskPriority.LOW).isEmpty()); }
}

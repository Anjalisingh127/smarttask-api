package com.anjali.smarttask;
import com.anjali.smarttask.controller.TaskController;
import com.anjali.smarttask.dto.*;
import com.anjali.smarttask.exception.*;
import com.anjali.smarttask.model.*;
import com.anjali.smarttask.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
class TaskControllerTest {
    TaskService service = mock(TaskService.class);
    MockMvc mvc;
    @BeforeEach void setup() { reset(service); mvc = MockMvcBuilders.standaloneSetup(new TaskController(service)).setControllerAdvice(new GlobalExceptionHandler()).build(); }
    TaskResponse sample() { return new TaskResponse(1L, "Fix API", null, TaskPriority.HIGH, TaskStatus.TODO, null, null); }
    @Test void createReturns201AndLocation() throws Exception {
        when(service.create(any())).thenReturn(sample());
        mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"Fix API\",\"priority\":\"HIGH\"}"))
            .andExpect(status().isCreated()).andExpect(header().string("Location", "/api/tasks/1"));
    }
    @Test void blankTitleReturns400() throws Exception {
        mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\" \" ,\"priority\":\"HIGH\"}"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").exists()); verifyNoInteractions(service);
    }
    @Test void missingPriorityReturns400() throws Exception {
        mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"Fix API\"}")).andExpect(status().isBadRequest());
    }
    @Test void invalidPriorityReturns400() throws Exception {
        mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"Fix API\",\"priority\":\"URGENT\"}")).andExpect(status().isBadRequest());
    }
    @Test void getMissingReturns404() throws Exception { when(service.get(9L)).thenThrow(new TaskNotFoundException(9L)); mvc.perform(get("/api/tasks/9")).andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404)); }
    @Test void getExistingReturns200() throws Exception { when(service.get(1L)).thenReturn(sample()); mvc.perform(get("/api/tasks/1")).andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Fix API")); }
    @Test void updateValidReturns200() throws Exception { when(service.update(eq(1L), any())).thenReturn(sample()); mvc.perform(put("/api/tasks/1").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"Fix API\",\"priority\":\"HIGH\"}")).andExpect(status().isOk()); }
    @Test void missingStatusReturns400() throws Exception { mvc.perform(patch("/api/tasks/1/status").contentType(MediaType.APPLICATION_JSON).content("{}" )).andExpect(status().isBadRequest()); }
    @Test void deleteReturns204() throws Exception { mvc.perform(delete("/api/tasks/1")).andExpect(status().isNoContent()); verify(service).delete(1L); }
}

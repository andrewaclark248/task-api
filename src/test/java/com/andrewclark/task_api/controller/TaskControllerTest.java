package com.andrewclark.task_api.controller;

import com.andrewclark.task_api.dto.CreateTaskRequest;
import com.andrewclark.task_api.entity.Task;
import com.andrewclark.task_api.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldGetAllTasks() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Learn Spring Testing");
        task.setCompleted(false);

        when(taskService.getAllTasks())
                .thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Learn Spring Testing"))
                .andExpect(jsonPath("$[0].completed").value(false));

        verify(taskService).getAllTasks();
    }

    @Test
    void shouldGetTaskById() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Deploy to ECS");
        task.setCompleted(false);

        when(taskService.getTask(1L))
                .thenReturn(task);

        mockMvc.perform(get("/api/tasks/1").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Deploy to ECS"))
                .andExpect(jsonPath("$.completed").value(false));

        verify(taskService).getTask(1L);
    }

    @Test
    void shouldCreateTask() throws Exception {
        Task createdTask = new Task();
        createdTask.setId(1L);
        createdTask.setTitle("Learn Docker");
        createdTask.setCompleted(false);

        when(taskService.createTask(any(CreateTaskRequest.class)))
                .thenReturn(createdTask);

        mockMvc.perform(post("/api/tasks").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Learn Docker",
                                    "completed": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Learn Docker"))
                .andExpect(jsonPath("$.completed").value(false));

        verify(taskService).createTask(any(CreateTaskRequest.class));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Learn Kubernetes");
        updatedTask.setCompleted(true);

        when(taskService.updateTask(eq(1L), any()))
                .thenReturn(updatedTask);

        mockMvc.perform(put("/api/tasks/1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Learn Kubernetes",
                                    "completed": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Learn Kubernetes"))
                .andExpect(jsonPath("$.completed").value(true));

        verify(taskService).updateTask(eq(1L), any());
    }

    @Test
    void shouldDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1").with(jwt()))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(1L);
    }
}
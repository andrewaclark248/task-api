package com.andrewclark.task_api.integration;

import com.andrewclark.task_api.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void createTask_thenGetAllTasks_returnsCreatedTask() throws Exception {

        // Create a task
        mockMvc.perform(post("/api/tasks")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Learn Spring Boot"
                                }
                                """))
                .andExpect(status().isOk());

        // Retrieve all tasks
        mockMvc.perform(get("/api/tasks")
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title")
                        .value("Learn Spring Boot"));
    }
}
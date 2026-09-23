package com.andrewclark.task_api.service;

import com.andrewclark.task_api.dto.CreateTaskRequest;
import com.andrewclark.task_api.dto.UpdateTaskRequest;
import com.andrewclark.task_api.entity.Task;
import com.andrewclark.task_api.exception.TaskNotFoundException;
import com.andrewclark.task_api.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository);
    }

    @Test
    void getAllTasks_returnsTasks() {
        Task task = new Task();
        task.setTitle("Learn Spring");

        when(taskRepository.findAll())
                .thenReturn(List.of(task));

        List<Task> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals("Learn Spring", result.get(0).getTitle());

        verify(taskRepository).findAll();
    }

    @Test
    void getTask_returnsTask_whenTaskExists() {
        Task task = new Task();
        task.setTitle("Learn Spring");

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        Task result = taskService.getTask(1L);

        assertEquals("Learn Spring", result.getTitle());

        verify(taskRepository).findById(1L);
    }

    @Test
    void getTask_throwsTaskNotFoundException_whenTaskDoesNotExist() {
        when(taskRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTask(1L)
        );

        verify(taskRepository).findById(1L);
    }

    @Test
    void createTask_savesAndReturnsTask() {
        CreateTaskRequest request =
                new CreateTaskRequest("Learn Spring");

        Task savedTask = new Task();
        savedTask.setTitle("Learn Spring");

        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        Task result = taskService.createTask(request);

        assertEquals("Learn Spring", result.getTitle());

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateTask_updatesAndReturnsTask() {
        Task existingTask = new Task();
        existingTask.setTitle("Old title");

        UpdateTaskRequest request =
                new UpdateTaskRequest("New title");

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existingTask));

        when(taskRepository.save(existingTask))
                .thenReturn(existingTask);

        Task result = taskService.updateTask(1L, request);

        assertEquals("New title", result.getTitle());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(existingTask);
    }

    @Test
    void deleteTask_deletesTask() {
        Task task = new Task();
        task.setTitle("Learn Spring");

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository).findById(1L);
        verify(taskRepository).delete(task);
    }
}
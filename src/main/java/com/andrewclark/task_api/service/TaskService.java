package com.andrewclark.task_api.service;
import com.andrewclark.task_api.dto.CreateTaskRequest;
import com.andrewclark.task_api.dto.UpdateTaskRequest;
import com.andrewclark.task_api.exception.TaskNotFoundException;
import com.andrewclark.task_api.repository.TaskRepository;
import org.springframework.stereotype.Service;
import com.andrewclark.task_api.entity.Task;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title());

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, UpdateTaskRequest updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(updatedTask.title());

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(task);
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not foudn wiht id = " + id.toString()));
    }

}
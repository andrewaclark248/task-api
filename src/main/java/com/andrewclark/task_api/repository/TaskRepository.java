package com.andrewclark.task_api.repository;
import com.andrewclark.task_api.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
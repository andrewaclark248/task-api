package com.andrewclark.task_api.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
        @NotBlank
        @Size(max = 100)
        String title

        // @Size(max = 500)
        //String description
) {}

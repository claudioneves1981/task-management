package com.example.taskmanagement.api;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@Data
public class TaskDto {

    private UUID id;

    @NotEmpty(message = "title should not be null")
    private String title;

    private Instant createdOn;

    private Instant updatedOn;

    @NotEmpty(message = "Expire date cannot empty, please inform!")
    private Instant expiredOn;

    private Priority priority;

    private Status status;

    @NotEmpty(message = "Description is mandatory")
    private String description;

}

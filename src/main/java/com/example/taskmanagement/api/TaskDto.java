package com.example.taskmanagement.api;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TaskDto {

    private UUID id;

    @NotEmpty(message = "title should not be null")
    private String title;

    private String createdOn;

    private String updatedOn;

    @NotEmpty(message = "Expire date cannot empty, please inform!")
    private String expireOn;

    private Priority priority;

    private Status status;

    @NotEmpty(message = "Description is mandatory")
    private String description;

    private String statusClass;
    private String priorityClass;



}

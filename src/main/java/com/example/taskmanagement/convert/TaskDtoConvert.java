package com.example.taskmanagement.convert;


import com.example.taskmanagement.api.Priority;
import com.example.taskmanagement.api.Status;
import com.example.taskmanagement.api.TaskDto;
import com.example.taskmanagement.db.entity.TaskEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TaskDtoConvert {

    private TaskDto taskDto;

    public TaskDtoConvert(TaskEntity taskEntity) {
        taskDto = convertTaskEntityToTaskDto(taskEntity);
    }

    public TaskDto convertTaskEntityToTaskDto(TaskEntity taskEntity) {

        return TaskDto.builder()
                .id(taskEntity.getId())
                .title(taskEntity.getTitle())
                .status(taskEntity.getStatus())
                .priority(taskEntity.getPriority())
                .createdOn(convertInstantToString(taskEntity.getCreatedOn()))
                .expireOn(convertInstantToString(taskEntity.getExpiredOn()))
                .description(taskEntity.getDescription())
                .updatedOn(convertInstantToString(taskEntity.getUpdatedOn()))
                .statusClass(getStatusClass(taskEntity.getStatus()))
                .priorityClass(getPriorityClass(taskEntity.getPriority()))
                .build();
    }

    private String convertInstantToString(final Instant dateInstant) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(Date.from(dateInstant));
    }

    private String getStatusClass(Status status) {
        return switch (status) {
            case Ready, Progress -> "badge badge-primary";
            case Done -> "badge badge-success";
            default -> "badge badge-secondary";
        };
    }

    private String getPriorityClass(Priority priority){
        return switch(priority){
            case Low, Normal -> "badge badge-primary";
            case High -> "badge badge-danger";
            default -> "badge badge-secondary";
        };
    }

}

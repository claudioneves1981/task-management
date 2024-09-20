package com.example.taskmanagement.convert;

import com.example.taskmanagement.api.Priority;
import com.example.taskmanagement.api.Status;
import com.example.taskmanagement.api.TaskDto;
import com.example.taskmanagement.db.entity.TaskEntity;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Component
public class TaskConvert {

    public TaskEntity convertTaskDtoToTaskEntity(final TaskDto taskDto){

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskDto.getTitle());
        taskEntity.setStatus(taskDto.getStatus());
        taskEntity.setPriority(taskDto.getPriority());
        taskEntity.setExpiredOn(convertStringToInstant(taskDto.getExpireOn()));
        taskEntity.setDescription(taskDto.getDescription());
        return taskEntity;

    }

    public TaskDto convertTaskEntityToTaskDto(final TaskEntity taskEntity){

        TaskDto taskDto = new TaskDto();
        taskDto.setId(taskEntity.getId());
        taskDto.setTitle(taskEntity.getTitle());
        taskDto.setStatus(taskEntity.getStatus());
        taskDto.setPriority(taskEntity.getPriority());
        taskDto.setCreatedOn(convertInstantToString(taskEntity.getCreatedOn()));
        taskDto.setExpireOn(convertInstantToString(taskEntity.getExpiredOn()));
        taskDto.setDescription(taskEntity.getDescription());
        taskDto.setUpdatedOn(convertInstantToString(taskEntity.getUpdatedOn()));
        taskDto.setStatusClass(getStatusClass(taskEntity.getStatus()));
        taskDto.setPriorityClass(getPriorityClass(taskEntity.getPriority()));
        return taskDto;

    }

    private String convertInstantToString(final Instant dateInstant){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(Date.from(dateInstant));
    }

    public Instant convertStringToInstant(final String dateString){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateString);
            return date.toInstant();
        }catch (ParseException pe){
            throw new IllegalArgumentException("Error during date parse "+ pe.getMessage());
        }
    }

    private String getStatusClass(Status status){
        return switch(status){
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

    public Status convertStatus(String status){

        switch(status){
            case "Ready" -> {
                return Status.Ready;
            }
            case "Progress" -> {
                return Status.Progress;
            }
            case "Done" -> {
                return Status.Done;
            }
        }

        return null;
    }


}

package com.example.taskmanagement.convert;

import com.example.taskmanagement.api.TaskDto;
import com.example.taskmanagement.db.entity.TaskEntity;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;


@Component
public class TaskEntityConvert {

    public TaskEntity convertTaskDtoToTaskEntity(TaskDto taskDto) {

        return TaskEntity.builder()
                .title(taskDto.getTitle())
                .status(taskDto.getStatus())
                .priority(taskDto.getPriority())
                .expiredOn(convertStringToInstant(taskDto.getExpireOn()))
                .description(taskDto.getDescription())
                .build();
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
}

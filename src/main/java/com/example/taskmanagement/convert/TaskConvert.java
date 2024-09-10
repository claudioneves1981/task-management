package com.example.taskmanagement.convert;

import com.example.taskmanagement.api.TaskDto;
import com.example.taskmanagement.db.entity.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskConvert {

    public TaskEntity convertTaskDtoToTaskEntity(final TaskDto taskDto){

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskDto.getTitle());
        taskEntity.setStatus(taskDto.getStatus());
        taskEntity.setPriority(taskDto.getPriority());
        taskEntity.setCreatedOn(taskDto.getCreatedOn());
        taskEntity.setExpiredOn(taskDto.getExpiredOn());
        taskEntity.setDescription(taskDto.getDescription());
        taskEntity.setUpdatedOn(taskDto.getUpdatedOn());
        return taskEntity;

    }

    public TaskDto convertTaskEntityToTaskDto(final TaskEntity taskEntity){

        TaskDto taskDto = new TaskDto();
        taskDto.setId(taskEntity.getId());
        taskDto.setTitle(taskEntity.getTitle());
        taskDto.setStatus(taskEntity.getStatus());
        taskDto.setPriority(taskEntity.getPriority());
        taskDto.setCreatedOn(taskEntity.getCreatedOn());
        taskDto.setExpiredOn(taskEntity.getExpiredOn());
        taskDto.setDescription(taskEntity.getDescription());
        taskDto.setUpdatedOn(taskEntity.getUpdatedOn());
        return taskDto;

    }



}

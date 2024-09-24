package com.example.taskmanagement.service;

import com.example.taskmanagement.api.Priority;
import com.example.taskmanagement.api.Status;
import com.example.taskmanagement.api.TaskDto;
import com.example.taskmanagement.convert.TaskDtoConvert;
import com.example.taskmanagement.convert.TaskEntityConvert;
import com.example.taskmanagement.db.entity.TaskEntity;
import com.example.taskmanagement.db.repository.TaskRepository;
import com.example.taskmanagement.exceptions.TaskNotFoundException;
import com.example.taskmanagement.convert.StatusConvert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {


    private final TaskRepository taskRepository;
    private final StatusConvert statusConvert;
    private final TaskDtoConvert taskDtoConvert;
    private final TaskEntityConvert taskEntityConvert;

    public TaskService(final TaskRepository taskRepository, final StatusConvert statusConvert, final TaskDtoConvert taskDtoConvert, final TaskEntityConvert taskEntityConvert){
        this.taskRepository = taskRepository;
        this.statusConvert = statusConvert;
        this.taskDtoConvert = taskDtoConvert;
        this.taskEntityConvert = taskEntityConvert;
    }

    /* @PostConstruct
    private void generateRandomTask(){

       TaskDto taskDto = new TaskDto();
        taskDto.setTitle("teste2");
        taskDto.setDescription("teste2 teste2");
        taskDto.setStatus(Status.Progress);
        taskDto.setPriority(Priority.Low);
        taskDto.setUpdatedOn(Instant.now());
        taskDto.setExpiredOn(Instant.now());
        taskDto.setCreatedOn(Instant.now());
        saveTask(taskDto);

    } */

    public void saveTask(final TaskDto taskDto){
        try {
            final TaskEntity taskEntity = taskEntityConvert.convertTaskDtoToTaskEntity(taskDto);
            taskRepository.save(taskEntity);
        } catch(RuntimeException re){
            throw re;
        }
    }

    public TaskDto getTaskById(final UUID id){
        final Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(id);
        if(optionalTaskEntity.isPresent()){
            return taskDtoConvert.convertTaskEntityToTaskDto(optionalTaskEntity.get());
        }else{
            throw new TaskNotFoundException("Task with id "+ id +" not found");
        }
    }

    public void deleteTask(final UUID id){
        taskRepository.deleteById(id);
    }

    public void updateTask(final TaskDto taskDto){

        try {
            final Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(taskDto.getId());
            if (optionalTaskEntity.isPresent()) {
                TaskEntity taskEntity = optionalTaskEntity.get();
                taskEntity.setDescription(taskDto.getDescription());
                taskEntity.setTitle(taskDto.getTitle());
                taskEntity.setExpiredOn(taskEntityConvert.convertStringToInstant(taskDto.getExpireOn()));
                taskEntity.setStatus(taskDto.getStatus());
                taskEntity.setPriority(taskDto.getPriority());
                taskRepository.save(taskEntity);
            } else {
                throw new TaskNotFoundException("Task with id " + taskDto.getId() + " not found");
            }
        }catch(final RuntimeException re) {
            throw re;
        }

    }

    public List<TaskDto> getTaskList(){
        return taskRepository.findAllByOrderByCreatedOnDesc()
                .stream()
                .map(taskDtoConvert::convertTaskEntityToTaskDto)
                .collect(Collectors.toList());
    }

    public List<String> getPriorities(){
        return Arrays.asList(Priority.Low.toString(),Priority.High.toString(), Priority.Normal.toString());
    }

    public List<String> getStatus(){
        return Arrays.asList(Status.Done.toString(),Status.Ready.toString(), Status.Progress.toString());
    }

    public List<TaskDto> getTaskListByStatus(String strStatus){
        Status status = statusConvert.convertStatus(strStatus);
        if(status == null){
                return getTaskList();
        }

        List<TaskEntity> taskEntityList = taskRepository.findAllByStatusOrderByCreatedOnDesc(status);
        return taskEntityList.stream()
                .map(taskDtoConvert::convertTaskEntityToTaskDto)
                .collect(Collectors.toList());
    }

    public Page<TaskDto> getTaskListPaginated(int pageNo, int pageSize, String status){

        List<TaskDto> taskDtoList;
        Page<TaskDto> page;

        if(status == null || status.isEmpty() || status.equals("all")){
            taskDtoList = getTaskList();
        }else{
            taskDtoList = getTaskListByStatus(status);
            pageNo = 1;
        }


        if(!taskDtoList.isEmpty()){
            if(taskDtoList.size() < pageSize){
                pageSize = taskDtoList.size();
            }
            Pageable pageable = PageRequest.of(pageNo -1, pageSize);
            int start = (int)pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), taskDtoList.size());
            List<TaskDto> subList = taskDtoList.subList(start, end);
            page = new PageImpl<>(subList, pageable, taskDtoList.size());
        }else{
            page = Page.empty();
        }

        return page;
    }

}

package com.example.taskmanagement.db.repository;

import com.example.taskmanagement.db.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

List<TaskEntity> findAllByOrderByCreatedOnDesc();


}

package com.anjali.smarttask.repository;
import com.anjali.smarttask.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {}

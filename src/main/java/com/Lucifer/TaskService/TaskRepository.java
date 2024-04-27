package com.Lucifer.TaskService;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
	public List<Task> findByAssignedUserId(Long userId);
}

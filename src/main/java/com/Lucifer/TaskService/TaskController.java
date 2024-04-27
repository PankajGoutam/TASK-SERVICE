package com.Lucifer.TaskService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/tasks")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserService userService;

	@CrossOrigin
	@PostMapping
	public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestHeader("Authorization") String jwt)
			throws Exception {
		if (jwt == null) {
			throw new Exception("jwt required...");
		}
		User user = userService.getUserProfile(jwt);
		Task createdTask = taskService.createTask(task, user.getRole());
		return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
	}

	@CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id,
                                            @RequestHeader("Authorization") String jwt) throws Exception {
        if(jwt==null){
            throw new Exception("jwt required...");
        }
        Task task = taskService.getTaskById(id);
        return task != null ? new ResponseEntity<>(task, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

	@CrossOrigin
    @GetMapping("/user")
    //@RequestParam(required = false) String sortByDeadline,
    //@RequestParam(required = false) String sortByCreatedAt
    public ResponseEntity< List<Task> > getAssignedUsersTask(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) TaskStatus status) throws Exception {
        User user=userService.getUserProfile(jwt);
        List<Task> tasks = taskService.assignedUsersTask(user.getId(),status);//, sortByDeadline, sortByCreatedAt
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

	@CrossOrigin
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) TaskStatus status
           // @RequestParam(required = false) String sortByDeadline,
           // @RequestParam(required = false) String sortByCreatedAt
    ) throws Exception {
        if(jwt==null){
            throw new Exception("jwt required...");
        }
        List<Task> tasks = taskService.getAllTasks(status);//sortByDeadline, sortByCreatedAt
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

	@CrossOrigin
    @PutMapping("/{id}/user/{userId}/assigned")
    public ResponseEntity<Task> assignedTaskToUser(
            @PathVariable Long id,
            @PathVariable Long userId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.getUserProfile(jwt);
        Task task = taskService.assignedToUser(userId,id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

	@CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody Task req,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        if(jwt==null){
            throw new Exception("jwt required...");
        }
        User user=userService.getUserProfile(jwt);
        Task task = taskService.updateTask(id, req, user.getId());
        return task != null ? new ResponseEntity<>(task, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

	@CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) throws Exception {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

	@CrossOrigin
    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable Long id) throws Exception {
        Task task = taskService.completeTask(id);
        return new ResponseEntity<>(task, HttpStatus.NO_CONTENT);
    }
}

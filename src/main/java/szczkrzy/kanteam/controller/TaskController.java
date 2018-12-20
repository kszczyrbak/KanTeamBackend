package szczkrzy.kanteam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szczkrzy.kanteam.model.entities.KTSubtask;
import szczkrzy.kanteam.model.entities.KTTask;
import szczkrzy.kanteam.model.requests.CommentCreateRequest;
import szczkrzy.kanteam.services.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        return taskService.getById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody KTTask task) {
        return taskService.create(task);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return taskService.getAll();
    }

    @PutMapping
    public ResponseEntity update(@RequestBody KTTask task) {
        return taskService.update(task);
    }

    @GetMapping("/colors")
    public ResponseEntity getColors() {
        return taskService.getColors();
    }

    @PutMapping("/{id}/users/add")
    public ResponseEntity addUser(@PathVariable int id, @RequestBody int userId) {
        return taskService.addUser(id, userId);
    }

    @PutMapping("/{id}/users/remove")
    public ResponseEntity removeUser(@PathVariable int id, @RequestBody int userId) {
        return taskService.removeUser(id, userId);
    }

    @PutMapping("/{id}/comments/add")
    public ResponseEntity addComment(@PathVariable int id, @RequestBody CommentCreateRequest comment) {
        return taskService.addComment(id, comment);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity getUsers(@PathVariable int id) {
        return taskService.getUsers(id);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody KTTask task) {
        return taskService.remove(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable int id) {
        return taskService.removeByid(id);
    }

    @PutMapping("/subtasks")
    public ResponseEntity updateSubtask(@RequestBody KTSubtask subtask) {
        return taskService.updateSubtask(subtask);
    }

    @PostMapping("/{id}/subtasks")
    public ResponseEntity<?> addSubtask(@PathVariable int id, @RequestBody KTSubtask subtask) {
        return taskService.addSubtask(id, subtask);
    }

}

package szczkrzy.kanteam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szczkrzy.kanteam.model.entity.KTTask;
import szczkrzy.kanteam.service.TaskService;

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

    @PutMapping("/{id}/users")
    public ResponseEntity addUser(@PathVariable int id, @RequestBody int userId) {
        return taskService.addUser(id, userId);
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
}

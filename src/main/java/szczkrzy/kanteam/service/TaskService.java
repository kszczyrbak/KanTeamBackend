package szczkrzy.kanteam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entity.KTTask;
import szczkrzy.kanteam.model.entity.KTUser;
import szczkrzy.kanteam.repository.TaskRepository;
import szczkrzy.kanteam.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity getById(int id) {
        Optional<KTTask> task = taskRepository.findById(id);
        if (!task.isPresent())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(task);
    }

    public ResponseEntity create(KTTask task) {
        KTTask newTask = taskRepository.save(task);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    public ResponseEntity getAll() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    public ResponseEntity<?> update(KTTask task) {
        KTTask newBoard = taskRepository.save(task);
        return new ResponseEntity<>(newBoard, HttpStatus.CREATED);
    }

    public ResponseEntity remove(KTTask task) {
        try {
            taskRepository.delete(task);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity removeByid(int id) {
        try {
            taskRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity addUser(int id, int userId) {
        Optional<KTUser> possibleUser = userRepository.findById(userId);
        if (possibleUser.isPresent()) {
            Optional<KTTask> possibleTask = taskRepository.findById(id);
            if (!possibleTask.isPresent())
                return ResponseEntity.badRequest().build();
            KTTask task = possibleTask.get();
            if (task.getUsers() == null) {
                List<KTUser> users = new ArrayList<>();
                users.add(possibleUser.get());
                task.setUsers(users);
            } else {
                task.getUsers().add(possibleUser.get());
            }
            KTTask newTask = taskRepository.save(task);
            return new ResponseEntity<>(newTask, HttpStatus.CREATED);

        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity getUsers(int id) {
        Optional<KTTask> task = taskRepository.findById(id);
        return task.<ResponseEntity<?>>map(ktBoard -> ResponseEntity.ok(ktBoard.getUsers())).orElse(ResponseEntity.badRequest().build());
    }
}
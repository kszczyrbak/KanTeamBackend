package szczkrzy.kanteam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entity.KTComment;
import szczkrzy.kanteam.model.entity.KTTask;
import szczkrzy.kanteam.model.entity.KTUser;
import szczkrzy.kanteam.model.request.CommentCreateRequest;
import szczkrzy.kanteam.repositories.ColumnRepository;
import szczkrzy.kanteam.repositories.CommentRepository;
import szczkrzy.kanteam.repositories.TaskRepository;
import szczkrzy.kanteam.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ColumnRepository columnRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, CommentRepository commentRepository, ColumnRepository columnRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.columnRepository = columnRepository;
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
        KTTask existingTask = taskRepository.getOne(task.getId());
        existingTask.setName(task.getName());
        existingTask.setDate(task.getDate());
        existingTask.setDescription(task.getDescription());
        KTTask updatedTask = taskRepository.save(existingTask);
        return new ResponseEntity<>(updatedTask, HttpStatus.CREATED);
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

    public ResponseEntity addComment(int id, CommentCreateRequest commentRequest) {
        Optional<KTTask> possibleTask = taskRepository.findById(id);
        if (!possibleTask.isPresent())
            return ResponseEntity.badRequest().build();
        Optional<KTUser> possibleUser = userRepository.findById(commentRequest.getUserId());
        if (!possibleUser.isPresent())
            return ResponseEntity.badRequest().build();
        KTComment comment = new KTComment(commentRequest);
        comment.setUser(possibleUser.get());
        comment.setTask(possibleTask.get());
        KTComment newComment = commentRepository.save(comment);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    public ResponseEntity removeUser(int id, int userId) {
        Optional<KTUser> possibleUser = userRepository.findById(userId);
        if (possibleUser.isPresent()) {
            Optional<KTTask> possibleTask = taskRepository.findById(id);
            if (!possibleTask.isPresent())
                return ResponseEntity.badRequest().build();
            KTTask task = possibleTask.get();
            KTUser user = possibleUser.get();
            if (!task.getUsers().contains(user))
                return ResponseEntity.badRequest().build();
            task.getUsers().remove(user);
            KTTask newTask = taskRepository.save(task);
            return new ResponseEntity<>(newTask, HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().build();
    }
}
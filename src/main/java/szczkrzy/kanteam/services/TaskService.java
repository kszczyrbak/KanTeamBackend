package szczkrzy.kanteam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entities.*;
import szczkrzy.kanteam.model.enums.NotificationType;
import szczkrzy.kanteam.model.enums.TaskColor;
import szczkrzy.kanteam.model.requests.CommentCreateRequest;
import szczkrzy.kanteam.repositories.*;

import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final SubtaskRepository subtaskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, CommentRepository commentRepository, ColumnRepository columnRepository, NotificationService notificationService, SubtaskRepository subtaskRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.notificationService = notificationService;
        this.subtaskRepository = subtaskRepository;
    }

    public ResponseEntity getById(int id) {
        KTTask task = taskRepository.findById(id).get();
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
        KTTask existingTask = taskRepository.findById(task.getId()).get();
        existingTask.setName(task.getName());
        existingTask.setDate(task.getDate());
        existingTask.setDescription(task.getDescription());
        existingTask.setColor(task.getColorEnum());
        KTTask updatedTask = taskRepository.save(existingTask);

        notificationService.send(NotificationType.TASK_UPDATED, updatedTask.getUsers(), updatedTask);

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
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        KTUser user = userRepository.findByEmail(login);
        KTTask task = taskRepository.findById(id).get();
        KTBoard board = task.getColumn().getBoard();
        if (board.getUsers().contains(user))
            try {
                taskRepository.deleteById(id);
            } catch (EmptyResultDataAccessException e) {
                return ResponseEntity.badRequest().build();
            }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity addUser(int id, int userId) {
        KTUser user = userRepository.findById(userId).get();
        KTTask task = taskRepository.findById(id).get();
        task.addUser(user);
        KTTask newTask = taskRepository.save(task);

        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    public ResponseEntity getUsers(int id) {
        KTTask task = taskRepository.findById(id).get();
        List<KTUser> users = task.getUsers();

        return ResponseEntity.ok(users);
    }

    public ResponseEntity addComment(int id, CommentCreateRequest commentRequest) {
        KTTask task = taskRepository.findById(id).get();
        KTUser user = userRepository.findById(commentRequest.getUserId()).get();
        KTComment comment = new KTComment(commentRequest);
        comment.setUser(user);
        comment.setTask(task);
        comment.setDate(new Date());
        KTComment newComment = commentRepository.save(comment);

        notificationService.send(NotificationType.TASK_COMMENT_ADDED, task.getUsers(), task);

        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    public ResponseEntity removeUser(int id, int userId) {
        KTUser user = userRepository.findById(userId).get();
        KTTask task = taskRepository.findById(id).get();
        task.getUsers().remove(user);
        KTTask newTask = taskRepository.save(task);

        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    public ResponseEntity getColors() {
        return ResponseEntity.ok(TaskColor.values());
    }

    public ResponseEntity updateSubtask(KTSubtask subtask) {
        KTSubtask savedTask = subtaskRepository.findById(subtask.getId()).get();
        subtask.setTask(savedTask.getTask());
        KTSubtask updatedSubtask = subtaskRepository.save(subtask);
        return new ResponseEntity<>(updatedSubtask, HttpStatus.CREATED);
    }

    public ResponseEntity addSubtask(int id, KTSubtask subtask) {
        KTTask task = taskRepository.findById(id).get();
        subtask.setTask(task);
        task.addSubtask(subtask);
        KTTask updatedTask = taskRepository.save(task);
        return new ResponseEntity<>(updatedTask, HttpStatus.CREATED);
    }

    public ResponseEntity removeComment(int id, int commentId) {
        commentRepository.deleteById(commentId);
        KTTask task = taskRepository.findById(id).get();
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    public ResponseEntity removeSubtask(int id, int subtaskId) {
        subtaskRepository.deleteById(subtaskId);
        KTTask task = taskRepository.findById(id).get();
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }
}
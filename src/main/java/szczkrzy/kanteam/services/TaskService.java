package szczkrzy.kanteam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entities.KTComment;
import szczkrzy.kanteam.model.entities.KTNotification;
import szczkrzy.kanteam.model.entities.KTTask;
import szczkrzy.kanteam.model.entities.KTUser;
import szczkrzy.kanteam.model.enums.NotificationType;
import szczkrzy.kanteam.model.enums.TaskPriority;
import szczkrzy.kanteam.model.requests.CommentCreateRequest;
import szczkrzy.kanteam.repositories.ColumnRepository;
import szczkrzy.kanteam.repositories.CommentRepository;
import szczkrzy.kanteam.repositories.TaskRepository;
import szczkrzy.kanteam.repositories.UserRepository;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, CommentRepository commentRepository, ColumnRepository columnRepository, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.notificationService = notificationService;
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
        KTTask updatedTask = taskRepository.save(existingTask);

        KTNotification notification = notificationService.createNotificationObject(NotificationType.TASK_UPDATED, updatedTask);
        notification.setRecipients(updatedTask.getUsers());
        notificationService.send(notification);

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
        KTComment newComment = commentRepository.save(comment);

        KTNotification notification = notificationService.createNotificationObject(NotificationType.TASK_COMMENT_ADDED, task);
        notification.setRecipients(task.getUsers());
        notificationService.send(notification);

        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    public ResponseEntity removeUser(int id, int userId) {
        KTUser user = userRepository.findById(userId).get();
        KTTask task = taskRepository.findById(id).get();
        task.getUsers().remove(user);
        KTTask newTask = taskRepository.save(task);

        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    public ResponseEntity getPriorities() {
        return ResponseEntity.ok(TaskPriority.values());
    }
}
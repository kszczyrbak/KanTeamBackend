package szczkrzy.kanteam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entities.*;
import szczkrzy.kanteam.model.enums.NotificationType;
import szczkrzy.kanteam.model.requests.BoardCreateRequest;
import szczkrzy.kanteam.repositories.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ColumnRepository columnRepository;
    private final TaskRepository taskRepository;
    private final NotificationService notificationService;

    @Autowired
    public BoardService(BoardRepository boardRepository, UserRepository userRepository, TeamRepository teamRepository, ColumnRepository columnRepository, TaskRepository taskRepository, NotificationService notificationService) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.columnRepository = columnRepository;
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
    }

    public ResponseEntity getById(int id) {
        Optional<KTBoard> board = boardRepository.findById(id);
        if (!board.isPresent())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(board);
    }

    public ResponseEntity getAll() {
        return ResponseEntity.ok(boardRepository.findAll());
    }

    public ResponseEntity<?> update(KTBoard board) {
        KTBoard newBoard = boardRepository.save(board);
        return new ResponseEntity<>(newBoard, HttpStatus.CREATED);
    }

    public ResponseEntity remove(KTBoard board) {
        try {
            boardRepository.delete(board);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity removeByid(int id) {
        try {
            boardRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> create(BoardCreateRequest board) {
        KTTeam team = teamRepository.findById(board.getTeam()).get();
        KTUser user = userRepository.findById(board.getUser()).get();
        KTBoard newBoard = new KTBoard();
        newBoard.setName(board.getName());
        newBoard.setTeam(team);
        newBoard.addUser(user);
        boardRepository.save(newBoard);
        return new ResponseEntity<>(newBoard, HttpStatus.CREATED);
    }

    public ResponseEntity addUser(int id, int userId) {
        KTUser user = userRepository.findById(userId).get();
        KTBoard board = boardRepository.findById(id).get();
        board.addUser(user);

        KTBoard newBoard = boardRepository.save(board);
        KTNotification notification1 = notificationService.createNotificationObject(NotificationType.BOARD_USER_INVITED, user, newBoard);
        notification1.setRecipients(board.getUsers());
        notificationService.send(notification1);

        KTNotification notification2 = notificationService.createNotificationObject(NotificationType.USER_BOARD_INVITE, newBoard);
        notification2.setRecipients(Collections.singletonList(user));
        notificationService.send(notification2);

        return new ResponseEntity<>(newBoard, HttpStatus.CREATED);

    }

    public ResponseEntity<?> getTasksById(int id) {
        KTBoard board = boardRepository.findById(id).get();
        List<KTColumn> columns = board.getColumns();
        return ResponseEntity.ok(columns);
    }

    public ResponseEntity<?> getMembersById(int id) {
        KTBoard board = boardRepository.findById(id).get();
        List<KTUser> members = board.getUsers();
        if (board.getTeam() != null)
            members.addAll(board.getTeam().getMembers());
        return ResponseEntity.ok(members);
    }

    public ResponseEntity addColumn(int id, KTColumn column) {
        KTBoard board = boardRepository.findById(id).get();
        column.setBoard(board);
        KTColumn newColumn = columnRepository.save(column);
        return new ResponseEntity<>(newColumn, HttpStatus.CREATED);
    }

    public ResponseEntity updateColumns(int id, List<KTColumn> columns) {
        KTBoard board = boardRepository.findById(id).get();
        for (KTColumn column : columns) {
            column.setBoard(board);
            columnRepository.save(column);
        }
        return ResponseEntity.ok(200);
    }

    public ResponseEntity updateColumnTasks(int id, List<KTTask> tasks) {
        KTColumn column = columnRepository.findById(id).get();
        for (KTTask task : tasks) {
            task.setColumn(column);
            taskRepository.save(task);
        }
        return ResponseEntity.ok(200);
    }

    public ResponseEntity addTask(int boardId, int colId, KTTask task) {
        KTBoard board = boardRepository.findById(boardId).get();
        KTColumn column = columnRepository.findById(colId).get();
        task.setColumn(column);
        KTTask newTask = taskRepository.save(task);

        KTNotification notification = notificationService.createNotificationObject(NotificationType.BOARD_TASK_CREATED, board);
        notification.setRecipients(board.getUsers());
        notificationService.send(notification);

        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }
}

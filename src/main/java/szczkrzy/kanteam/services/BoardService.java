package szczkrzy.kanteam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entities.*;
import szczkrzy.kanteam.model.enums.NotificationType;
import szczkrzy.kanteam.model.requests.BoardCreateRequest;
import szczkrzy.kanteam.repositories.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ColumnRepository columnRepository;
    private final TaskRepository taskRepository;
    private final NotificationService notificationService;
    private final ColorMappingRepository colorMappingRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, UserRepository userRepository, TeamRepository teamRepository, ColumnRepository columnRepository, TaskRepository taskRepository, NotificationService notificationService, ColorMappingRepository colorMappingRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.columnRepository = columnRepository;
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
        this.colorMappingRepository = colorMappingRepository;
    }

    public ResponseEntity getById(int id) {
        KTBoard board = boardRepository.findById(id).get();
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
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        KTUser user = userRepository.findByEmail(login);
        KTBoard board = boardRepository.findById(id).get();
        if (board.getUsers().contains(user))
            try {
                boardRepository.deleteById(id);
            } catch (EmptyResultDataAccessException e) {
                return ResponseEntity.badRequest().build();
            }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> create(BoardCreateRequest board) {
        KTTeam team = tryGetTeam(board.getTeam());
        KTUser user = userRepository.findById(board.getUser()).get();
        KTBoard newBoard = new KTBoard();
        newBoard.setName(board.getName());
        if (team != null) {
            newBoard.setTeam(team);
            List<KTUser> teamMembers = new ArrayList<>(team.getMembers());
            newBoard.setUsers(teamMembers);
        } else {
            newBoard.addUser(user);
        }
        boardRepository.save(newBoard);
        return new ResponseEntity<>(newBoard, HttpStatus.CREATED);
    }

    public ResponseEntity addUser(int id, int userId) {
        KTUser user = userRepository.findById(userId).get();
        KTBoard board = boardRepository.findById(id).get();
        board.addUser(user);

        KTBoard newBoard = boardRepository.save(board);
        sendUserAddedNotifications(user, newBoard);

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
        return ResponseEntity.ok(board.getColumns());
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

        notificationService.send(NotificationType.BOARD_TASK_CREATED, board.getUsers(), board);

        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    public ResponseEntity<?> changeColorMappings(int id, List<KTColorMapping> mappings) {
        KTBoard board = boardRepository.findById(id).get();
        board.getColorMappings().clear();
        mappings.forEach(mapping -> mapping.setBoard(board));
        board.getColorMappings().addAll(mappings);
        KTBoard updatedBoard = boardRepository.save(board);
        return new ResponseEntity<>(updatedBoard, HttpStatus.CREATED);
    }


    private KTTeam tryGetTeam(int teamId) {
        try {
            return teamRepository.findById(teamId).get();
        } catch (Exception e) {
            return null;
        }
    }

    public ResponseEntity removeColumnById(int id, int colId) {
        KTBoard board = boardRepository.findById(id).get();
        KTColumn column = columnRepository.findById(colId).get();
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        KTUser user = userRepository.findByEmail(login);
        if (board.getUsers().stream().map(KTUser::getId).anyMatch(uid -> uid == user.getId())) {
            columnRepository.delete(column);
        }

        return ResponseEntity.accepted().build();
    }

    public ResponseEntity updateUsers(int id, List<KTUser> users) {

        KTBoard board = boardRepository.findById(id).get();
        List<KTUser> oldUsers = new ArrayList<>(board.getUsers());
        board.setUsers(users);
        KTBoard updatedBoard = boardRepository.save(board);
        List<KTUser> newUsers = new ArrayList<>(updatedBoard.getUsers());
        newUsers.removeAll(oldUsers);

        newUsers.forEach(user -> sendUserAddedNotifications(user, updatedBoard));

        return ResponseEntity.ok(updatedBoard.getUsers());
    }

    private void sendUserAddedNotifications(KTUser user, KTBoard board) {
        List<KTUser> otherUsers = board.getUsers();
        otherUsers.remove(user);
        notificationService.send(NotificationType.BOARD_USER_INVITED, otherUsers, user, board);
        notificationService.send(NotificationType.USER_BOARD_INVITE, Collections.singletonList(user), board);
    }
}

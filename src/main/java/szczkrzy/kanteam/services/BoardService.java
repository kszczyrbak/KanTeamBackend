package szczkrzy.kanteam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entity.*;
import szczkrzy.kanteam.model.request.BoardCreateRequest;
import szczkrzy.kanteam.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ColumnRepository columnRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, UserRepository userRepository, TeamRepository teamRepository, ColumnRepository columnRepository, TaskRepository taskRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.columnRepository = columnRepository;
        this.taskRepository = taskRepository;
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
        Optional<KTTeam> team = teamRepository.findById(board.getTeam());
        Optional<KTUser> user = userRepository.findById(board.getUser());
        KTBoard newBoard = new KTBoard();
        newBoard.setName(board.getName());
        team.ifPresent(newBoard::setTeam);

        if (user.isPresent()) {
            List<KTUser> users = new ArrayList<>();
            users.add(user.get());
            newBoard.setUsers(users);
        }

        boardRepository.save(newBoard);
        return new ResponseEntity<>(newBoard, HttpStatus.CREATED);
    }

    //todo: fix duplicate

    public ResponseEntity addUser(int id, int userId) {
        Optional<KTUser> possibleUser = userRepository.findById(userId);
        if (possibleUser.isPresent()) {
            Optional<KTBoard> possibleBoard = boardRepository.findById(id);
            if (!possibleBoard.isPresent())
                return ResponseEntity.badRequest().build();
            KTBoard board = possibleBoard.get();
            if (board.getUsers() == null) {
                List<KTUser> users = new ArrayList<>();
                users.add(possibleUser.get());
                board.setUsers(users);
            } else {
                board.getUsers().add(possibleUser.get());
            }
            KTBoard newBoard = boardRepository.save(board);
            return new ResponseEntity<>(newBoard, HttpStatus.CREATED);

        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<?> getTasksById(int id) {
        Optional<KTBoard> board = boardRepository.findById(id);
        return board.<ResponseEntity<?>>map(ktBoard -> ResponseEntity.ok(ktBoard.getColumns())).orElse(ResponseEntity.badRequest().build());
    }

    public ResponseEntity<?> getMembersById(int id) {
        Optional<KTBoard> possibleBoard = boardRepository.findById(id);
        if (!possibleBoard.isPresent())
            return ResponseEntity.badRequest().build();
        KTBoard board = possibleBoard.get();
        List<KTUser> members = board.getUsers();
        if (board.getTeam() != null)
            members.addAll(board.getTeam().getMembers());
        return ResponseEntity.ok(members);
    }

    public ResponseEntity addColumn(int id, KTColumn column) {
        Optional<KTBoard> possibleBoard = boardRepository.findById(id);
        if (!possibleBoard.isPresent())
            return ResponseEntity.badRequest().build();
        KTBoard board = possibleBoard.get();
        column.setBoard(board);

        KTColumn newColumn = columnRepository.save(column);
        return new ResponseEntity<>(newColumn, HttpStatus.CREATED);
    }

    public ResponseEntity updateColumns(int id, List<KTColumn> columns) {
        Optional<KTBoard> possibleBoard = boardRepository.findById(id);
        if (!possibleBoard.isPresent())
            return ResponseEntity.badRequest().build();
        KTBoard board = possibleBoard.get();
        for (KTColumn column : columns) {
            column.setBoard(board);
            columnRepository.save(column);
        }
        return ResponseEntity.ok(200);
    }

    public ResponseEntity updateColumnTasks(int id, List<KTTask> tasks) {
        Optional<KTColumn> possibleColumn = columnRepository.findById(id);
        if (!possibleColumn.isPresent())
            return ResponseEntity.badRequest().build();
        KTColumn column = possibleColumn.get();
        for (KTTask task : tasks) {
            task.setColumn(column);
            taskRepository.save(task);
        }
        return ResponseEntity.ok(200);
    }

    public ResponseEntity addTask(int boardId, int colId, KTTask task) {
        Optional<KTBoard> possibleBoard = boardRepository.findById(boardId);
        if (!possibleBoard.isPresent())
            return ResponseEntity.badRequest().build();
        Optional<KTColumn> possibleColumn = columnRepository.findById(colId);
        if (!possibleColumn.isPresent())
            return ResponseEntity.badRequest().build();
        KTColumn column = possibleColumn.get();
        task.setColumn(column);

        KTTask newTask = taskRepository.save(task);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }
}

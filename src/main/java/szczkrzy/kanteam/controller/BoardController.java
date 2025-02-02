package szczkrzy.kanteam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import szczkrzy.kanteam.model.entities.*;
import szczkrzy.kanteam.model.requests.BoardCreateRequest;
import szczkrzy.kanteam.services.BoardService;

import java.util.List;

@RestController

@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        return boardService.getById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BoardCreateRequest board) {
        return boardService.create(board);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> addUser(@RequestParam int userId, @PathVariable int id) {
        return boardService.addUser(id, userId);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAll() {
        return boardService.getAll();
    }

    @PutMapping
    public ResponseEntity update(@RequestBody KTBoard board) {
        return boardService.update(board);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody KTBoard board) {
        return boardService.remove(board);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable int id) {
        return boardService.removeByid(id);
    }

    @DeleteMapping("/{id}/columns/{colId}")
    public ResponseEntity deleteColumnById(@PathVariable int id, @PathVariable int colId) {
        return boardService.removeColumnById(id, colId);
    }

    @GetMapping("/{id}/columns")
    public ResponseEntity<?> getTaskColumns(@PathVariable int id) {
        return boardService.getTasksById(id);
    }

    @PostMapping("/{id}/columns")
    public ResponseEntity addColumn(@PathVariable int id, @RequestBody KTColumn column) {
        return boardService.addColumn(id, column);
    }

    @PutMapping("/{id}/columns")
    public ResponseEntity updateColumns(@PathVariable int id, @RequestBody List<KTColumn> columns) {
        return boardService.updateColumns(id, columns);
    }

    @PutMapping("/{id}/users")
    public ResponseEntity updateUsers(@PathVariable int id, @RequestBody List<KTUser> users) {
        return boardService.updateUsers(id, users);
    }

    @PutMapping("/{id}/columns/{colId}/tasks")
    public ResponseEntity updateColumnTasks(@PathVariable int colId, @RequestBody List<KTTask> tasks) {
        return boardService.updateColumnTasks(colId, tasks);
    }

    @PostMapping("/{id}/columns/{colId}/tasks")
    public ResponseEntity addTask(@PathVariable int id, @PathVariable int colId, @RequestBody KTTask task) {
        return boardService.addTask(id, colId, task);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<?> getUsers(@PathVariable int id) {
        return boardService.getMembersById(id);
    }

    @PutMapping("/{id}/colors")
    public ResponseEntity<?> changeColorMappings(@PathVariable int id, @RequestBody List<KTColorMapping> mappings) {
        return boardService.changeColorMappings(id, mappings);
    }

}

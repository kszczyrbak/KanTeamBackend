package szczkrzy.kanteam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szczkrzy.kanteam.model.entity.KTBoard;
import szczkrzy.kanteam.model.entity.KTColumn;
import szczkrzy.kanteam.model.entity.KTTask;
import szczkrzy.kanteam.model.request.BoardCreateRequest;
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

    @GetMapping("/{id}/columns")
    public ResponseEntity<?> getTaskColumns(@PathVariable int id) {
        return boardService.getTasksById(id);
    }

    @CrossOrigin("http://localhost:4200")
    @PutMapping("/{id}/columns/add")
    public ResponseEntity addColumn(@PathVariable int id, @RequestBody KTColumn column) {
        return boardService.addColumn(id, column);
    }

    @CrossOrigin("http://localhost:4200")
    @PutMapping("/{id}/columns")
    public ResponseEntity updateColumns(@PathVariable int id, @RequestBody List<KTColumn> columns) {
        return boardService.updateColumns(id, columns);
    }

    @CrossOrigin("http://localhost:4200")
    @PutMapping("/{id}/columns/{colId}/tasks/add")
    public ResponseEntity addTask(@PathVariable int id, @PathVariable int colId, @RequestBody KTTask task) {
        return boardService.addTask(id, colId, task);
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<?> getMembers(@PathVariable int id) {
        return boardService.getMembersById(id);
    }


}

package szczkrzy.kanteam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szczkrzy.kanteam.model.entities.KTTeam;
import szczkrzy.kanteam.model.entities.KTUser;
import szczkrzy.kanteam.model.requests.TeamCreateRequest;
import szczkrzy.kanteam.services.TeamService;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        return teamService.getById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TeamCreateRequest request) {
        return teamService.create(request);
    }

    @PutMapping("/{id}/members/add/{userId}")
    public ResponseEntity<?> addUser(@PathVariable int id, @PathVariable int userId) {
        return teamService.addUser(id, userId);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return teamService.getAll();
    }

    @PutMapping
    public ResponseEntity update(@RequestBody KTTeam team) {
        return teamService.update(team);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable int id) {
        return teamService.removeByid(id);
    }

    @GetMapping("/{id}/boards")
    public ResponseEntity<?> getBoards(@PathVariable int id) {
        return teamService.getBoardsById(id);
    }

    @PutMapping("/{id}/members")
    public ResponseEntity updateUsers(@PathVariable int id, @RequestBody List<KTUser> users) {
        return teamService.updateUsers(id, users);
    }
}

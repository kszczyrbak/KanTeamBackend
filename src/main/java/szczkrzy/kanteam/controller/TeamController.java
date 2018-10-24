package szczkrzy.kanteam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szczkrzy.kanteam.model.entity.KTTeam;
import szczkrzy.kanteam.model.request.TeamCreateRequest;
import szczkrzy.kanteam.services.TeamService;

@RestController
@CrossOrigin("http://localhost:4200")
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

    @PutMapping("/{id}/members/add")
    public ResponseEntity<?> addUser(@RequestBody int user, @PathVariable int id) {
        return teamService.addUser(id, user);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return teamService.getAll();
    }

    @PutMapping
    public ResponseEntity update(@RequestBody KTTeam team) {
        return teamService.update(team);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody KTTeam team) {
        return teamService.remove(team);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable int id) {
        return teamService.removeByid(id);
    }

    @GetMapping("/{id}/boards")
    public ResponseEntity<?> getBoards(@PathVariable int id) {
        return teamService.getBoardsById(id);
    }
}

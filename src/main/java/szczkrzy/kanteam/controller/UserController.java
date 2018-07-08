package szczkrzy.kanteam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szczkrzy.kanteam.model.entity.KanTeamUser;
import szczkrzy.kanteam.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {

        return userService.getById(id);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {

        return userService.getAll();
    }

    @PutMapping
    public ResponseEntity update(@RequestBody KanTeamUser user) {
        return userService.update(user);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody KanTeamUser user) {
        return userService.remove(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable int id) {
        return userService.removeByid(id);
    }
}

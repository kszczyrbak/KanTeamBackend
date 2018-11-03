package szczkrzy.kanteam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import szczkrzy.kanteam.model.entity.KTUser;
import szczkrzy.kanteam.services.UserService;

@RestController
@RequestMapping("/api/users")
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

    @GetMapping("{id}/boards")
    public ResponseEntity<?> getUserBoards(@PathVariable int id) {
        return userService.getUserBoards(id);
    }

    @GetMapping("{id}/teams")
    public ResponseEntity<?> getUserTeams(@PathVariable int id) {
        return userService.getUserTeams(id);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody KTUser user) {
        return userService.update(user);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody KTUser user) {
        return userService.remove(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable int id) {
        return userService.removeByid(id);
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity uploadPhoto(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        return userService.saveUserPhoto(id, file);
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity getPhoto(@PathVariable int id) {
        return userService.getPhoto(id);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getUsersByName(@RequestParam("name") String name) {
        return userService.getUsersByName(name);
    }

}

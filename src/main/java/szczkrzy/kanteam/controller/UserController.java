package szczkrzy.kanteam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import szczkrzy.kanteam.model.request.SignupRequest;
import szczkrzy.kanteam.service.UserService;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignupRequest signupRequest) {
        //TODO: logger

        return userService.register(signupRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {

        return userService.getById(id);
    }
}

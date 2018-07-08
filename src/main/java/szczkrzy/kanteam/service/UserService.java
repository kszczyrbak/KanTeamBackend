package szczkrzy.kanteam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entity.KanTeamUser;
import szczkrzy.kanteam.model.request.LoginRequest;
import szczkrzy.kanteam.model.request.SignupRequest;
import szczkrzy.kanteam.model.response.AuthResponse;
import szczkrzy.kanteam.model.security.SecurityUserModel;
import szczkrzy.kanteam.repository.UserRepository;
import szczkrzy.kanteam.security.JwtTokenService;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService tokenService;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public ResponseEntity register(SignupRequest signupRequest) {
        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        KanTeamUser kanTeamUser = new KanTeamUser(signupRequest);

        userRepository.save(kanTeamUser);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    public ResponseEntity<?> getById(int id) {

        return new ResponseEntity<>(userRepository.findById(id), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final KanTeamUser user = userRepository.findByEmail(loginRequest.getLogin());
        final String token = tokenService.generateToken(new SecurityUserModel(user));
        return ResponseEntity.ok(new AuthResponse(user.getId(), token));
    }

    public ResponseEntity getAll() {

        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity update(KanTeamUser user) {
        KanTeamUser existingUser = userRepository.findByEmail(user.getEmail());
        user.setPassword(existingUser.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity remove(KanTeamUser user) {
        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity removeByid(int id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

package szczkrzy.kanteam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import szczkrzy.kanteam.model.entity.KTBoard;
import szczkrzy.kanteam.model.entity.KTTeam;
import szczkrzy.kanteam.model.entity.KTUser;
import szczkrzy.kanteam.model.request.LoginRequest;
import szczkrzy.kanteam.model.request.SignupRequest;
import szczkrzy.kanteam.model.response.AuthResponse;
import szczkrzy.kanteam.model.security.SecurityUserModel;
import szczkrzy.kanteam.repositories.UserRepository;
import szczkrzy.kanteam.security.JwtTokenService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    public ResponseEntity<?> register(SignupRequest signupRequest) {
        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        KTUser KTUser = new KTUser(signupRequest);

        KTUser = userRepository.save(KTUser);
        return new ResponseEntity<>(KTUser, HttpStatus.CREATED);
    }

    public ResponseEntity getById(int id) {
        Optional<KTUser> user = userRepository.findById(id);
        if (!user.isPresent())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final KTUser user = userRepository.findByEmail(loginRequest.getLogin());
        final String token = tokenService.generateToken(new SecurityUserModel(user));
        return ResponseEntity.ok(new AuthResponse(user.getId(), token));
    }

    public ResponseEntity getAll() {

        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<?> update(KTUser user) {
        KTUser existingUser = userRepository.findByEmail(user.getEmail());
        user.setPassword(existingUser.getPassword());
        KTUser newUser = userRepository.save(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    public ResponseEntity remove(KTUser user) {
        try {
            userRepository.delete(user);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity removeByid(int id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getUserBoards(int id) {
        Optional<KTUser> possibleUser = userRepository.findById(id);
        if (!possibleUser.isPresent())
            return ResponseEntity.badRequest().build();
        else {
            KTUser user = possibleUser.get();
            List<KTBoard> boards = user.getBoards();
            user.getTeams().forEach(team -> boards.addAll(team.getBoards()));
            return ResponseEntity.ok(boards);
        }
    }

    public ResponseEntity<?> getUserTeams(int id) {
        Optional<KTUser> possibleUser = userRepository.findById(id);
        if (!possibleUser.isPresent())
            return ResponseEntity.badRequest().build();
        else {
            KTUser user = possibleUser.get();
            List<KTTeam> teams = user.getTeams();
            return ResponseEntity.ok(teams);
        }
    }

    public ResponseEntity saveUserPhoto(int id, MultipartFile file) {
        System.out.println(file);
        Optional<KTUser> possibleUser = userRepository.findById(id);
        if (!possibleUser.isPresent())
            return ResponseEntity.badRequest().build();
        KTUser user = possibleUser.get();
        try {
            user.setPhoto(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        KTUser newUser = userRepository.save(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getUsersByName(String name) {

        return ResponseEntity.ok(userRepository.findByFullName(name));
    }

    public ResponseEntity<byte[]> getPhoto(int id) {
        Optional<KTUser> possibleUser = userRepository.findById(id);
        if (!possibleUser.isPresent())
            return ResponseEntity.badRequest().build();
        else {
            KTUser user = possibleUser.get();
            if (user.getPhoto() == null)
                return ResponseEntity.badRequest().build();
            byte[] photo = user.getPhoto();
            return ResponseEntity.ok(photo);
        }
    }
}
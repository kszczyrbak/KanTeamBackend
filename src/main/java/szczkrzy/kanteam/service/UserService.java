package szczkrzy.kanteam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entity.KanTeamUser;
import szczkrzy.kanteam.model.request.SignupRequest;
import szczkrzy.kanteam.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity register(SignupRequest signupRequest) {
        KanTeamUser kanTeamUser = new KanTeamUser(signupRequest);

        userRepository.save(kanTeamUser);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    public ResponseEntity<?> getById(int id) {

        return new ResponseEntity<>(userRepository.findById(id), HttpStatus.ACCEPTED);
    }
}

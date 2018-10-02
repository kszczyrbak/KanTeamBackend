package szczkrzy.kanteam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entity.KTTeam;
import szczkrzy.kanteam.model.entity.KTUser;
import szczkrzy.kanteam.repository.TeamRepository;
import szczkrzy.kanteam.repository.UserRepository;

import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity getById(int id) {
        Optional<KTTeam> board = teamRepository.findById(id);
        if (!board.isPresent())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(board);
    }

    public ResponseEntity getAll() {
        return ResponseEntity.ok(teamRepository.findAll());
    }

    public ResponseEntity<?> update(KTTeam team) {
        KTTeam newBoard = teamRepository.save(team);
        return new ResponseEntity<>(newBoard, HttpStatus.CREATED);
    }

    public ResponseEntity remove(KTTeam team) {
        try {
            teamRepository.delete(team);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity removeByid(int id) {
        try {
            teamRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> create(KTTeam team) {
        KTTeam newTeam = teamRepository.save(team);
        return new ResponseEntity<>(newTeam, HttpStatus.CREATED);
    }

    public ResponseEntity<?> addUser(int id, int userId) {
        Optional<KTTeam> possibleTeam = teamRepository.findById(id);
        if (!possibleTeam.isPresent())
            return ResponseEntity.badRequest().build();
        else {
            KTTeam team = possibleTeam.get();
            Optional<KTUser> possibleUser = userRepository.findById(userId);
            if (!possibleUser.isPresent())
                return ResponseEntity.badRequest().build();
            else {
                team.getMembers().add(possibleUser.get());
                teamRepository.save(team);
                return ResponseEntity.ok().build();
            }

        }
    }
}

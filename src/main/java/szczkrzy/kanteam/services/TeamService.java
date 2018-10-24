package szczkrzy.kanteam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entity.KTBoard;
import szczkrzy.kanteam.model.entity.KTTeam;
import szczkrzy.kanteam.model.entity.KTUser;
import szczkrzy.kanteam.model.request.TeamCreateRequest;
import szczkrzy.kanteam.repositories.TeamRepository;
import szczkrzy.kanteam.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
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
        Optional<KTTeam> team = teamRepository.findById(id);
        if (!team.isPresent())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(team);
    }

    public ResponseEntity getAll() {
        return ResponseEntity.ok(teamRepository.findAll());
    }

    public ResponseEntity<?> update(KTTeam team) {
        KTTeam newTeam = teamRepository.save(team);
        return new ResponseEntity<>(newTeam, HttpStatus.CREATED);
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

    public ResponseEntity<?> create(TeamCreateRequest requestBody) {
        Optional<KTUser> possibleUser = userRepository.findById(requestBody.getOwnerId());
        if (!possibleUser.isPresent())
            return ResponseEntity.badRequest().build();
        KTTeam newTeam = new KTTeam();
        newTeam.setName(requestBody.getName());
        List<KTUser> members = new ArrayList<>();
        members.add(possibleUser.get());
        newTeam.setMembers(members);
        KTTeam createdTeam = teamRepository.save(newTeam);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
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
                KTTeam updatedTeam = teamRepository.save(team);
                return new ResponseEntity<>(updatedTeam, HttpStatus.CREATED);
            }

        }
    }

    public ResponseEntity<?> getBoardsById(int id) {
        Optional<KTTeam> team = teamRepository.findById(id);
        if (!team.isPresent())
            return ResponseEntity.badRequest().build();
        List<KTBoard> boards = team.get().getBoards();
        return ResponseEntity.ok(boards);
    }
}

package szczkrzy.kanteam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entities.KTBoard;
import szczkrzy.kanteam.model.entities.KTNotification;
import szczkrzy.kanteam.model.entities.KTTeam;
import szczkrzy.kanteam.model.entities.KTUser;
import szczkrzy.kanteam.model.enums.NotificationType;
import szczkrzy.kanteam.model.requests.TeamCreateRequest;
import szczkrzy.kanteam.repositories.TeamRepository;
import szczkrzy.kanteam.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository, NotificationService notificationService) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public ResponseEntity getById(int id) {
        KTTeam team = teamRepository.findById(id).get();
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
        KTUser user = userRepository.findById(requestBody.getOwnerId()).get();
        KTTeam newTeam = new KTTeam();
        newTeam.setName(requestBody.getName());
        List<KTUser> members = new ArrayList<>();
        members.add(user);
        newTeam.setMembers(members);
        KTTeam createdTeam = teamRepository.save(newTeam);

        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }

    public ResponseEntity<?> addUser(int id, int userId) {
        KTTeam team = teamRepository.findById(id).get();
        KTUser user = userRepository.findById(userId).get();
        team.getMembers().add(user);
        KTTeam updatedTeam = teamRepository.save(team);

        KTNotification notification1 = notificationService.createNotificationObject(NotificationType.TEAM_USER_INVITED, user, team);
        notification1.setRecipients(team.getMembers());
        notificationService.send(notification1);

        KTNotification notification2 = notificationService.createNotificationObject(NotificationType.USER_TEAM_INVITE, team);
        notification2.setRecipients(Collections.singletonList(user));
        notificationService.send(notification2);

        return new ResponseEntity<>(updatedTeam, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getBoardsById(int id) {
        KTTeam team = teamRepository.findById(id).get();
        List<KTBoard> boards = team.getBoards();

        return ResponseEntity.ok(boards);
    }
}

package szczkrzy.kanteam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entities.KTBoard;
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
    private final BoardService boardService;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository, NotificationService notificationService, BoardService boardService) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.boardService = boardService;
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
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        KTUser user = userRepository.findByEmail(login);
        KTTeam team = teamRepository.findById(id).get();
        if (team.getMembers().contains(user))
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
        updatedTeam.getBoards().forEach(board -> boardService.addUser(board.getId(), userId));

        sendUserInvitedNotifications(user, team);

        return new ResponseEntity<>(updatedTeam, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getBoardsById(int id) {
        KTTeam team = teamRepository.findById(id).get();
        List<KTBoard> boards = team.getBoards();

        return ResponseEntity.ok(boards);
    }

    public ResponseEntity updateUsers(int id, List<KTUser> users) {
        KTTeam team = teamRepository.findById(id).get();
        List<KTUser> oldUsers = new ArrayList<>(team.getMembers());
        team.setMembers(users);
        KTTeam updatedTeam = teamRepository.save(team);
        List<KTUser> newUsers = new ArrayList<>(updatedTeam.getMembers());
        newUsers.removeAll(oldUsers);

        newUsers.forEach(user -> sendUserInvitedNotifications(user, updatedTeam));

        return ResponseEntity.ok(updatedTeam.getMembers());
    }


    private void sendUserInvitedNotifications(KTUser user, KTTeam team) {
        List<KTUser> otherUsers = team.getMembers();
        otherUsers.remove(user);
        notificationService.send(NotificationType.TEAM_USER_INVITED, otherUsers, user, team);
        notificationService.send(NotificationType.USER_TEAM_INVITE, Collections.singletonList(user), team);
    }
}

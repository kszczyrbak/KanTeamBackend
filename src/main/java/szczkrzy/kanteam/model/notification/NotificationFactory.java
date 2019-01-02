package szczkrzy.kanteam.model.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entities.KTNotification;
import szczkrzy.kanteam.model.entities.KTUser;
import szczkrzy.kanteam.model.enums.NotificationType;
import szczkrzy.kanteam.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static szczkrzy.kanteam.model.enums.NotificationType.*;

@Service
public class NotificationFactory {

    @Autowired
    private UserRepository userRepository;

    private static Map<NotificationType, String> notificationsTextMap = Map.of(
            TASK_COMMENT_ADDED, "%s has commented on task %s",
            BOARD_USER_INVITED, "%s has added %s to board %s",
            BOARD_TASK_CREATED, "%s has created a task in board %s",
            TEAM_USER_INVITED, "%s added %s to team %s",
            TEAM_BOARD_CREATED, "%s created a board %s in team %s",
            USER_TEAM_INVITE, "%s added you to team %s",
            USER_BOARD_INVITE, "%s added you to board %s",
            TASK_UPDATED, "%s has modified the task %s"
    );

    public KTNotification getNotification(NotificationType notificationType, List<KTUser> recipients, NotificationSubject... subjects) {
        String format = notificationsTextMap.get(notificationType);

        List<NotificationSubject> subjectList = new ArrayList<>();
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        KTUser user = userRepository.findByEmail(login);
        subjectList.add(user);
        subjectList.addAll(Arrays.asList(subjects));
        String text = String.format(format, subjectList.stream().map(NotificationSubject::getNotificationTextRepresentation).toArray());
        KTNotification notification = new KTNotification();
        recipients.remove(user);
        notification.setRecipients(recipients);
        notification.setText(text);
        return notification;
    }
}

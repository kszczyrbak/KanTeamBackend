package szczkrzy.kanteam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import szczkrzy.kanteam.model.entities.KTNotification;
import szczkrzy.kanteam.model.entities.KTUser;
import szczkrzy.kanteam.model.enums.NotificationType;
import szczkrzy.kanteam.model.notification.NotificationFactory;
import szczkrzy.kanteam.model.notification.NotificationSubject;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationFactory notificationFactory;

    @Value("${notification.url}")
    private String notificationMicroserviceURL;


    @Autowired
    public NotificationService(NotificationFactory notificationFactory) {
        this.notificationFactory = notificationFactory;
    }

    private KTNotification createNotificationObject(NotificationType notificationType, List<KTUser> recipients, NotificationSubject... subjects) {
        return notificationFactory.getNotification(notificationType, recipients, subjects);
    }

    public void send(NotificationType notificationType, List<KTUser> recipients, NotificationSubject... subjects) {
        KTNotification notification = notificationFactory.getNotification(notificationType, recipients, subjects);
        RestTemplate restTemplate = new RestTemplate();
        String notificationEndpoint = notificationMicroserviceURL + "api/notifications";
        try {
            restTemplate.postForObject(notificationEndpoint, notification, NotificationType.class);
        } catch (RestClientException ignored) {
            //probably could not extract json response; we ignore it, because we dont need the response
        }
    }
}

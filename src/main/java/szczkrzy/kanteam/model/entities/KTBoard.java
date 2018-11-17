package szczkrzy.kanteam.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import szczkrzy.kanteam.model.notification.NotificationSubject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KTBoard implements NotificationSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private int id;

    @NotNull
    @Column(unique = true)
    private String name;

    @JoinColumn(name = "team_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    private KTTeam team;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "board_users",
            joinColumns = {@JoinColumn(name = "board_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    @JsonIgnore
    private List<KTUser> users;

    @JsonIgnore
    @OneToMany(mappedBy = "board")
    private List<KTColumn> columns;

    @Override
    @JsonIgnore
    public String getNotificationTextRepresentation() {
        return name;
    }

    public void addUser(KTUser user) {
        if (users == null) {
            users = new ArrayList<>();
            users.add(user);
        } else {
            users.add(user);
        }
    }
}

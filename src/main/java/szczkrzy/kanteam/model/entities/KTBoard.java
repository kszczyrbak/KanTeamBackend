package szczkrzy.kanteam.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @ManyToOne
    private KTTeam team;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "board_users",
            joinColumns = {@JoinColumn(name = "board_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<KTUser> users;

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KTColumn> columns;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KTColorMapping> colorMappings;

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

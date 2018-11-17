package szczkrzy.kanteam.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import szczkrzy.kanteam.model.notification.NotificationSubject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KTTeam implements NotificationSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "team_id")
    private int id;

    @NotNull
    @Column(unique = true)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "team_users",
            joinColumns = {@JoinColumn(name = "team_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<KTUser> members;

    @JsonIgnore
    @OneToMany(mappedBy = "team")
    private List<KTBoard> boards;

    @Override
    @JsonIgnore
    public String getNotificationTextRepresentation() {
        return name;
    }
}

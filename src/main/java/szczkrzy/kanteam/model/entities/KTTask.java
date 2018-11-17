package szczkrzy.kanteam.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import szczkrzy.kanteam.model.enums.TaskPriority;
import szczkrzy.kanteam.model.notification.NotificationSubject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KTTask implements NotificationSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id")
    private int id;

    @NotNull
    @Column
    private String name;

    @Column
    private String description;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "task_users",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<KTUser> users;

    @JoinColumn(name = "column_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JsonIgnore
    private KTColumn column;

    @OneToMany(mappedBy = "task")
    private List<KTComment> comments;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date date;

    @Column(name = "task_order")
    @Basic
    private int order;

    @Enumerated(EnumType.STRING)
    @Column
    private TaskPriority priority;

    @JsonGetter
    private int getPriority() {
        return priority.getValue();
    }

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

package szczkrzy.kanteam.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import szczkrzy.kanteam.model.enums.TaskColor;
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

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "task_users",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<KTUser> users;

    @JoinColumn(name = "column_id")
    @ManyToOne
    @JsonIgnore
    private KTColumn column;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KTComment> comments;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KTSubtask> subtasks;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date date;

    @Column(name = "task_order")
    @Basic
    private int order;

    @Enumerated(EnumType.STRING)
    @Column
    private TaskColor color;

    @JsonIgnore
    public TaskColor getColorEnum() {
        return color;
    }

//    public String getColor(){
//        return color.toString().toLowerCase();
//    }

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

    public void addSubtask(KTSubtask subtask) {
        if (subtasks == null) {
            subtasks = new ArrayList<>();
            subtasks.add(subtask);
        } else {
            subtasks.add(subtask);
        }
    }
}

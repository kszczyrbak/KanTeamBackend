package szczkrzy.kanteam.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import szczkrzy.kanteam.model.notification.NotificationSubject;
import szczkrzy.kanteam.model.requests.SignupRequest;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KTUser implements NotificationSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int id;


    @ManyToMany(cascade = CascadeType.REFRESH,
            mappedBy = "members")
    @JsonIgnore
    private List<KTTeam> teams;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column
    private String password;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @NotNull
    @Column
    private String fullName;

    @ManyToMany(cascade = CascadeType.REFRESH,
            mappedBy = "users")
    @JsonIgnore
    private List<KTBoard> boards;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<KTComment> comments;

    @Column
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private byte[] photo;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<KTRole> roles;

    public KTUser(SignupRequest signupRequest) {
        email = signupRequest.getEmail();
        password = signupRequest.getPassword();
        fullName = signupRequest.getFullName();
    }

    public void addRole(KTRole role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
            this.roles.add(role);
        } else
            this.roles.add(role);
    }

    @Override
    @JsonIgnore
    public String getNotificationTextRepresentation() {
        return fullName;
    }
}

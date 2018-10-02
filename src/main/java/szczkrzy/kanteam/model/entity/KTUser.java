package szczkrzy.kanteam.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import szczkrzy.kanteam.model.request.SignupRequest;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KTUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int id;


    @ManyToMany(cascade = CascadeType.ALL,
            mappedBy = "members")
    private List<KTTeam> teams;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column
    private String password;

    @NotNull
    @Column
    private String fullName;

    @ManyToMany(cascade = CascadeType.ALL,
            mappedBy = "users")
    @JsonIgnore
    private List<KTBoard> boards;

    public KTUser(SignupRequest signupRequest) {
        email = signupRequest.getEmail();
        password = signupRequest.getPassword();
        fullName = signupRequest.getFullName();
    }
}

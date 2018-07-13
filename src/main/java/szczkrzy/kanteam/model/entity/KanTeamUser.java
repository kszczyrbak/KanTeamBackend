package szczkrzy.kanteam.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import szczkrzy.kanteam.model.request.SignupRequest;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KanTeamUser {

    public KanTeamUser(SignupRequest signupRequest) {
        email = signupRequest.getEmail();
        password = signupRequest.getPassword();
        fullName = signupRequest.getFullName();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column
    private String password;

    @NotNull
    @Column
    private String fullName;
}

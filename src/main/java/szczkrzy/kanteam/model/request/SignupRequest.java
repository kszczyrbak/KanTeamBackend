package szczkrzy.kanteam.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupRequest {

    private String email;
    private String password;
    private String fullName;

}

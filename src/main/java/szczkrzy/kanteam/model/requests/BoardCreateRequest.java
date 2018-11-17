package szczkrzy.kanteam.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardCreateRequest {

    private String name;
    private int team;
    private int user;
}

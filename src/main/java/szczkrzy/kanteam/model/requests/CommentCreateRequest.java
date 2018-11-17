package szczkrzy.kanteam.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentCreateRequest {

    private int userId;
    private String comment;
}

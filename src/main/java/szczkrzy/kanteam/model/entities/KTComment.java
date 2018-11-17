package szczkrzy.kanteam.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import szczkrzy.kanteam.model.requests.CommentCreateRequest;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KTComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private int id;

    @NotNull
    @Column
    private String comment;

    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @NotNull
    private KTUser user;

    @JoinColumn(name = "task_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JsonIgnore
    private KTTask task;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date date;

    public KTComment(CommentCreateRequest commentRequest) {
        comment = commentRequest.getComment();
    }
}

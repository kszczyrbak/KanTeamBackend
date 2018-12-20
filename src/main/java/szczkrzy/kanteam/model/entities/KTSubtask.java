package szczkrzy.kanteam.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KTSubtask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subtask_id")
    private int id;

    @NotNull
    @Column
    private String name;

    @Column
    private boolean completed;

    @JoinColumn(name = "task_id")
    @ManyToOne
    @JsonIgnore
    private KTTask task;
}

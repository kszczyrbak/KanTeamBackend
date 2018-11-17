package szczkrzy.kanteam.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KTColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "column_id")
    private int id;

    @NotNull
    @Column
    private String name;

    @OneToMany(mappedBy = "column")
    private List<KTTask> tasks;

    @JoinColumn(name = "board_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JsonIgnore
    private KTBoard board;

    @Column(name = "col_order")
    @Basic
    private int order;
}

package szczkrzy.kanteam.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "column", cascade = CascadeType.DETACH, orphanRemoval = true)
    private List<KTTask> tasks;

    @JoinColumn(name = "board_id")
    @ManyToOne
    @JsonIgnore
    private KTBoard board;

    @Column(name = "col_order")
    @Basic
    private int order;

    @Column
    @Basic
    private int taskLimit;
}

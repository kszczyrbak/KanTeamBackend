package szczkrzy.kanteam.model.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import szczkrzy.kanteam.model.enums.TaskColor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KTColorMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "mapping_id")
    @JsonIgnore
    private int id;

    @Enumerated(EnumType.STRING)
    @Column
    private TaskColor color;
    @Basic
    @Column
    @NotNull
    private String description;
    @JoinColumn(name = "board_id")
    @ManyToOne
    @JsonIgnore
    private KTBoard board;

    @JsonGetter
    private int getColor() {
        return color.getValue();
    }

}

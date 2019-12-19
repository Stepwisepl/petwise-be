package pl.stepwise.petwise.pet.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pet_seq")
    @SequenceGenerator(name = "pet_seq")
    private Long id;

    @OneToMany(mappedBy = "pet")
    @JsonIgnore
    private List<MissingReport> missingReport;

    @NotNull
    private String imageId;

    @NotNull
    private String name;
}

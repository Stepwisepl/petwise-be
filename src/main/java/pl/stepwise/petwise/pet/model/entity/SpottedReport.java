package pl.stepwise.petwise.pet.model.entity;

import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class SpottedReport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spotted_report_seq")
    @SequenceGenerator(name = "spotted_report_seq")
    private Long id;

    @ManyToOne
    private MissingReport missingReport;

    @NotNull
    private Point placeOfFinding;

    @NotNull
    private LocalDateTime timeOfFinding;

    @NotNull
    private String imageId;
}

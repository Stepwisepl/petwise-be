package pl.stepwise.petwise.pet.model.entity;

import lombok.*;
import org.locationtech.jts.geom.Point;
import pl.stepwise.petwise.pet.model.MissingReportStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class MissingReport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "missing_report_seq")
    @SequenceGenerator(name = "missing_report_seq")
    private Long id;

    @ManyToOne
    private Pet pet;

    @OneToMany(mappedBy = "missingReport")
    private List<PetSpottedNotification> notifications;

    @NotNull
    private Point placeOfMissing;

    private MissingReportStatus status;

    @NotNull
    private LocalDateTime reportTime;
}

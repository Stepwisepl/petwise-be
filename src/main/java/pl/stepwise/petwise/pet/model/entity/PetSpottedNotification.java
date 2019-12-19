package pl.stepwise.petwise.pet.model.entity;

import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class PetSpottedNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pet_spotted_notification_seq")
    @SequenceGenerator(name = "pet_spotted_notification_seq")
    private Long id;

    @ManyToOne
    private MissingReport missingReport;

    private Point placeOfFinding;

    private LocalDateTime timeOfFinding;
}

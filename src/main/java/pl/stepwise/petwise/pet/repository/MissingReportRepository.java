package pl.stepwise.petwise.pet.repository;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.stepwise.petwise.pet.model.entity.MissingReport;

import java.util.List;

public interface MissingReportRepository extends CrudRepository<MissingReport, Long> {

    @Query("SELECT m" +
            " FROM MissingReport m" +
            " LEFT JOIN FETCH m.spottedReports n" +
            " WHERE WITHIN(m.placeOfMissing, ?1) = true")
    List<MissingReport> findAllWithin(Geometry location);
}

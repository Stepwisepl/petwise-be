package pl.stepwise.petwise.pet.repository;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.stepwise.petwise.pet.model.entity.SpottedReport;

import java.util.List;

public interface SpottedReportRepository extends CrudRepository<SpottedReport, Long> {

    @Query("SELECT p" +
            " FROM SpottedReport p" +
            " WHERE WITHIN(p.placeOfFinding, ?1) = true")
    List<SpottedReport> findAllWithin(Geometry location);
}

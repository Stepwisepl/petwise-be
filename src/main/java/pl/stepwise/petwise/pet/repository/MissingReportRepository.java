package pl.stepwise.petwise.pet.repository;

import org.springframework.data.repository.CrudRepository;
import pl.stepwise.petwise.pet.model.entity.MissingReport;

public interface MissingReportRepository extends CrudRepository<MissingReport, Long> {
}

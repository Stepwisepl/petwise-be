package pl.stepwise.petwise.pet.repository;

import org.springframework.data.repository.CrudRepository;
import pl.stepwise.petwise.pet.model.entity.Pet;

public interface PetRepository extends CrudRepository<Pet, Long> {

}

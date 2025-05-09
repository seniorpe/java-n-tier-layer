package pe.senior.pets.respository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.senior.pets.respository.model.Pet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PetRepository extends ReactiveCrudRepository<Pet, Long> {
    Flux<Pet> findByIsActiveTrue();

    Flux<Pet> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT COUNT(*) FROM pets WHERE is_active = true")
    Mono<Long> countByIsActiveTrue();

    @Query("SELECT EXISTS(SELECT 1 FROM pets WHERE name = :name AND species = :species AND is_active = true)")
    Mono<Boolean> existsByNameAndSpeciesAndIsActiveTrue(String name, String species);
}

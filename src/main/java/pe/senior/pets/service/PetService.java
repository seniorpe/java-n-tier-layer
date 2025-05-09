package pe.senior.pets.service;

import pe.senior.pets.controller.dto.PageResponse;
import pe.senior.pets.controller.dto.PetResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PetService {
    Flux<PetResponse> getAllPets();
    Mono<PageResponse<PetResponse>> getAllPetsPaginated(int page, int size);
}

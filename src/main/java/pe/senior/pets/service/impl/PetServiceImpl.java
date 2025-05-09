package pe.senior.pets.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.senior.pets.controller.dto.PageResponse;
import pe.senior.pets.controller.dto.PetRequest;
import pe.senior.pets.controller.dto.PetResponse;
import pe.senior.pets.respository.PetRepository;
import pe.senior.pets.respository.model.Pet;
import pe.senior.pets.service.PetService;
import pe.senior.pets.service.mapper.PetMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Override
    public Mono<PetResponse> createPet(PetRequest petRequest) {
        Pet pet = petMapper.toPetEntity(petRequest);
        pet.setIsActive(true);
        pet.setCreatedAt(LocalDateTime.now());
        pet.setUpdatedAt(LocalDateTime.now());

        return petRepository.save(pet)
                .map(petMapper::toPetResponse);
    }

    @Override
    public Flux<PetResponse> getAllPets() {
        return petRepository.findAll().map(petMapper::toPetResponse);
    }

    @Override
    public Mono<PageResponse<PetResponse>> getAllPetsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return petRepository.countByIsActiveTrue()
                .flatMap(total -> {
                    // Fixed calculation: Explicitly cast to double before division
                    int totalPages = size > 0 ? (int) Math.ceil((double) total / (double) size) : 0;
                    boolean isLast = page >= totalPages - 1;

                    return petRepository.findByIsActiveTrue(pageable)
                            .map(petMapper::toPetResponse)
                            .collectList()
                            .map(petResponses -> PageResponse.<PetResponse>builder()
                                    .content(petResponses)
                                    .pageNumber(page)
                                    .pageSize(size)
                                    .totalElements(total)
                                    .totalPages(totalPages)
                                    .last(isLast)
                                    .build());
                });
    }
}

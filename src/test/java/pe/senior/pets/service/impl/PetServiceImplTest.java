package pe.senior.pets.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pe.senior.pets.controller.dto.PageResponse;
import pe.senior.pets.controller.dto.PetRequest;
import pe.senior.pets.controller.dto.PetResponse;
import pe.senior.pets.respository.PetRepository;
import pe.senior.pets.respository.model.Pet;
import pe.senior.pets.service.mapper.PetMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.mockito.Mockito.*;

class PetServiceImplTest {
    @Mock
    PetRepository petRepository;
    @Mock
    PetMapper petMapper;
    @InjectMocks
    PetServiceImpl petServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePet() {
        Pet petEntity = new Pet(
                1L, "name", "species", "breed",
                LocalDate.of(2025, Month.MAY, 9), "gender", "description",
                Boolean.TRUE,
                LocalDateTime.of(2025, Month.MAY, 9, 14, 14, 7),
                LocalDateTime.of(2025, Month.MAY, 9, 14, 14, 7)
        );

        when(petMapper.toPetEntity(any(PetRequest.class))).thenReturn(petEntity);
        when(petRepository.save(any(Pet.class))).thenReturn(Mono.just(petEntity));
        when(petMapper.toPetResponse(any(Pet.class))).thenReturn(PetResponse.builder().name("Firu").build());

        Mono<PetResponse> result = petServiceImpl.createPet(new PetRequest("name", "species", "breed", LocalDate.of(2025, Month.MAY, 9), "gender", "description"));

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("Firu"))
                .verifyComplete();
    }

    @Test
    void testGetAllPets() {
        Pet pet = new Pet(1L, "name", "species", "breed", LocalDate.now(), "gender", "description", true, LocalDateTime.now(), LocalDateTime.now());
        when(petRepository.findAll()).thenReturn(Flux.just(pet));
        when(petMapper.toPetResponse(pet)).thenReturn(PetResponse.builder().name("Firu").build());

        Flux<PetResponse> result = petServiceImpl.getAllPets();

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }


    @Test
    void testGetAllPetsPaginated() {
        Pageable pageable = PageRequest.of(0, 2);
        List<Pet> pets = List.of(
                new Pet(1L, "name1", "species", "breed", LocalDate.now(), "gender", "desc", true, LocalDateTime.now(), LocalDateTime.now()),
                new Pet(2L, "name2", "species", "breed", LocalDate.now(), "gender", "desc", true, LocalDateTime.now(), LocalDateTime.now())
        );

        when(petRepository.findByIsActiveTrue(any(Pageable.class))).thenReturn(Flux.fromIterable(pets));
        when(petRepository.countByIsActiveTrue()).thenReturn(Mono.just(2L));
        when(petMapper.toPetResponse(any(Pet.class))).thenAnswer(inv -> {
            Pet p = inv.getArgument(0);
            return PetResponse.builder().name("Firu").build();
        });

        Mono<PageResponse<PetResponse>> result = petServiceImpl.getAllPetsPaginated(0, 2);

        StepVerifier.create(result)
                .expectNextMatches(page -> page.getTotalElements() == 2 && page.getContent().size() == 2)
                .verifyComplete();
    }

}

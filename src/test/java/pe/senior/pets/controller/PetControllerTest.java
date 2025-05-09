package pe.senior.pets.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.senior.pets.controller.dto.PageResponse;
import pe.senior.pets.controller.dto.PetRequest;
import pe.senior.pets.controller.dto.PetResponse;
import pe.senior.pets.service.PetService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class PetControllerTest {
    @Mock
    PetService petService;
    @InjectMocks
    PetController petController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePet() {
        when(petService.createPet(any(PetRequest.class))).thenReturn(null);

        Mono<PetResponse> result = petController.createPet(new PetRequest("name", "species", "breed", LocalDate.of(2025, Month.MAY, 9), "gender", "description"));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetAllPets() {
        when(petService.getAllPets()).thenReturn(Flux.empty());

        Mono<List<PetResponse>> result = petController.getAllPets();

        StepVerifier.create(result)
                .expectNext(Collections.emptyList())
                .verifyComplete();
    }

    @Test
    void testGetAllPetsWithData() {
        List<PetResponse> pets = List.of(PetResponse.builder().name("Firu").build(),
                PetResponse.builder().name("Michi").build());
        when(petService.getAllPets()).thenReturn(Flux.fromIterable(pets));

        Mono<List<PetResponse>> result = petController.getAllPets();

        StepVerifier.create(result)
                .expectNext(pets)
                .verifyComplete();
    }


    @Test
    void testGetPaginatedPets_empty() {
        when(petService.getAllPetsPaginated(anyInt(), anyInt()))
                .thenReturn(Mono.empty());

        Mono<PageResponse<PetResponse>> result = petController.getPaginatedPets(0, 0);

        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }

    @Test
    void testGetPaginatedPets_withData() {
        List<PetResponse> pets = List.of(PetResponse.builder().name("Firu").build(),
                PetResponse.builder().name("Michi").build());
        PageResponse<PetResponse> pageResponse = new PageResponse<>(pets, 0, 10, 2, 1, false);

        when(petService.getAllPetsPaginated(anyInt(), anyInt()))
                .thenReturn(Mono.just(pageResponse));

        Mono<PageResponse<PetResponse>> result = petController.getPaginatedPets(0, 10);

        StepVerifier.create(result)
                .expectNext(pageResponse)
                .verifyComplete();
    }

}

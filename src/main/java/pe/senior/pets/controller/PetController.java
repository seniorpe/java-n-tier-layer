package pe.senior.pets.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.senior.pets.controller.dto.PageResponse;
import pe.senior.pets.controller.dto.PetResponse;
import pe.senior.pets.service.PetService;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pets")
public class PetController {
    private final PetService petService;

    @GetMapping
    public Mono<List<PetResponse>> getAllPets(){
        return petService.getAllPets()
                .collectList();
    }

    @GetMapping("/paginated")
    public Mono<PageResponse<PetResponse>> getPaginatedPets(@RequestParam(required = false, defaultValue = "0") @Min(0) int page,
                                                      @RequestParam(required = false, defaultValue = "10") @Min(1) int size){
        return petService.getAllPetsPaginated(page, size);
    }
}

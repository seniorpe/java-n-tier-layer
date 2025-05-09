package pe.senior.pets.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import pe.senior.pets.controller.dto.PetRequest;
import pe.senior.pets.controller.dto.PetResponse;
import pe.senior.pets.respository.model.Pet;

@Mapper(componentModel = "spring")
public interface PetMapper {
    PetResponse toPetResponse(Pet source);

    Pet toPetEntity(PetRequest source);
}

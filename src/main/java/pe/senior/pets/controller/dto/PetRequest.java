package pe.senior.pets.controller.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetRequest {

    @NotBlank(message = "Pet name is required")
    @Length(min = 2, max = 100, message = "Pet name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Species is required")
    @Length(max = 50, message = "Species must not exceed 50 characters")
    private String species;

    @Length(max = 50, message = "Breed must not exceed 50 characters")
    private String breed;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @Pattern(regexp = "[MF]", message = "Gender must be 'M' for male or 'F' for female")
    private String gender;

    @Length(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}

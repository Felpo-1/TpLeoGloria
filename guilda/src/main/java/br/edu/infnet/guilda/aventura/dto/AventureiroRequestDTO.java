package br.edu.infnet.guilda.aventura.dto;

import br.edu.infnet.guilda.aventura.domain.ClasseAventureiro;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AventureiroRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotNull(message = "A classe é obrigatória")
    private ClasseAventureiro classe;

    @NotNull(message = "O nível é obrigatório")
    @Min(value = 1, message = "O nível deve ser maior ou igual a 1")
    private Integer nivel;
}

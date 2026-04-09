package br.edu.infnet.guilda.aventura.dto;

import br.edu.infnet.guilda.aventura.domain.EspecieCompanheiro;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompanheiroRequestDTO {

    @NotBlank(message = "O nome do companheiro é obrigatório")
    private String nome;

    @NotNull(message = "A espécie é obrigatória")
    private EspecieCompanheiro especie;

    @NotNull(message = "A lealdade é obrigatória")
    @Min(value = 0, message = "A lealdade mínima é 0")
    @Max(value = 100, message = "A lealdade máxima é 100")
    private Integer lealdade;
}

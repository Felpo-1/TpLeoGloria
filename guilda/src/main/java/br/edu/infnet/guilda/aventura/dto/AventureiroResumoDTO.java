package br.edu.infnet.guilda.aventura.dto;

import br.edu.infnet.guilda.aventura.domain.Aventureiro;
import lombok.Data;

@Data
public class AventureiroResumoDTO {
    private Long id;
    private String nome;
    private String classe;
    private Integer nivel;
    private Boolean ativo;

    public static AventureiroResumoDTO from(Aventureiro a) {
        AventureiroResumoDTO dto = new AventureiroResumoDTO();
        dto.setId(a.getId());
        dto.setNome(a.getNome());
        dto.setClasse(a.getClasse().name());
        dto.setNivel(a.getNivel());
        dto.setAtivo(a.getAtivo());
        return dto;
    }
}

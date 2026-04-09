package br.edu.infnet.guilda.aventura.dto;

import br.edu.infnet.guilda.aventura.domain.Aventureiro;
import lombok.Data;

@Data
public class AventureiroDetalheDTO {
    private Long id;
    private String nome;
    private String classe;
    private Integer nivel;
    private Boolean ativo;
    private CompanheiroDTO companheiro;

    public static AventureiroDetalheDTO from(Aventureiro a) {
        AventureiroDetalheDTO dto = new AventureiroDetalheDTO();
        dto.setId(a.getId());
        dto.setNome(a.getNome());
        dto.setClasse(a.getClasse().name());
        dto.setNivel(a.getNivel());
        dto.setAtivo(a.getAtivo());
        if (a.getCompanheiro() != null) {
            CompanheiroDTO comp = new CompanheiroDTO();
            comp.setNome(a.getCompanheiro().getNome());
            comp.setEspecie(a.getCompanheiro().getEspecie().name());
            comp.setLealdade(a.getCompanheiro().getIndiceLealdade());
            dto.setCompanheiro(comp);
        }
        return dto;
    }
    
    @Data
    public static class CompanheiroDTO {
        private String nome;
        private String especie;
        private Integer lealdade;
    }
}

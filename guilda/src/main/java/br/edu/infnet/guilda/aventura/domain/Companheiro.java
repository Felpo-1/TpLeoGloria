package br.edu.infnet.guilda.aventura.domain;

import jakarta.persistence.*;
import lombok.*;
import br.edu.infnet.guilda.aventura.domain.enums.EspecieCompanheiro;

import java.io.Serializable;

@Entity
@Table(name = "companheiro", schema = "aventura")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Companheiro implements Serializable {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "aventureiro_id")
    private Aventureiro aventureiro;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private EspecieCompanheiro especie;

    @Column(name = "indice_lealdade", nullable = false)
    private Integer indiceLealdade;
}

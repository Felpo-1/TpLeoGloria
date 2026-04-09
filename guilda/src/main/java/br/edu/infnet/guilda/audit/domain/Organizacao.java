package br.edu.infnet.guilda.audit.domain;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Table(name = "organizacoes", schema = "audit")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Organizacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}

package br.edu.infnet.guilda.audit.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions", schema = "audit")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80, unique = true)
    private String code;

    @Column(nullable = false, length = 255)
    private String descricao;
}

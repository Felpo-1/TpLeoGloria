package br.edu.infnet.guilda.aventura.domain;

import br.edu.infnet.guilda.audit.domain.Organizacao;
import br.edu.infnet.guilda.audit.domain.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "aventureiro", schema = "aventura")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Aventureiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_cadastro_id", nullable = false)
    private Usuario usuarioCadastro;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ClasseAventureiro classe;

    @Column(nullable = false)
    private Integer nivel;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @OneToOne(mappedBy = "aventureiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private Companheiro companheiro;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = this.dataCriacao;
        if (this.ativo == null) this.ativo = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
} 




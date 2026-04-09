package br.edu.infnet.guilda.aventura.domain;

import br.edu.infnet.guilda.audit.domain.Organizacao;
import jakarta.persistence.*;
import lombok.*;
import br.edu.infnet.guilda.aventura.domain.enums.NivelPerigoMissao;
import br.edu.infnet.guilda.aventura.domain.enums.StatusMissao;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "missao", schema = "aventura")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Missao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_perigo", nullable = false, length = 20)
    private NivelPerigoMissao nivelPerigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMissao status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @OneToMany(mappedBy = "missao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipacaoMissao> participacoes;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }
}

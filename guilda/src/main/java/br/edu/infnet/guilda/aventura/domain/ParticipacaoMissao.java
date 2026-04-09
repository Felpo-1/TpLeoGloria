package br.edu.infnet.guilda.aventura.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "participacao_missao", schema = "aventura")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParticipacaoMissao {

    @EmbeddedId
    private ParticipacaoMissaoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("missaoId")
    @JoinColumn(name = "missao_id")
    private Missao missao;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("aventureiroId")
    @JoinColumn(name = "aventureiro_id")
    private Aventureiro aventureiro;

    @Column(nullable = false, length = 40)
    private String papel;

    @Column(name = "recompensa_ouro")
    private BigDecimal recompensaOuro;

    @Column(nullable = false)
    private Boolean destaque;

    @Column(name = "data_registro", nullable = false, updatable = false)
    private LocalDateTime dataRegistro;

    @PrePersist
    public void prePersist() {
        if (this.dataRegistro == null) this.dataRegistro = LocalDateTime.now();
        if (this.destaque == null) this.destaque = false;
        if (this.recompensaOuro == null) this.recompensaOuro = BigDecimal.ZERO;
    }
}

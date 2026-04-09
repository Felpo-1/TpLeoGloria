package br.edu.infnet.guilda.aventura.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParticipacaoMissaoId implements Serializable {

    @Column(name = "missao_id")
    private Long missaoId;

    @Column(name = "aventureiro_id")
    private Long aventureiroId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipacaoMissaoId that = (ParticipacaoMissaoId) o;
        return Objects.equals(missaoId, that.missaoId) &&
               Objects.equals(aventureiroId, that.aventureiroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(missaoId, aventureiroId);
    }
}

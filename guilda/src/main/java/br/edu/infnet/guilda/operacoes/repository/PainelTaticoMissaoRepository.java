package br.edu.infnet.guilda.operacoes.repository;

import br.edu.infnet.guilda.operacoes.domain.PainelTaticoMissao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PainelTaticoMissaoRepository extends JpaRepository<PainelTaticoMissao, Long> {

    @Query("SELECT p FROM PainelTaticoMissao p WHERE p.ultimaAtualizacao >= :limiteData ORDER BY p.indiceProntidao DESC")
    List<PainelTaticoMissao> findTopMissoesRecentes(LocalDateTime limiteData, Pageable pageable);

}

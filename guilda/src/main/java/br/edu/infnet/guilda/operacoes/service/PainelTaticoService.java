package br.edu.infnet.guilda.operacoes.service;

import br.edu.infnet.guilda.operacoes.domain.PainelTaticoMissao;
import br.edu.infnet.guilda.operacoes.repository.PainelTaticoMissaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PainelTaticoService {

    private final PainelTaticoMissaoRepository repository;

    /**
     * Retorna as top 10 missões com atualização nos últimos 15 dias, 
     * ordenadas por índice de prontidão decrescente.
     * Resultado é cacheadot para diminuir a carga no banco de dados.
     */
    @Cacheable(value = "topMissoesCache", key = "'top10_15dias'")
    public List<PainelTaticoMissao> getTop10MissoesTaticasUltimos15Dias() {
        log.info("Executando consulta pesada no banco de dados: Top 10 Missões Táticas (últimos 15 dias)");
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(15);
        return repository.findTopMissoesRecentes(dataLimite, PageRequest.of(0, 10));
    }
}

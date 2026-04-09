package br.edu.infnet.guilda.aventura.service;

import br.edu.infnet.guilda.aventura.domain.LogAventura;
import br.edu.infnet.guilda.aventura.repository.LogAventuraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogAventuraService {

    private final LogAventuraRepository repository;

    public void registrarLog(String operacao, String detalhes, Long usuarioId) {
        try {
            LogAventura logAventura = LogAventura.builder()
                    .operacao(operacao)
                    .detalhes(detalhes)
                    .usuarioId(usuarioId != null ? usuarioId : 0L)
                    .dataRegistro(LocalDateTime.now())
                    .build();
            repository.save(logAventura);
            log.info("Log registrado no MongoDB: {}", operacao);
        } catch (Exception e) {
            log.error("Erro ao tentar persistir log no MongoDB", e);
        }
    }
}

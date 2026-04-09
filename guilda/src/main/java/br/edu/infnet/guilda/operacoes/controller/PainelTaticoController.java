package br.edu.infnet.guilda.operacoes.controller;

import br.edu.infnet.guilda.operacoes.domain.PainelTaticoMissao;
import br.edu.infnet.guilda.operacoes.service.PainelTaticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/missoes")
@RequiredArgsConstructor
public class PainelTaticoController {

    private final PainelTaticoService painelTaticoService;

    @GetMapping("/top15dias")
    public List<PainelTaticoMissao> getTop15Dias() {
        return painelTaticoService.getTop10MissoesTaticasUltimos15Dias();
    }
}

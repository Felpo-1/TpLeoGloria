package br.edu.infnet.guilda.marketplace.controller;

import br.edu.infnet.guilda.marketplace.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/produtos/agregacoes")
@RequiredArgsConstructor
public class ProdutoAgregacaoController {

    private final ProdutoService produtoService;

    @GetMapping("/por-categoria")
    public Map<String, Long> quantidadePorCategoria() throws java.io.IOException {
        return produtoService.quantidadePorCategoria();
    }

    @GetMapping("/por-raridade")
    public Map<String, Long> quantidadePorRaridade() throws java.io.IOException {
        return produtoService.quantidadePorRaridade();
    }

    @GetMapping("/preco-medio")
    public Map<String, Double> precoMedio() throws java.io.IOException {
        return Map.of("precoMedio", produtoService.precoMedio());
    }

    @GetMapping("/faixas-preco")
    public Map<String, Long> faixasPreco() throws java.io.IOException {
        return produtoService.faixasPreco();
    }
}

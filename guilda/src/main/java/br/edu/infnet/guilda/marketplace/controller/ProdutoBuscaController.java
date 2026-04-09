package br.edu.infnet.guilda.marketplace.controller;

import br.edu.infnet.guilda.marketplace.domain.Produto;
import br.edu.infnet.guilda.marketplace.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos/busca")
@RequiredArgsConstructor
public class ProdutoBuscaController {

    private final ProdutoService produtoService;

    @GetMapping("/nome")
    public List<Produto> buscarPorNome(@RequestParam String termo) {
        return produtoService.buscarPorNome(termo);
    }

    @GetMapping("/descricao")
    public List<Produto> buscarPorDescricao(@RequestParam String termo) {
        return produtoService.buscarPorDescricao(termo);
    }

    @GetMapping("/frase")
    public List<Produto> buscarPorFrase(@RequestParam String termo) {
        return produtoService.buscarPorFrase(termo);
    }

    @GetMapping("/fuzzy")
    public List<Produto> buscarFuzzy(@RequestParam String termo) {
        return produtoService.buscarFuzzy(termo);
    }

    @GetMapping("/multicampos")
    public List<Produto> buscarMultiCampos(@RequestParam String termo) {
        return produtoService.buscarMultiCampos(termo);
    }

    @GetMapping("/com-filtro")
    public List<Produto> buscarComFiltro(@RequestParam String termo, @RequestParam String categoria) {
        return produtoService.buscarComFiltroCategoria(termo, categoria);
    }

    @GetMapping("/faixa-preco")
    public List<Produto> buscarPorFaixaPreco(@RequestParam Double min, @RequestParam Double max) {
        return produtoService.buscarPorFaixaPreco(min, max);
    }

    @GetMapping("/avancada")
    public List<Produto> buscaAvancada(@RequestParam String categoria, 
                                       @RequestParam String raridade, 
                                       @RequestParam Double min, 
                                       @RequestParam Double max) {
        return produtoService.buscaAvancada(categoria, raridade, min, max);
    }
}

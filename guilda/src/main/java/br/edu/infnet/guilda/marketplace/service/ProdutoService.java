package br.edu.infnet.guilda.marketplace.service;

import br.edu.infnet.guilda.marketplace.domain.Produto;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final co.elastic.clients.elasticsearch.ElasticsearchClient elasticsearchClient;


    public List<Produto> buscarPorNome(String termo) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("nome").query(termo)))
                .build();
        return executarBusca(query);
    }

    public List<Produto> buscarPorDescricao(String termo) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("descricao").query(termo)))
                .build();
        return executarBusca(query);
    }

    public List<Produto> buscarPorFrase(String frase) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.matchPhrase(m -> m.field("descricao").query(frase)))
                .build();
        return executarBusca(query);
    }

    public List<Produto> buscarFuzzy(String termo) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.fuzzy(f -> f.field("nome").value(termo).fuzziness("AUTO")))
                .build();
        return executarBusca(query);
    }

    public List<Produto> buscarMultiCampos(String termo) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(m -> m.fields("nome", "descricao").query(termo)))
                .build();
        return executarBusca(query);
    }


    public List<Produto> buscarComFiltroCategoria(String termo, String categoria) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b
                        .must(m -> m.match(match -> match.field("descricao").query(termo)))
                        .filter(f -> f.term(t -> t.field("categoria").value(categoria)))
                ))
                .build();
        return executarBusca(query);
    }

    public List<Produto> buscarPorFaixaPreco(Double min, Double max) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.range(r -> r.field("preco").gte(co.elastic.clients.json.JsonData.of(min)).lte(co.elastic.clients.json.JsonData.of(max))))
                .build();
        return executarBusca(query);
    }

    public List<Produto> buscaAvancada(String categoria, String raridade, Double min, Double max) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b
                        .filter(f1 -> f1.term(t -> t.field("categoria").value(categoria)))
                        .filter(f2 -> f2.term(t -> t.field("raridade").value(raridade)))
                        .filter(f3 -> f3.range(r -> r.field("preco").gte(co.elastic.clients.json.JsonData.of(min)).lte(co.elastic.clients.json.JsonData.of(max))))
                ))
                .build();
        return executarBusca(query);
    }


    public Map<String, Long> quantidadePorCategoria() throws java.io.IOException {
        Map<String, Long> map = new HashMap<>();
        var response = elasticsearchClient.search(s -> s
                .index("guilda_loja")
                .size(0)
                .aggregations("por_categoria", a -> a.terms(t -> t.field("categoria").size(100))), 
                Void.class);
        var agg = response.aggregations().get("por_categoria");
        if (agg != null && agg.isSterms()) {
            for (var bucket : agg.sterms().buckets().array()) {
                map.put(bucket.key().stringValue(), bucket.docCount());
            }
        }
        return map;
    }

    public Map<String, Long> quantidadePorRaridade() throws java.io.IOException {
        Map<String, Long> map = new HashMap<>();
        var response = elasticsearchClient.search(s -> s
                .index("guilda_loja")
                .size(0)
                .aggregations("por_raridade", a -> a.terms(t -> t.field("raridade").size(100))), 
                Void.class);
        var agg = response.aggregations().get("por_raridade");
        if (agg != null && agg.isSterms()) {
            for (var bucket : agg.sterms().buckets().array()) {
                map.put(bucket.key().stringValue(), bucket.docCount());
            }
        }
        return map;
    }

    public Double precoMedio() throws java.io.IOException {
        var response = elasticsearchClient.search(s -> s
                .index("guilda_loja")
                .size(0)
                .aggregations("preco_medio", a -> a.avg(avg -> avg.field("preco"))), 
                Void.class);
        var agg = response.aggregations().get("preco_medio");
        if (agg != null && agg.isAvg()) {
            return agg.avg().value();
        }
        return 0.0;
    }

    public Map<String, Long> faixasPreco() throws java.io.IOException {
        Map<String, Long> map = new HashMap<>();
        var response = elasticsearchClient.search(s -> s
                .index("guilda_loja")
                .size(0)
                .aggregations("faixas", a -> a.range(r -> r.field("preco")
                        .ranges(rg -> rg.to("100.0"))
                        .ranges(rg -> rg.from("100.0").to("300.0"))
                        .ranges(rg -> rg.from("300.0").to("700.0"))
                        .ranges(rg -> rg.from("700.0"))
                )), Void.class);
        var agg = response.aggregations().get("faixas");
        if (agg != null && agg.isRange()) {
            for (var bucket : agg.range().buckets().array()) {
                String key = "De " + (bucket.from() != null ? bucket.from() : "0") + 
                             " a " + (bucket.to() != null ? bucket.to() : "Infinito");
                map.put(key, bucket.docCount());
            }
        }
        return map;
    }

    private List<Produto> executarBusca(NativeQuery query) {
        return elasticsearchOperations.search(query, Produto.class)
                .getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }
}

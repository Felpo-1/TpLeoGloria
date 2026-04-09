package br.edu.infnet.guilda.marketplace.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "guilda_loja", createIndex = false)
public class Produto {

    @Id
    private String id;

    @Field(type = FieldType.Text, name = "nome")
    private String nome;

    @Field(type = FieldType.Text, name = "descricao")
    private String descricao;

    @Field(type = FieldType.Keyword, name = "categoria")
    private String categoria;

    @Field(type = FieldType.Keyword, name = "raridade")
    private String raridade;

    @Field(type = FieldType.Double, name = "preco")
    private Double preco;
}

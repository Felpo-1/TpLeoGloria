package br.edu.infnet.guilda.aventura.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "log_aventura")
public class LogAventura {

    @Id
    private String id;
    private String operacao;
    private String detalhes;
    private Long usuarioId;
    private LocalDateTime dataRegistro;

}

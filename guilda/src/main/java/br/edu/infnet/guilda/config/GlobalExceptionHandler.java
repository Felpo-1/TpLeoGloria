package br.edu.infnet.guilda.config;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessResourceFailureException;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DataAccessResourceFailureException.class, IOException.class})
    public ResponseEntity<ErroResposta> handleElasticsearchConnectionFailure(Exception ex) {
        ErroResposta erro = new ErroResposta("Serviço de Marketplace (Elasticsearch) temporariamente indisponível", List.of("Verifique se o servidor na porta 9200 está online e operante.", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(erro);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroResposta> handleEntityNotFound(EntityNotFoundException ex) {
        ErroResposta erro = new ErroResposta("recurso não encontrado", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String field = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    return field + ": " + message;
                })
                .collect(Collectors.toList());

        ErroResposta erro = new ErroResposta("Solicitação inválida", detalhes);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResposta> handleIllegalArgument(IllegalArgumentException ex) {
        ErroResposta erro = new ErroResposta("Solicitação inválida", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    public record ErroResposta(String mensagem, List<String> detalhes) {
    }
}

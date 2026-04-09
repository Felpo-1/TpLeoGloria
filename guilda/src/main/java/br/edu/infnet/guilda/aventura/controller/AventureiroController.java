package br.edu.infnet.guilda.aventura.controller;

import br.edu.infnet.guilda.aventura.domain.Aventureiro;
import br.edu.infnet.guilda.aventura.domain.ClasseAventureiro;
import br.edu.infnet.guilda.aventura.dto.AventureiroDetalheDTO;
import br.edu.infnet.guilda.aventura.dto.AventureiroRequestDTO;
import br.edu.infnet.guilda.aventura.dto.AventureiroResumoDTO;
import br.edu.infnet.guilda.aventura.dto.CompanheiroRequestDTO;
import br.edu.infnet.guilda.aventura.service.AventureiroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aventureiros")
@RequiredArgsConstructor
public class AventureiroController {

    private final AventureiroService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AventureiroResumoDTO registrar(@RequestBody @Valid AventureiroRequestDTO dto, 
                                          @RequestHeader(value = "X-Org-Id", defaultValue = "1") Long orgId,
                                          @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId) {
        return AventureiroResumoDTO.from(service.registrar(dto, orgId, userId));
    }

    @GetMapping
    public ResponseEntity<List<AventureiroResumoDTO>> listar(
            @RequestParam(required = false) ClasseAventureiro classe,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) Integer nivelMin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        if (size > 50) size = 50;

        Page<Aventureiro> pg = service.listar(classe, ativo, nivelMin, PageRequest.of(page, size, Sort.by("id").ascending()));

        List<AventureiroResumoDTO> respostas = pg.getContent().stream()
                .map(AventureiroResumoDTO::from).toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(pg.getTotalElements()))
                .header("X-Page", String.valueOf(pg.getNumber()))
                .header("X-Size", String.valueOf(pg.getSize()))
                .header("X-Total-Pages", String.valueOf(pg.getTotalPages()))
                .body(respostas);
    }

    @GetMapping("/{id}")
    public AventureiroDetalheDTO consultar(@PathVariable Long id) {
        return AventureiroDetalheDTO.from(service.consultarPorId(id));
    }

    @PutMapping("/{id}")
    public AventureiroDetalheDTO atualizar(@PathVariable Long id, @RequestBody @Valid AventureiroRequestDTO dto) {
        return AventureiroDetalheDTO.from(service.atualizarDado(id, dto));
    }

    @PatchMapping("/{id}/inativar")
    public void inativar(@PathVariable Long id) {
        service.inativar(id);
    }

    @PatchMapping("/{id}/ativar")
    public void ativar(@PathVariable Long id) {
        service.ativar(id);
    }

    @PutMapping("/{id}/companheiro")
    public AventureiroDetalheDTO definirCompanheiro(@PathVariable Long id, @RequestBody @Valid CompanheiroRequestDTO dto) {
        return AventureiroDetalheDTO.from(service.atualizarCompanheiro(id, dto));
    }

    @DeleteMapping("/{id}/companheiro")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerCompanheiro(@PathVariable Long id) {
        service.removerCompanheiro(id);
    }
}

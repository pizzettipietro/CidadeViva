// Define o pacote dos controllers.
package api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import api.dto.CidadaoRequestDTO;
import api.dto.CidadaoResponseDTO;
import api.service.CidadaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

// CAMADA: Controller
// RESPONSABILIDADE: porta de entrada dos cidadãos. Só recebe a requisição, chama o
//                   Service e devolve a resposta: nenhuma regra de negócio aqui.
// ROTAS ATENDIDAS: POST /cidadaos, GET /cidadaos e GET /cidadaos/{id}.
//                  PUT e DELETE ficam para as próximas etapas.
// SITUAÇÃO: implementada.
@RestController
@RequestMapping("/cidadaos")
@Tag(name = "Cidadãos", description = "Pessoas que reportam problemas urbanos")
public class CidadaoController {

    private final CidadaoService service;

    public CidadaoController(CidadaoService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastra um cidadão")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CidadaoResponseDTO criar(@Valid @RequestBody CidadaoRequestDTO dto) {
        return service.criar(dto);
    }

    @Operation(summary = "Lista os cidadãos")
    @GetMapping
    public List<CidadaoResponseDTO> listar() {
        return service.listar();
    }

    @Operation(summary = "Busca um cidadão pelo id")
    @GetMapping("/{id}")
    public CidadaoResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
}

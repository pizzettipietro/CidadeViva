// Define o pacote da camada de conversão.
package api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import api.dto.CidadaoRequestDTO;
import api.dto.CidadaoResponseDTO;
import api.model.Cidadao;

// CAMADA: Mapper
// RESPONSABILIDADE: converte entre CidadaoRequestDTO/CidadaoResponseDTO e a
//                   entidade Cidadao. Não consulta o banco e não tem regra de negócio.
// SITUAÇÃO: implementada.
@Component
public class CidadaoMapper {

    // DTO de entrada em entidade nova.
    public Cidadao toEntity(CidadaoRequestDTO dto) {
        return new Cidadao(dto.nome(), dto.email());
    }

    // Entidade em DTO de saída.
    public CidadaoResponseDTO toResponse(Cidadao cidadao) {
        return new CidadaoResponseDTO(cidadao.getId(), cidadao.getNome(), cidadao.getEmail());
    }

    // Lista de entidades em lista de DTOs.
    public List<CidadaoResponseDTO> toResponseList(List<Cidadao> cidadaos) {
        return cidadaos.stream().map(this::toResponse).toList();
    }
}

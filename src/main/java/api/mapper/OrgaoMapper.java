// Define o pacote da camada de conversão.
package api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import api.dto.OrgaoRequestDTO;
import api.dto.OrgaoResponseDTO;
import api.model.Orgao;

// CAMADA: Mapper
// RESPONSABILIDADE: converte entre OrgaoRequestDTO/OrgaoResponseDTO e a entidade
//                   Orgao. Não consulta o banco e não tem regra de negócio.
// SITUAÇÃO: implementada.
@Component
public class OrgaoMapper {

    // DTO de entrada em entidade nova (nasce ativa).
    public Orgao toEntity(OrgaoRequestDTO dto) {
        return new Orgao(dto.nome(), dto.tipoAtendido());
    }

    // Entidade em DTO de saída.
    public OrgaoResponseDTO toResponse(Orgao orgao) {
        return new OrgaoResponseDTO(orgao.getId(), orgao.getNome(), orgao.getTipoAtendido(), orgao.isAtivo());
    }

    // Lista de entidades em lista de DTOs.
    public List<OrgaoResponseDTO> toResponseList(List<Orgao> orgaos) {
        return orgaos.stream().map(this::toResponse).toList();
    }
}

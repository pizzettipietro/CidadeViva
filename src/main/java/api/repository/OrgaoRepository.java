// Define o pacote da camada de acesso ao banco.
package api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import api.model.Orgao;
import api.model.TipoOcorrencia;

// CAMADA: Repository
// RESPONSABILIDADE: lê e grava órgãos no banco. Além do básico do JpaRepository,
//                   acha o órgão ativo que atende um tipo de ocorrência (usado pela R1).
// SITUAÇÃO: implementada.
public interface OrgaoRepository extends JpaRepository<Orgao, Long> {

    // O Spring monta o SQL pelo nome: tipo_atendido = ? AND ativo = true, o de menor id.
    // O "First" e o OrderByIdAsc garantem sempre o mesmo órgão se houver mais de um do tipo.
    Optional<Orgao> findFirstByTipoAtendidoAndAtivoTrueOrderByIdAsc(TipoOcorrencia tipoAtendido);
}

// Define o pacote da camada de acesso ao banco.
package api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.model.Cidadao;

// CAMADA: Repository
// RESPONSABILIDADE: lê e grava cidadãos no banco. Confere se um e-mail já está em uso.
// SITUAÇÃO: implementada.
public interface CidadaoRepository extends JpaRepository<Cidadao, Long> {

    // O Spring monta o SQL pelo nome: existe algum cidadão com este e-mail?
    boolean existsByEmail(String email);
}

// Define o pacote das entidades do domínio.
package api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// CAMADA: Model
// RESPONSABILIDADE: representa a tabela orgaos: a secretaria ou autoridade que
//                   recebe e resolve as ocorrências de um tipo.
// SITUAÇÃO: implementada.
@Entity
@Table(name = "orgaos")
public class Orgao {

    // O banco gera o id (BIGSERIAL na migration V1).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    // Grava o nome do enum como texto (BURACO, COLETA_LIXO...), não o número.
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_atendido", nullable = false, length = 30)
    private TipoOcorrencia tipoAtendido;

    @Column(nullable = false)
    private boolean ativo;

    // Exigido pelo Hibernate.
    protected Orgao() {
    }

    // Todo órgão nasce ativo: o cliente não controla isso.
    public Orgao(String nome, TipoOcorrencia tipoAtendido) {
        this.nome = nome;
        this.tipoAtendido = tipoAtendido;
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public TipoOcorrencia getTipoAtendido() {
        return tipoAtendido;
    }

    public boolean isAtivo() {
        return ativo;
    }
}

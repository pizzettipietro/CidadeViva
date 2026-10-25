// Define o pacote da camada de serviço.
package api.service;

// CAMADA: Service
// RESPONSABILIDADE: sinaliza que não existe um órgão com o id pedido. Por herdar de
//                   RecursoNaoEncontradoException, o ApiExceptionHandler devolve 404.
// SITUAÇÃO: implementada.
public class OrgaoNaoEncontradoException extends RecursoNaoEncontradoException {

    // Monta a mensagem que aparece no campo "mensagem" do ErroDTO.
    public OrgaoNaoEncontradoException(Long id) {
        super("Órgão não encontrado: " + id);
    }
}

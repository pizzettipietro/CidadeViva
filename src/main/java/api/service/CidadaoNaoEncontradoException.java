// Define o pacote da camada de serviço.
package api.service;

// CAMADA: Service
// RESPONSABILIDADE: sinaliza que não existe um cidadão com o id pedido. Por herdar de
//                   RecursoNaoEncontradoException, o ApiExceptionHandler devolve 404.
// SITUAÇÃO: implementada.
public class CidadaoNaoEncontradoException extends RecursoNaoEncontradoException {

    // Monta a mensagem que aparece no campo "mensagem" do ErroDTO.
    public CidadaoNaoEncontradoException(Long id) {
        super("Cidadão não encontrado: " + id);
    }
}

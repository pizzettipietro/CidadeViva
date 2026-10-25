// Define o pacote da camada de serviço.
package api.service;

// CAMADA: Service
// RESPONSABILIDADE: sinaliza que não existe uma ocorrência com o id pedido. Por herdar de
//                   RecursoNaoEncontradoException, o ApiExceptionHandler devolve 404.
// SITUAÇÃO: implementada.
public class OcorrenciaNaoEncontradaException extends RecursoNaoEncontradoException {

    // Monta a mensagem que aparece no campo "mensagem" do ErroDTO.
    public OcorrenciaNaoEncontradaException(Long id) {
        super("Ocorrência não encontrada: " + id);
    }
}

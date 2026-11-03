package br.unesp.fct.es2.admshopping.comum;

/**
 * Lançada quando o usuário não tem permissão para a operação (RNF 3.0, Anexo A).
 */
public class AcessoNegadoException extends RuntimeException {

    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }
}

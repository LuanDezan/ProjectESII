package br.unesp.fct.es2.admshopping.comum;

/**
 * Lançada quando uma operação viola uma regra de negócio
 * (campo obrigatório vazio, transição de status inválida etc.).
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}

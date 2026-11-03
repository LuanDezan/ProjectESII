package br.unesp.fct.es2.admshopping.chamados;

/**
 * Status de um chamado (RF 3.1). Concluído e Cancelado são finais.
 */
public enum StatusChamado {
    PENDENTE,
    EM_ANDAMENTO,
    CONCLUIDO,
    CANCELADO;

    public boolean ehFinal() {
        return this == CONCLUIDO || this == CANCELADO;
    }
}

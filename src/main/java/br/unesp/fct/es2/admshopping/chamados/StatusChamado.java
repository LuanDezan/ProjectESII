package br.unesp.fct.es2.admshopping.chamados;

/**
 * Status de um chamado (RF 3.1). No protótipo só o PENDENTE é usado;
 * as transições entram no incremento T13.
 */
public enum StatusChamado {
    PENDENTE,
    EM_ANDAMENTO,
    CONCLUIDO,
    CANCELADO
}

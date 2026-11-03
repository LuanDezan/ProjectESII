package br.unesp.fct.es2.admshopping.chamados;

/**
 * Foto anexada a um chamado (RF 3.0). Neste incremento guardamos só o nome e o tamanho.
 */
public record Foto(String nomeArquivo, long tamanhoBytes) {
}

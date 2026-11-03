package br.unesp.fct.es2.admshopping.notificacao;

import java.time.LocalDateTime;

/**
 * Mensagem mostrada a um usuário dentro do sistema (RF 6.0).
 */
public record Notificacao(Long idDestinatario, String mensagem, LocalDateTime dataHora) {
}

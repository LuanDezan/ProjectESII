package br.unesp.fct.es2.admshopping.chamados;

import br.unesp.fct.es2.admshopping.acesso.Usuario;
import java.time.LocalDateTime;

/**
 * Registro de uma mudança de status do chamado, para o histórico (RF 3.1).
 */
public record MudancaStatus(LocalDateTime dataHora, Usuario usuario,
                            StatusChamado anterior, StatusChamado novo) {

    @Override
    public String toString() {
        return dataHora.withNano(0) + " - " + usuario.getNome() + ": " + anterior + " -> " + novo;
    }
}

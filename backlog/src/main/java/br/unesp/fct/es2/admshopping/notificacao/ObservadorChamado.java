package br.unesp.fct.es2.admshopping.notificacao;

import br.unesp.fct.es2.admshopping.chamados.Chamado;
import br.unesp.fct.es2.admshopping.chamados.MudancaStatus;

/**
 * Observador do padrão Observer (Aula 03): é avisado pelo ServicoChamados
 * quando um chamado é direcionado ou muda de status (RF 6.0).
 */
public interface ObservadorChamado {

    void aoDirecionar(Chamado chamado);

    void aoMudarStatus(Chamado chamado, MudancaStatus mudanca);
}

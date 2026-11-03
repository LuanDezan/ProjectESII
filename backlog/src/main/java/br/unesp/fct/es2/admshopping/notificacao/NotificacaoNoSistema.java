package br.unesp.fct.es2.admshopping.notificacao;

import br.unesp.fct.es2.admshopping.acesso.Usuario;
import br.unesp.fct.es2.admshopping.chamados.Chamado;
import br.unesp.fct.es2.admshopping.chamados.MudancaStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Notificações dentro do sistema (RF 6.0, parcial): avisa o técnico quando recebe
 * um chamado e o solicitante a cada mudança de status. O envio por e-mail fica
 * para outro incremento (seria só mais um ObservadorChamado).
 */
public class NotificacaoNoSistema implements ObservadorChamado {

    private final Map<Long, List<Notificacao>> notificacoesPorUsuario = new HashMap<>();

    @Override
    public void aoDirecionar(Chamado chamado) {
        enviar(chamado.getTecnico(), "Chamado #" + chamado.getId() + " direcionado a você: "
                + chamado.getDescricao());
    }

    @Override
    public void aoMudarStatus(Chamado chamado, MudancaStatus mudanca) {
        enviar(chamado.getSolicitante(), "Seu chamado #" + chamado.getId() + " mudou de "
                + mudanca.anterior() + " para " + mudanca.novo());
    }

    public List<Notificacao> listarDe(Usuario usuario) {
        List<Notificacao> lista = notificacoesPorUsuario.get(usuario.getId());
        if (lista == null) {
            return List.of();
        }
        return List.copyOf(lista);
    }

    private void enviar(Usuario destinatario, String mensagem) {
        Notificacao notificacao = new Notificacao(destinatario.getId(), mensagem, LocalDateTime.now());
        notificacoesPorUsuario.computeIfAbsent(destinatario.getId(), id -> new ArrayList<>()).add(notificacao);
    }
}

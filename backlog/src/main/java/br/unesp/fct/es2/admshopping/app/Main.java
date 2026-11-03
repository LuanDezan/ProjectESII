package br.unesp.fct.es2.admshopping.app;

import br.unesp.fct.es2.admshopping.acesso.Perfil;
import br.unesp.fct.es2.admshopping.acesso.RepositorioUsuariosMemoria;
import br.unesp.fct.es2.admshopping.acesso.ServicoAcesso;
import br.unesp.fct.es2.admshopping.acesso.Usuario;
import br.unesp.fct.es2.admshopping.chamados.Chamado;
import br.unesp.fct.es2.admshopping.chamados.Foto;
import br.unesp.fct.es2.admshopping.chamados.Local;
import br.unesp.fct.es2.admshopping.chamados.MudancaStatus;
import br.unesp.fct.es2.admshopping.chamados.RepositorioChamadosMemoria;
import br.unesp.fct.es2.admshopping.chamados.ServicoChamados;
import br.unesp.fct.es2.admshopping.chamados.Urgencia;
import br.unesp.fct.es2.admshopping.notificacao.Notificacao;
import br.unesp.fct.es2.admshopping.notificacao.NotificacaoNoSistema;
import java.util.List;

/**
 * Roteiro de demonstração no console do Incremento 1:
 * cadastro, login, abertura, direcionamento e atendimento de um chamado.
 */
public class Main {

    public static void main(String[] args) {
        ServicoAcesso acesso = new ServicoAcesso(new RepositorioUsuariosMemoria());
        ServicoChamados chamados = new ServicoChamados(new RepositorioChamadosMemoria());
        NotificacaoNoSistema notificacoes = new NotificacaoNoSistema();
        chamados.adicionarObservador(notificacoes);

        System.out.println("== Cadastro de usuários (RF 5.1) ==");
        acesso.criarAdministradorInicial("Administração", "admin@shopping.com", "admin123");
        Usuario admin = acesso.autenticar("admin@shopping.com", "admin123");
        acesso.cadastrarUsuario(admin, "Loja Sabor & Cia", "lojista@sabor.com", "loja123",
                Perfil.LOJISTA, "L-102");
        acesso.cadastrarUsuario(admin, "Carlos Técnico", "carlos@shopping.com", "tec123",
                Perfil.MANUTENCAO, null);
        System.out.println("Usuários criados: admin, lojista e técnico");

        System.out.println("\n== Login (RF 5.0) ==");
        Usuario lojista = acesso.autenticar("lojista@sabor.com", "loja123");
        Usuario tecnico = acesso.autenticar("carlos@shopping.com", "tec123");
        System.out.println("Entraram: " + lojista + " e " + tecnico);

        System.out.println("\n== Abertura do chamado pelo lojista (RF 3.0) ==");
        Chamado chamado = chamados.abrirChamado(lojista, "Ar-condicionado pingando no balcão",
                Local.LOJA, "Loja L-102, próximo ao caixa", Urgencia.ALTA,
                List.of(new Foto("vazamento.jpg", 1_200_000)));
        System.out.println("Chamado #" + chamado.getId() + " aberto com status " + chamado.getStatus());

        System.out.println("\n== Direcionamento pelo administrador (RF 3.1) ==");
        chamados.direcionar(admin, chamado.getId(), tecnico);
        System.out.println("Chamado direcionado a " + chamado.getTecnico().getNome());

        System.out.println("\n== Atendimento pelo técnico (RF 3.2) ==");
        chamados.iniciarAtendimento(tecnico, chamado.getId());
        chamados.finalizar(tecnico, chamado.getId(), "Dreno desobstruído e bandeja limpa");
        System.out.println("Status final: " + chamado.getStatus());
        System.out.println("Serviço realizado: " + chamado.getServicoRealizado());

        System.out.println("\n== Histórico do chamado ==");
        for (MudancaStatus mudanca : chamado.getHistorico()) {
            System.out.println(mudanca);
        }

        System.out.println("\n== Notificações no sistema (RF 6.0) ==");
        imprimirNotificacoes(tecnico, notificacoes);
        imprimirNotificacoes(lojista, notificacoes);
    }

    private static void imprimirNotificacoes(Usuario usuario, NotificacaoNoSistema notificacoes) {
        for (Notificacao notificacao : notificacoes.listarDe(usuario)) {
            System.out.println("[" + usuario.getNome() + "] " + notificacao.mensagem());
        }
    }
}

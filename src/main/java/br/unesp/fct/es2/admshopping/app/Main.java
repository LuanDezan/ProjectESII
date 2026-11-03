package br.unesp.fct.es2.admshopping.app;

import br.unesp.fct.es2.admshopping.acesso.Perfil;
import br.unesp.fct.es2.admshopping.acesso.RepositorioUsuariosMemoria;
import br.unesp.fct.es2.admshopping.acesso.ServicoAcesso;
import br.unesp.fct.es2.admshopping.acesso.Usuario;
import br.unesp.fct.es2.admshopping.chamados.Chamado;
import br.unesp.fct.es2.admshopping.chamados.Foto;
import br.unesp.fct.es2.admshopping.chamados.Local;
import br.unesp.fct.es2.admshopping.chamados.RepositorioChamadosMemoria;
import br.unesp.fct.es2.admshopping.chamados.ServicoChamados;
import br.unesp.fct.es2.admshopping.chamados.Urgencia;
import java.util.List;

/**
 * Demonstração do protótipo (T06): login de um lojista e abertura de um chamado.
 */
public class Main {

    public static void main(String[] args) {
        ServicoAcesso acesso = new ServicoAcesso(new RepositorioUsuariosMemoria());
        ServicoChamados chamados = new ServicoChamados(new RepositorioChamadosMemoria());

        acesso.cadastrar("Loja Sabor & Cia", "lojista@sabor.com", "loja123", Perfil.LOJISTA, "L-102");

        Usuario lojista = acesso.autenticar("lojista@sabor.com", "loja123");
        System.out.println("Login (RF 5.0): " + lojista);

        Chamado chamado = chamados.abrirChamado(lojista, "Ar-condicionado pingando no balcão",
                Local.LOJA, "Loja L-102, próximo ao caixa", Urgencia.ALTA,
                List.of(new Foto("vazamento.jpg", 1_200_000)));
        System.out.println("Chamado #" + chamado.getId() + " aberto (RF 3.0): "
                + chamado.getDescricao() + " | urgência " + chamado.getUrgencia()
                + " | status " + chamado.getStatus());

        System.out.println("Chamados do lojista: " + chamados.listarDoSolicitante(lojista).size());
    }
}

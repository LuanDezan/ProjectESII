package br.unesp.fct.es2.admshopping.chamados;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.unesp.fct.es2.admshopping.acesso.Perfil;
import br.unesp.fct.es2.admshopping.acesso.RepositorioUsuariosMemoria;
import br.unesp.fct.es2.admshopping.acesso.ServicoAcesso;
import br.unesp.fct.es2.admshopping.acesso.Usuario;
import br.unesp.fct.es2.admshopping.comum.AcessoNegadoException;
import br.unesp.fct.es2.admshopping.comum.RegraNegocioException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ServicoChamadosTest {

    private static final long UM_MB = 1024 * 1024;

    private ServicoChamados servico;
    private Usuario lojista;
    private Usuario outroLojista;
    private Usuario admin;
    private Usuario tecnico;

    @BeforeEach
    void preparar() {
        ServicoAcesso acesso = new ServicoAcesso(new RepositorioUsuariosMemoria());
        lojista = acesso.cadastrar("Lojista", "lojista@loja.com", "123", Perfil.LOJISTA, "L-102");
        outroLojista = acesso.cadastrar("Outro", "outro@loja.com", "123", Perfil.LOJISTA, "L-200");
        admin = acesso.cadastrar("Admin", "admin@shopping.com", "123", Perfil.ADMINISTRADOR, null);
        tecnico = acesso.cadastrar("Técnico", "tec@shopping.com", "123", Perfil.MANUTENCAO, null);
        servico = new ServicoChamados(new RepositorioChamadosMemoria());
    }

    private Chamado abrir(Usuario solicitante, List<Foto> fotos) {
        return servico.abrirChamado(solicitante, "Lâmpada queimada", Local.LOJA, "Loja L-102",
                Urgencia.MEDIA, fotos);
    }

    private List<Foto> fotos(int quantidade, long tamanho) {
        List<Foto> lista = new ArrayList<>();
        for (int i = 1; i <= quantidade; i++) {
            lista.add(new Foto("foto" + i + ".jpg", tamanho));
        }
        return lista;
    }

    @Test
    @DisplayName("RF 3.0 - lojista abre chamado, que nasce PENDENTE e com id")
    void lojistaAbreChamado() {
        Chamado chamado = abrir(lojista, List.of());
        assertNotNull(chamado.getId());
        assertEquals(StatusChamado.PENDENTE, chamado.getStatus());
        assertEquals(Urgencia.MEDIA, chamado.getUrgencia());
    }

    @Test
    @DisplayName("RF 3.0 - administrador também abre chamado")
    void adminAbreChamado() {
        Chamado chamado = servico.abrirChamado(admin, "Vazamento no banheiro", Local.AREA_COMUM,
                "Banheiro do piso 2", Urgencia.ALTA, List.of());
        assertEquals(Local.AREA_COMUM, chamado.getLocal());
    }

    @Test
    @DisplayName("RF 3.0 - equipe de manutenção não abre chamado")
    void manutencaoNaoAbre() {
        assertThrows(AcessoNegadoException.class, () -> abrir(tecnico, List.of()));
    }

    @Test
    @DisplayName("RF 3.0 - descrição é obrigatória")
    void descricaoObrigatoria() {
        assertThrows(RegraNegocioException.class, () -> servico.abrirChamado(lojista, " ",
                Local.LOJA, "Loja L-102", Urgencia.BAIXA, List.of()));
    }

    @Test
    @DisplayName("RF 3.0 - local e detalhe do local são obrigatórios")
    void localObrigatorio() {
        assertThrows(RegraNegocioException.class, () -> servico.abrirChamado(lojista, "Porta quebrada",
                null, "Loja L-102", Urgencia.BAIXA, List.of()));
        assertThrows(RegraNegocioException.class, () -> servico.abrirChamado(lojista, "Porta quebrada",
                Local.LOJA, "", Urgencia.BAIXA, List.of()));
    }

    @Test
    @DisplayName("RF 3.0 - urgência é obrigatória")
    void urgenciaObrigatoria() {
        assertThrows(RegraNegocioException.class, () -> servico.abrirChamado(lojista, "Porta quebrada",
                Local.LOJA, "Loja L-102", null, List.of()));
    }

    @Test
    @DisplayName("RF 3.0 - aceita até 5 fotos de até 5 MB")
    void aceitaCincoFotos() {
        assertDoesNotThrow(() -> abrir(lojista, fotos(5, 5 * UM_MB)));
    }

    @Test
    @DisplayName("RF 3.0 - não permite mais de 5 fotos")
    void recusaSeisFotos() {
        assertThrows(RegraNegocioException.class, () -> abrir(lojista, fotos(6, UM_MB)));
    }

    @Test
    @DisplayName("RF 3.0 - não permite foto maior que 5 MB")
    void recusaFotoGrande() {
        assertThrows(RegraNegocioException.class, () -> abrir(lojista, fotos(1, 5 * UM_MB + 1)));
    }

    @Test
    @DisplayName("RF 3.0 - o lojista lista só os chamados que ele abriu")
    void listaSoDoSolicitante() {
        abrir(lojista, List.of());
        abrir(lojista, List.of());
        abrir(outroLojista, List.of());
        assertEquals(2, servico.listarDoSolicitante(lojista).size());
        assertEquals(1, servico.listarDoSolicitante(outroLojista).size());
    }
}

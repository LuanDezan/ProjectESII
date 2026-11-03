package br.unesp.fct.es2.admshopping.chamados;

import br.unesp.fct.es2.admshopping.acesso.Perfil;
import br.unesp.fct.es2.admshopping.acesso.Usuario;
import br.unesp.fct.es2.admshopping.comum.AcessoNegadoException;
import br.unesp.fct.es2.admshopping.comum.RegraNegocioException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abertura de chamados (RF 3.0) e consulta dos chamados do próprio solicitante.
 * Direcionamento, mudança de status e atendimento entram no incremento T13.
 */
public class ServicoChamados {

    public static final int MAXIMO_FOTOS = 5;
    public static final long TAMANHO_MAXIMO_FOTO = 5L * 1024 * 1024;

    private final RepositorioChamados repositorio;

    public ServicoChamados(RepositorioChamados repositorio) {
        this.repositorio = repositorio;
    }

    /** RF 3.0: lojista ou administrador abre um chamado. */
    public Chamado abrirChamado(Usuario solicitante, String descricao, Local local, String detalheLocal,
                                Urgencia urgencia, List<Foto> fotos) {
        if (solicitante.getPerfil() == Perfil.MANUTENCAO) {
            throw new AcessoNegadoException("A equipe de manutenção não abre chamados");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new RegraNegocioException("Descrição é obrigatória");
        }
        if (local == null || detalheLocal == null || detalheLocal.isBlank()) {
            throw new RegraNegocioException("Informe o local do problema");
        }
        if (urgencia == null) {
            throw new RegraNegocioException("Urgência é obrigatória");
        }
        if (fotos == null) {
            fotos = List.of();
        }
        validarFotos(fotos);
        Chamado chamado = new Chamado(solicitante, descricao, local, detalheLocal, urgencia, fotos);
        return repositorio.salvar(chamado);
    }

    /** Lista os chamados abertos pelo próprio solicitante. */
    public List<Chamado> listarDoSolicitante(Usuario solicitante) {
        List<Chamado> resultado = new ArrayList<>();
        for (Chamado chamado : repositorio.listarTodos()) {
            if (chamado.ehDoSolicitante(solicitante)) {
                resultado.add(chamado);
            }
        }
        return resultado;
    }

    private void validarFotos(List<Foto> fotos) {
        if (fotos.size() > MAXIMO_FOTOS) {
            throw new RegraNegocioException("São permitidas no máximo " + MAXIMO_FOTOS + " fotos");
        }
        for (Foto foto : fotos) {
            if (foto.tamanhoBytes() > TAMANHO_MAXIMO_FOTO) {
                throw new RegraNegocioException("A foto " + foto.nomeArquivo() + " passa de 5 MB");
            }
        }
    }
}

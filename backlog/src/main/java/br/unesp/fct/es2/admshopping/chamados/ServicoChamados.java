package br.unesp.fct.es2.admshopping.chamados;

import br.unesp.fct.es2.admshopping.acesso.Perfil;
import br.unesp.fct.es2.admshopping.acesso.Usuario;
import br.unesp.fct.es2.admshopping.comum.AcessoNegadoException;
import br.unesp.fct.es2.admshopping.comum.RegraNegocioException;
import br.unesp.fct.es2.admshopping.notificacao.ObservadorChamado;
import java.util.ArrayList;
import java.util.List;

/**
 * Regras de abertura, direcionamento e atendimento de chamados
 * (RF 3.0, RF 3.1, RF 3.2) e controle de acesso por perfil (RNF 3.0).
 * É o "sujeito" do padrão Observer: avisa os observadores (RF 6.0).
 */
public class ServicoChamados {

    public static final int MAXIMO_FOTOS = 5;
    public static final long TAMANHO_MAXIMO_FOTO = 5L * 1024 * 1024;

    private final RepositorioChamados repositorio;
    private final List<ObservadorChamado> observadores = new ArrayList<>();

    public ServicoChamados(RepositorioChamados repositorio) {
        this.repositorio = repositorio;
    }

    public void adicionarObservador(ObservadorChamado observador) {
        observadores.add(observador);
    }

    public void removerObservador(ObservadorChamado observador) {
        observadores.remove(observador);
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
        validarFotos(fotos);
        Chamado chamado = new Chamado(solicitante, descricao, local, detalheLocal, urgencia, fotos);
        return repositorio.salvar(chamado);
    }

    /** RF 3.1: o Administrador direciona o chamado a um técnico ativo. */
    public void direcionar(Usuario administrador, Long idChamado, Usuario tecnico) {
        exigirAdministrador(administrador);
        if (tecnico == null || tecnico.getPerfil() != Perfil.MANUTENCAO || !tecnico.isAtivo()) {
            throw new RegraNegocioException("O chamado deve ser direcionado a um técnico de manutenção ativo");
        }
        Chamado chamado = buscarExistente(idChamado);
        chamado.direcionarPara(tecnico);
        repositorio.salvar(chamado);
        for (ObservadorChamado observador : observadores) {
            observador.aoDirecionar(chamado);
        }
    }

    /** RF 3.2: o técnico responsável (ou o admin) inicia o atendimento. */
    public void iniciarAtendimento(Usuario usuario, Long idChamado) {
        Chamado chamado = buscarExistente(idChamado);
        exigirTecnicoResponsavelOuAdmin(usuario, chamado);
        MudancaStatus mudanca = chamado.mudarStatus(StatusChamado.EM_ANDAMENTO, usuario);
        salvarENotificar(chamado, mudanca);
    }

    /** RF 3.2: o técnico responsável (ou o admin) finaliza a ordem de serviço. */
    public void finalizar(Usuario usuario, Long idChamado, String servicoRealizado) {
        Chamado chamado = buscarExistente(idChamado);
        exigirTecnicoResponsavelOuAdmin(usuario, chamado);
        MudancaStatus mudanca = chamado.concluir(servicoRealizado, usuario);
        salvarENotificar(chamado, mudanca);
    }

    /** RF 3.1: só o Administrador cancela um chamado. */
    public void cancelar(Usuario administrador, Long idChamado) {
        exigirAdministrador(administrador);
        Chamado chamado = buscarExistente(idChamado);
        MudancaStatus mudanca = chamado.mudarStatus(StatusChamado.CANCELADO, administrador);
        salvarENotificar(chamado, mudanca);
    }

    /** RNF 3.0: cada perfil vê apenas os chamados que lhe cabem. */
    public List<Chamado> listarChamados(Usuario usuario) {
        List<Chamado> visiveis = new ArrayList<>();
        for (Chamado chamado : repositorio.listarTodos()) {
            if (podeVer(usuario, chamado)) {
                visiveis.add(chamado);
            }
        }
        return visiveis;
    }

    /** RF 3.1: consulta o detalhe e o status de um chamado, respeitando o perfil. */
    public Chamado consultar(Usuario usuario, Long idChamado) {
        Chamado chamado = buscarExistente(idChamado);
        if (!podeVer(usuario, chamado)) {
            throw new AcessoNegadoException("Você não tem acesso a este chamado");
        }
        return chamado;
    }

    private boolean podeVer(Usuario usuario, Chamado chamado) {
        switch (usuario.getPerfil()) {
            case ADMINISTRADOR:
                return true;
            case LOJISTA:
                return chamado.ehDoSolicitante(usuario);
            case MANUTENCAO:
                return chamado.ehDoTecnico(usuario);
            default:
                return false;
        }
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

    private void salvarENotificar(Chamado chamado, MudancaStatus mudanca) {
        repositorio.salvar(chamado);
        for (ObservadorChamado observador : observadores) {
            observador.aoMudarStatus(chamado, mudanca);
        }
    }

    private Chamado buscarExistente(Long idChamado) {
        Chamado chamado = repositorio.buscarPorId(idChamado);
        if (chamado == null) {
            throw new RegraNegocioException("Chamado não encontrado: " + idChamado);
        }
        return chamado;
    }

    private void exigirAdministrador(Usuario usuario) {
        if (!usuario.ehAdministrador()) {
            throw new AcessoNegadoException("Apenas o Administrador pode fazer esta operação");
        }
    }

    private void exigirTecnicoResponsavelOuAdmin(Usuario usuario, Chamado chamado) {
        if (!usuario.ehAdministrador() && !chamado.ehDoTecnico(usuario)) {
            throw new AcessoNegadoException("Apenas o técnico responsável pode atualizar este chamado");
        }
    }
}

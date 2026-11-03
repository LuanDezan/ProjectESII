package br.unesp.fct.es2.admshopping.chamados;

import br.unesp.fct.es2.admshopping.acesso.Usuario;
import br.unesp.fct.es2.admshopping.comum.RegraNegocioException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Chamado de manutenção (RF 3.0). Quando direcionado a um técnico, passa a ser
 * uma ordem de serviço (RF 3.1, RF 3.2). Controla as transições de status válidas.
 */
public class Chamado {

    private Long id;
    private final Usuario solicitante;
    private final String descricao;
    private final Local local;
    private final String detalheLocal;
    private final Urgencia urgencia;
    private final List<Foto> fotos;
    private final LocalDateTime dataAbertura;
    private StatusChamado status = StatusChamado.PENDENTE;
    private Usuario tecnico;
    private String servicoRealizado;
    private final List<MudancaStatus> historico = new ArrayList<>();

    public Chamado(Usuario solicitante, String descricao, Local local, String detalheLocal,
                   Urgencia urgencia, List<Foto> fotos) {
        this.solicitante = solicitante;
        this.descricao = descricao;
        this.local = local;
        this.detalheLocal = detalheLocal;
        this.urgencia = urgencia;
        this.fotos = new ArrayList<>(fotos);
        this.dataAbertura = LocalDateTime.now();
    }

    /** RF 3.1: direciona o chamado a um técnico de manutenção. */
    public void direcionarPara(Usuario tecnico) {
        if (status.ehFinal()) {
            throw new RegraNegocioException("Chamado " + status + " não pode ser direcionado");
        }
        this.tecnico = tecnico;
    }

    /** RF 3.1: muda o status, validando a transição, e registra no histórico. */
    public MudancaStatus mudarStatus(StatusChamado novo, Usuario usuario) {
        if (!podeMudarPara(novo)) {
            throw new RegraNegocioException("Transição inválida: " + status + " -> " + novo);
        }
        MudancaStatus mudanca = new MudancaStatus(LocalDateTime.now(), usuario, status, novo);
        historico.add(mudanca);
        status = novo;
        return mudanca;
    }

    /** RF 3.2: encerra a ordem de serviço registrando o serviço realizado. */
    public MudancaStatus concluir(String servicoRealizado, Usuario usuario) {
        if (servicoRealizado == null || servicoRealizado.isBlank()) {
            throw new RegraNegocioException("Informe o serviço realizado para concluir");
        }
        MudancaStatus mudanca = mudarStatus(StatusChamado.CONCLUIDO, usuario);
        this.servicoRealizado = servicoRealizado;
        return mudanca;
    }

    public boolean podeMudarPara(StatusChamado novo) {
        switch (status) {
            case PENDENTE:
                if (novo == StatusChamado.EM_ANDAMENTO) {
                    return tecnico != null;
                }
                return novo == StatusChamado.CANCELADO;
            case EM_ANDAMENTO:
                return novo == StatusChamado.CONCLUIDO || novo == StatusChamado.CANCELADO;
            default:
                return false;
        }
    }

    public boolean ehDoTecnico(Usuario usuario) {
        return tecnico != null && tecnico.getId().equals(usuario.getId());
    }

    public boolean ehDoSolicitante(Usuario usuario) {
        return solicitante.getId().equals(usuario.getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public String getDescricao() {
        return descricao;
    }

    public Local getLocal() {
        return local;
    }

    public String getDetalheLocal() {
        return detalheLocal;
    }

    public Urgencia getUrgencia() {
        return urgencia;
    }

    public List<Foto> getFotos() {
        return List.copyOf(fotos);
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public StatusChamado getStatus() {
        return status;
    }

    public Usuario getTecnico() {
        return tecnico;
    }

    public String getServicoRealizado() {
        return servicoRealizado;
    }

    public List<MudancaStatus> getHistorico() {
        return List.copyOf(historico);
    }
}

package br.unesp.fct.es2.admshopping.chamados;

import br.unesp.fct.es2.admshopping.acesso.Usuario;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Chamado de manutenção aberto por um lojista ou administrador (RF 3.0).
 * Todo chamado nasce com status PENDENTE.
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
}

package br.unesp.fct.es2.admshopping.chamados;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Guarda os chamados em memória (Incremento 1, sem banco de dados).
 */
public class RepositorioChamadosMemoria implements RepositorioChamados {

    private final Map<Long, Chamado> chamados = new LinkedHashMap<>();
    private long proximoId = 1;

    @Override
    public Chamado salvar(Chamado chamado) {
        if (chamado.getId() == null) {
            chamado.setId(proximoId++);
        }
        chamados.put(chamado.getId(), chamado);
        return chamado;
    }

    @Override
    public Chamado buscarPorId(Long id) {
        return chamados.get(id);
    }

    @Override
    public List<Chamado> listarTodos() {
        return new ArrayList<>(chamados.values());
    }
}

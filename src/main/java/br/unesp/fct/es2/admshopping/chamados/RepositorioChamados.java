package br.unesp.fct.es2.admshopping.chamados;

import java.util.List;

/**
 * Acesso aos dados de chamados. No próximo incremento ganha uma
 * implementação com JPA/Hibernate (Aula 03); os serviços não mudam.
 */
public interface RepositorioChamados {

    Chamado salvar(Chamado chamado);

    Chamado buscarPorId(Long id);

    List<Chamado> listarTodos();
}

package br.unesp.fct.es2.admshopping.acesso;

import java.util.List;

/**
 * Acesso aos dados de usuários. No próximo incremento ganha uma
 * implementação com JPA/Hibernate (Aula 03); os serviços não mudam.
 */
public interface RepositorioUsuarios {

    Usuario salvar(Usuario usuario);

    Usuario buscarPorId(Long id);

    Usuario buscarPorEmail(String email);

    List<Usuario> listarTodos();
}

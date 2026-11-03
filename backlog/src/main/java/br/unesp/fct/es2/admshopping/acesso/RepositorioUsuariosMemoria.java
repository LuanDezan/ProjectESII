package br.unesp.fct.es2.admshopping.acesso;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Guarda os usuários em memória (Incremento 1, sem banco de dados).
 */
public class RepositorioUsuariosMemoria implements RepositorioUsuarios {

    private final Map<Long, Usuario> usuarios = new LinkedHashMap<>();
    private long proximoId = 1;

    @Override
    public Usuario salvar(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(proximoId++);
        }
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarios.get(id);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios.values());
    }
}

package br.unesp.fct.es2.admshopping.acesso;

import br.unesp.fct.es2.admshopping.comum.AcessoNegadoException;
import br.unesp.fct.es2.admshopping.comum.RegraNegocioException;

/**
 * Autenticação e cadastro de usuários (RF 5.0 e RF 5.1).
 * Só o Administrador cadastra, edita e inativa usuários (Anexo A).
 */
public class ServicoAcesso {

    private final RepositorioUsuarios repositorio;

    public ServicoAcesso(RepositorioUsuarios repositorio) {
        this.repositorio = repositorio;
    }

    /** Cria o primeiro administrador do sistema (carga inicial). */
    public Usuario criarAdministradorInicial(String nome, String email, String senha) {
        if (!repositorio.listarTodos().isEmpty()) {
            throw new RegraNegocioException("O administrador inicial só pode ser criado com o sistema vazio");
        }
        return criar(nome, email, senha, Perfil.ADMINISTRADOR, null);
    }

    /** RF 5.0: autentica por e-mail e senha. */
    public Usuario autenticar(String email, String senha) {
        Usuario usuario = repositorio.buscarPorEmail(email);
        if (usuario == null || !GeradorHashSenha.verificar(senha, usuario.getHashSenha())) {
            throw new AcessoNegadoException("E-mail ou senha inválidos");
        }
        if (!usuario.isAtivo()) {
            throw new AcessoNegadoException("Usuário inativo");
        }
        return usuario;
    }

    /** RF 5.1: cadastra um usuário com perfil; o lojista é vinculado a uma loja. */
    public Usuario cadastrarUsuario(Usuario administrador, String nome, String email, String senha,
                                    Perfil perfil, String identificacaoLoja) {
        exigirAdministrador(administrador);
        return criar(nome, email, senha, perfil, identificacaoLoja);
    }

    /** RF 5.1: edita nome, e-mail e loja de um usuário. */
    public Usuario editarUsuario(Usuario administrador, Long id, String nome, String email,
                                 String identificacaoLoja) {
        exigirAdministrador(administrador);
        Usuario usuario = buscar(id);
        exigirTexto(nome, "Nome");
        exigirTexto(email, "E-mail");
        Usuario outro = repositorio.buscarPorEmail(email);
        if (outro != null && !outro.getId().equals(id)) {
            throw new RegraNegocioException("Já existe usuário com o e-mail " + email);
        }
        exigirLojaSeLojista(usuario.getPerfil(), identificacaoLoja);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setIdentificacaoLoja(identificacaoLoja);
        return repositorio.salvar(usuario);
    }

    /** RF 5.1: inativa um usuário (ele deixa de conseguir entrar). */
    public void inativarUsuario(Usuario administrador, Long id) {
        exigirAdministrador(administrador);
        Usuario usuario = buscar(id);
        usuario.inativar();
        repositorio.salvar(usuario);
    }

    public Usuario buscar(Long id) {
        Usuario usuario = repositorio.buscarPorId(id);
        if (usuario == null) {
            throw new RegraNegocioException("Usuário não encontrado: " + id);
        }
        return usuario;
    }

    private Usuario criar(String nome, String email, String senha, Perfil perfil, String identificacaoLoja) {
        exigirTexto(nome, "Nome");
        exigirTexto(email, "E-mail");
        exigirTexto(senha, "Senha");
        if (perfil == null) {
            throw new RegraNegocioException("Perfil é obrigatório");
        }
        if (repositorio.buscarPorEmail(email) != null) {
            throw new RegraNegocioException("Já existe usuário com o e-mail " + email);
        }
        exigirLojaSeLojista(perfil, identificacaoLoja);
        String hash = GeradorHashSenha.gerarHash(senha);
        return repositorio.salvar(new Usuario(nome, email, hash, perfil, identificacaoLoja));
    }

    private void exigirAdministrador(Usuario usuario) {
        if (usuario == null || !usuario.ehAdministrador()) {
            throw new AcessoNegadoException("Apenas o Administrador pode gerenciar usuários");
        }
    }

    private void exigirLojaSeLojista(Perfil perfil, String identificacaoLoja) {
        if (perfil == Perfil.LOJISTA && (identificacaoLoja == null || identificacaoLoja.isBlank())) {
            throw new RegraNegocioException("O lojista deve ser vinculado a uma loja");
        }
    }

    private void exigirTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new RegraNegocioException(campo + " é obrigatório");
        }
    }
}

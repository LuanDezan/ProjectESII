package br.unesp.fct.es2.admshopping.acesso;

import br.unesp.fct.es2.admshopping.comum.AcessoNegadoException;
import br.unesp.fct.es2.admshopping.comum.RegraNegocioException;

/**
 * Autenticação por e-mail e senha (RF 5.0).
 * O cadastro aqui é simples; as regras do Administrador (RF 5.1) entram no incremento T12.
 */
public class ServicoAcesso {

    private final RepositorioUsuarios repositorio;

    public ServicoAcesso(RepositorioUsuarios repositorio) {
        this.repositorio = repositorio;
    }

    /** Cadastro simples, usado pelos testes e pela demonstração. */
    public Usuario cadastrar(String nome, String email, String senha, Perfil perfil, String identificacaoLoja) {
        if (nome == null || nome.isBlank() || email == null || email.isBlank()
                || senha == null || senha.isBlank() || perfil == null) {
            throw new RegraNegocioException("Nome, e-mail, senha e perfil são obrigatórios");
        }
        if (repositorio.buscarPorEmail(email) != null) {
            throw new RegraNegocioException("Já existe usuário com o e-mail " + email);
        }
        String hash = GeradorHashSenha.gerarHash(senha);
        return repositorio.salvar(new Usuario(nome, email, hash, perfil, identificacaoLoja));
    }

    /** RF 5.0: autentica por e-mail e senha. Usuário inativo não entra. */
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
}

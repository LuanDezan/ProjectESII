package br.unesp.fct.es2.admshopping.acesso;

/**
 * Usuário do sistema com um dos três perfis (RF 5.0).
 * A senha nunca é guardada em texto, só o hash com salt (RNF 3.1).
 */
public class Usuario {

    private Long id;
    private final String nome;
    private final String email;
    private final String hashSenha;
    private final Perfil perfil;
    private final String identificacaoLoja;
    private boolean ativo = true;

    public Usuario(String nome, String email, String hashSenha, Perfil perfil, String identificacaoLoja) {
        this.nome = nome;
        this.email = email;
        this.hashSenha = hashSenha;
        this.perfil = perfil;
        this.identificacaoLoja = identificacaoLoja;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getHashSenha() {
        return hashSenha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public String getIdentificacaoLoja() {
        return identificacaoLoja;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void inativar() {
        this.ativo = false;
    }

    @Override
    public String toString() {
        return nome + " (" + perfil + ")";
    }
}

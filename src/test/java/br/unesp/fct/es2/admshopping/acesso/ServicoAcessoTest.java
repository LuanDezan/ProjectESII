package br.unesp.fct.es2.admshopping.acesso;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.unesp.fct.es2.admshopping.comum.AcessoNegadoException;
import br.unesp.fct.es2.admshopping.comum.RegraNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ServicoAcessoTest {

    private ServicoAcesso servico;

    @BeforeEach
    void preparar() {
        servico = new ServicoAcesso(new RepositorioUsuariosMemoria());
        servico.cadastrar("Lojista", "lojista@loja.com", "loja123", Perfil.LOJISTA, "L-102");
    }

    @Test
    @DisplayName("RF 5.0 - autentica com e-mail e senha corretos e devolve o perfil")
    void autenticaComDadosCorretos() {
        Usuario usuario = servico.autenticar("lojista@loja.com", "loja123");
        assertEquals(Perfil.LOJISTA, usuario.getPerfil());
    }

    @Test
    @DisplayName("RF 5.0 - recusa senha errada")
    void recusaSenhaErrada() {
        assertThrows(AcessoNegadoException.class, () -> servico.autenticar("lojista@loja.com", "errada"));
    }

    @Test
    @DisplayName("RF 5.0 - recusa e-mail não cadastrado")
    void recusaEmailDesconhecido() {
        assertThrows(AcessoNegadoException.class, () -> servico.autenticar("ninguem@loja.com", "loja123"));
    }

    @Test
    @DisplayName("RF 5.0 - usuário inativo não autentica")
    void inativoNaoAutentica() {
        servico.autenticar("lojista@loja.com", "loja123").inativar();
        assertThrows(AcessoNegadoException.class, () -> servico.autenticar("lojista@loja.com", "loja123"));
    }

    @Test
    @DisplayName("RNF 3.1 - a senha não é guardada em texto")
    void senhaNaoFicaEmTexto() {
        Usuario usuario = servico.autenticar("lojista@loja.com", "loja123");
        assertNotEquals("loja123", usuario.getHashSenha());
    }

    @Test
    @DisplayName("Cadastro - não permite dois usuários com o mesmo e-mail")
    void emailUnico() {
        assertThrows(RegraNegocioException.class,
                () -> servico.cadastrar("Outro", "lojista@loja.com", "x", Perfil.LOJISTA, "L-200"));
    }
}

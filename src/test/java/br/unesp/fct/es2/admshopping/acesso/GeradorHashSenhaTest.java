package br.unesp.fct.es2.admshopping.acesso;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GeradorHashSenhaTest {

    @Test
    @DisplayName("RNF 3.1 - o hash não contém a senha em texto")
    void hashNaoContemSenha() {
        String hash = GeradorHashSenha.gerarHash("segredo123");
        assertFalse(hash.contains("segredo123"));
    }

    @Test
    @DisplayName("RNF 3.1 - a mesma senha gera hashes diferentes por causa do salt")
    void saltGeraHashesDiferentes() {
        assertNotEquals(GeradorHashSenha.gerarHash("segredo123"), GeradorHashSenha.gerarHash("segredo123"));
    }

    @Test
    @DisplayName("RNF 3.1 - verifica a senha correta e rejeita a errada")
    void verificaSenha() {
        String hash = GeradorHashSenha.gerarHash("segredo123");
        assertTrue(GeradorHashSenha.verificar("segredo123", hash));
        assertFalse(GeradorHashSenha.verificar("outraSenha", hash));
    }
}

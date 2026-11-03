package br.unesp.fct.es2.admshopping.acesso;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Gera e verifica o hash das senhas com salt aleatório (RNF 3.1).
 * O valor guardado tem o formato "salt:hash", ambos em Base64.
 */
public class GeradorHashSenha {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int ITERACOES = 65536;
    private static final int TAMANHO_CHAVE = 256;
    private static final int TAMANHO_SALT = 16;

    public static String gerarHash(String senha) {
        byte[] salt = new byte[TAMANHO_SALT];
        new SecureRandom().nextBytes(salt);
        byte[] hash = calcular(senha, salt);
        Base64.Encoder base64 = Base64.getEncoder();
        return base64.encodeToString(salt) + ":" + base64.encodeToString(hash);
    }

    public static boolean verificar(String senha, String hashArmazenado) {
        String[] partes = hashArmazenado.split(":");
        byte[] salt = Base64.getDecoder().decode(partes[0]);
        String hashCalculado = Base64.getEncoder().encodeToString(calcular(senha, salt));
        return hashCalculado.equals(partes[1]);
    }

    private static byte[] calcular(String senha, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, ITERACOES, TAMANHO_CHAVE);
            SecretKeyFactory fabrica = SecretKeyFactory.getInstance(ALGORITMO);
            return fabrica.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao calcular o hash da senha", e);
        }
    }
}

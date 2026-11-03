# Código de referência para T12, T13 e T14 (não compilado)

Esta pasta guarda uma versão adiantada do código do Incremento 1, escrita antes de o escopo da
Entrega 1 ser reduzido ao protótipo (T06). Ela fica fora de `src/`, então o Maven não a compila
e o build da equipe continua passando.

O que tem aqui e ainda não está em `src/`:
- `ServicoAcesso` com as regras do RF 5.1 (só o Administrador cadastra, edita e inativa usuários);
- `ServicoChamados` com direcionamento ao técnico, transições de status, histórico (`MudancaStatus`)
  e visibilidade por perfil (RF 3.1, RF 3.2, RNF 3.0);
- pacote `notificacao` com o padrão Observer (RF 6.0 parcial).

Atenção:
- **Não tem testes.** Os testes de `src/test` foram escritos para o protótipo e não compilam contra
  esta versão, porque a assinatura de `cadastrar` mudou e a listagem passou a ser por perfil.
- É ponto de partida, não entrega: quem pegar a T12, T13 ou T14 decide o que aproveitar, adapta,
  escreve os testes e leva para `src/` pelo branch da tarefa.

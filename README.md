# ADM Shopping — Protótipo inicial (tarefa T06)

Sistema de Gestão Integrada para Shopping Centers, disciplina Engenharia de Software II (UNESP/FCT, Prof. Dr. Rogério Eduardo Garcia).

Este é o **protótipo inicial da tarefa T06 do cronograma, Entrega 1 (26/10/2026)**. Ele implementa uma fatia pequena do Documento de Requisitos v1.1 para validar a estrutura do projeto, os pacotes por módulo e o uso de testes automatizados. Os demais requisitos do recorte do semestre entram nos próximos incrementos.

Equipe de codificação: Vinícius Mazzaro Vantini dos Santos e Matheus Henrique Trindade Lopes.

## O que foi feito

| Requisito | O que o protótipo faz |
|---|---|
| RF 5.0 – Autenticação | Login por e-mail e senha com os perfis ADMINISTRADOR, LOJISTA e MANUTENCAO. Usuário inativo não autentica. O cadastro é um método simples, ainda sem as regras do Administrador (RF 5.1). |
| RNF 3.1 – Senhas | A senha é guardada só como hash com salt aleatório (PBKDF2WithHmacSHA256), nunca em texto. |
| RF 3.0 – Abrir chamado | Lojista ou administrador abre um chamado com descrição, local (área comum, loja ou outro, mais o detalhe), urgência (baixa, média ou alta) e até 5 fotos de até 5 MB cada. A equipe de manutenção não abre chamados. O chamado nasce com status PENDENTE. O solicitante pode listar os chamados que abriu. |

Ainda não há banco de dados nem interface web: os dados ficam em memória (repositórios `...Memoria`) e a demonstração roda no console.

## Próximos incrementos

| Tarefa | Conteúdo |
|---|---|
| T12 – Acesso completo | RF 5.1: o Administrador cadastra, edita e inativa usuários; o lojista fica vinculado a uma loja; e-mail único. |
| T13 – Chamados | RF 3.1 e RF 3.2: direcionamento ao técnico, transições de status (Pendente, Em Andamento, Concluído, Cancelado), histórico, atendimento e encerramento da ordem de serviço, visibilidade por perfil (RNF 3.0). |
| T14 – Notificações | RF 6.0: notificações no sistema usando o padrão Observer (Aula 03). |
| T15 – Interface web | Telas responsivas e persistência com JPA/Hibernate no lugar dos repositórios em memória. |

## Como compilar, testar e rodar

Requisitos: Java 21 e Maven 3.6.3 ou superior (no Ubuntu: `sudo apt install openjdk-21-jdk maven`).

```bash
mvn test
```

```bash
mvn compile
java -cp target/classes br.unesp.fct.es2.admshopping.app.Main
```

No WSL da equipe, onde o JDK e o Maven foram instalados no usuário, sem estar no PATH:

```bash
export JAVA_HOME=$HOME/.local/opt/jdk21 PATH=$HOME/.local/opt/jdk21/bin:$HOME/.local/opt/maven/bin:$PATH
```

Depois disso, os comandos acima funcionam normalmente.

## Estrutura de pacotes

Os pacotes seguem os módulos do RNF 4.0:

```
br.unesp.fct.es2.admshopping
├── acesso     Usuario, Perfil, GeradorHashSenha, RepositorioUsuarios(+Memoria), ServicoAcesso
├── chamados   Chamado, StatusChamado, Urgencia, Local, Foto, RepositorioChamados(+Memoria), ServicoChamados
├── comum      RegraNegocioException, AcessoNegadoException
└── app        Main (demonstração no console)
```

Os serviços dependem só das interfaces dos repositórios. Assim, trocar a implementação em memória por JPA/Hibernate (T15) não muda as regras de negócio.

## Rastreabilidade: requisito → classes → testes

| Requisito | Classes | Testes |
|---|---|---|
| RF 5.0 | `ServicoAcesso`, `Usuario`, `Perfil`, `RepositorioUsuarios`, `RepositorioUsuariosMemoria` | `ServicoAcessoTest`: autenticaComDadosCorretos, recusaSenhaErrada, recusaEmailDesconhecido, inativoNaoAutentica, emailUnico |
| RNF 3.1 | `GeradorHashSenha`, `ServicoAcesso` | `GeradorHashSenhaTest`: hashNaoContemSenha, saltGeraHashesDiferentes, verificaSenha; `ServicoAcessoTest`: senhaNaoFicaEmTexto |
| RF 3.0 | `ServicoChamados`, `Chamado`, `StatusChamado`, `Urgencia`, `Local`, `Foto`, `RepositorioChamados`, `RepositorioChamadosMemoria` | `ServicoChamadosTest`: lojistaAbreChamado, adminAbreChamado, manutencaoNaoAbre, descricaoObrigatoria, localObrigatorio, urgenciaObrigatoria, aceitaCincoFotos, recusaSeisFotos, recusaFotoGrande, listaSoDoSolicitante |

Resultado atual: 19 testes, 0 falhas.

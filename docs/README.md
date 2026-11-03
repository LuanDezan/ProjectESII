# Documentação do projeto ADM Shopping

Engenharia de Software II — Prof. Dr. Rogério Eduardo Garcia

| Papel | Integrantes |
|---|---|
| Gerência | Matheus Augustho de Moura Nazaro e Luan Yamada Dezan |
| Análise e Projeto | Caio Silva Nestlehner e Ana Clara Plaszezeski Souza da Silveira |
| Codificação | Vinícius Mazzaro Vantini dos Santos e Matheus Henrique Trindade Lopes |
| SQA | Alexandre Vinícius Malavazi e Ryan Victor Rocha Marson |

## gerencia/
- `Plano_de_Projeto_ADM_Shopping.xlsx` — métricas, estimativas, rede de tarefas (PERT/CPM), Gantt, controle e riscos.
  Comece pela aba **Resumo**. A aba **Controle** é atualizada toda semana pela gerência.
- `Gantt_ADM_Shopping.png` — imagem do Gantt da Entrega 1.

## requisitos/
1. `1_Documento_de_Requisitos_v1.0.pdf` — versão original (inspecionada).
2. `2_Relatorio_de_Defeitos.docx` — 33 defeitos encontrados pelo SQA (classes O/FI/I/A/IE).
3. `3_Documento_de_Requisitos_v1.1_Corrigido.docx` — versão corrigida, com rastreabilidade defeito → correção.

O código-fonte (Maven, Java 21) fica na raiz do repositório. Veja o `README.md` principal.

## Como trabalhamos no repositório
- `main`: só o que já foi entregue ou revisado pelo SQA.
- Cada tarefa do cronograma é feita num branch com o ID da tarefa (ex.: `T12-acesso`) e entra na `main` por pull request revisado.
- Mensagens de commit começam com o ID da tarefa (ex.: `T12: cadastro de usuários pelo administrador`), para ligar o histórico ao cronograma.

# 🐛 BuggerFixer — Correção Inteligente de Bugs com IA

O **BuggerFixer** é um sistema que utiliza inteligência artificial para analisar, sugerir e aplicar correções em métodos Java de projetos hospedados no GitHub. Nosso objetivo é acelerar a correção de bugs com o mínimo de esforço humano, mantendo a qualidade e consistência do código.

---

## 🚀 Funcionalidades

- 🤖 Correção automatizada de métodos Java com base no comportamento esperado.
- 🔍 Localização precisa de métodos dentro de projetos.
- 🧠 Sugestões de fix com OpenAI (GPT-4o).
- 🛠️ Substituição segura do método com formatação automática.
- 📦 Geração de branch, commit e push automático no repositório clonado.
- 🔁 Criação de Pull Request automaticamente no GitHub.
- 🧾 Detecção de imports faltantes após as mudanças.
- 🛡️ Suporte para múltiplos clientes com autenticação via token.

---

## 🧩 Como Funciona

1. O cliente envia:
   - A URL do repositório no GitHub
   - O nome do método (ex: `ProductService.update`)
   - A descrição do comportamento esperado

2. O BuggerFixer:
   - Clona o repositório temporariamente
   - Encontra o método com base no nome completo da classe e método
   - Usa a IA para sugerir uma correção
   - Substitui o método original pelo corrigido
   - Formata o código automaticamente
   - Verifica e sugere imports ou dependências faltantes
   - Cria uma nova branch e faz o push
   - Abre um Pull Request com a sugestão de correção

---

## 🔧 Tecnologias Utilizadas

- Java 17
- Spring Boot
- OpenAI GPT-4o (via API)
- JGit
- GitHub REST API
- Maven
- JWT (para autenticação)

---

## 🔐 Autenticação

Cada cliente usa um token de acesso para consumir a API. Isso permite controle de uso e segurança no acesso às funcionalidades.

---

## 📥 Exemplo de Requisição

```json
POST /api/fix
Content-Type: application/json
Authorization: Bearer {token}

{
  "githubUrl": "https://github.com/user/project.git",
  "methodName": "ProductService.update",
  "expectedBehavior": "O método deve validar o ID antes de atualizar e lançar exceção se não encontrar o produto."
}

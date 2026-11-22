# Budgeting — API inteligente de gastos por voz

Projeto final do bootcamp **Itaú — Java com Inteligência Artificial**, seguindo a arquitetura do laboratório da DIO ministrado por Thiago Poiani.

A API recebe um áudio como “gastei 80 reais no supermercado”, transcreve a fala com Whisper, interpreta a intenção com o `ChatClient`, chama casos de uso Java por **Tool Calling**, persiste o gasto e devolve a confirmação em MP3.

## Fluxo

```text
Áudio → Whisper (fala para texto) → ChatClient → @Tool/caso de uso
                                                   ↓
Resposta MP3 ← Text-to-Speech ← resposta textual ← MySQL/H2
```

## Tecnologias

- Java 25, Gradle, Spring Boot 4
- Spring AI 2 e OpenAI (chat, Whisper e Text-to-Speech)
- Spring Data JPA, MySQL 9 e H2
- Docker Compose, Bean Validation, JUnit 5 e MockMvc
- Arquitetura em camadas: domínio, aplicação e infraestrutura

Os valores são armazenados em **centavos** no domínio. Assim, `8050` representa `R$ 80,50`, evitando erros de ponto flutuante.

## Executar sem IA

O perfil padrão usa H2 local e não exige chave externa:

```bash
./gradlew bootRun
```

Exemplo:

```bash
curl -X POST http://localhost:8080/transactions \
  -H 'Content-Type: application/json' \
  -d '{"description":"Supermercado","category":"GROCERIES","amount":8050}'

curl http://localhost:8080/transactions/GROCERIES
curl http://localhost:8080/transactions/GROCERIES/total
```

Categorias disponíveis: `GROCERIES`, `PHARMA` e `AUTO`.

## Executar o fluxo completo por voz

Defina a chave da OpenAI e ative o perfil `openai`:

```bash
export OPENAI_API_KEY='sua-chave'
SPRING_PROFILES_ACTIVE=openai ./gradlew bootRun
```

Envie um áudio e salve a resposta:

```bash
curl -X POST http://localhost:8080/transactions/ai \
  -F 'file=@meu-gasto.m4a' \
  --output resposta.mp3
```

Para usar MySQL, suba `docker compose up -d` e execute com os perfis `openai,mysql`.

## Endpoints

| Método | Endpoint | Função |
|---|---|---|
| `POST` | `/transactions` | Registra gasto via JSON |
| `GET` | `/transactions/{category}` | Lista gastos por categoria |
| `GET` | `/transactions/{category}/total` | Soma gastos da categoria |
| `POST` | `/transactions/ai` | Processa áudio e retorna MP3 (perfil `openai`) |

## Testes

```bash
./gradlew test
```

Os testes usam H2 em memória e não chamam a OpenAI, portanto não consomem créditos.

## Estrutura

```text
src/main/java/dio/budgeting
├── domain          # modelo e contrato do repositório, sem dependência de framework
├── application     # casos de uso também expostos como ferramentas da IA
└── infrastructure  # REST, Spring AI e persistência JPA
```

Nunca publique sua chave. O arquivo `.env` está ignorado pelo Git; use `.env.example` apenas como referência.

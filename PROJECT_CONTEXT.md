# PROJECT_CONTEXT — OrçaFácil

> Documento de conhecimento permanente do Project OrçaFácil.
> **Commit analisado:** `f591bbd` (branch `main`, 04/11/2026 18:35 UTC) — análise feita a 10/11/2026.
> Se o repositório tiver avançado desde esse commit, este documento pode estar desatualizado: ver a secção 10.

## Legenda de estados

Funcionalidades e requisitos:

- **[CONCLUÍDO]** — existe no código do commit `f591bbd` e foi lido na análise.
- **[PLANEADO]** — previsto (README ou plano), mas **não existe** no código.
- **[NÃO VERIFICADO]** — não foi possível confirmar (ver secção 7).

Problemas técnicos:

- **[CONFIRMADO]** — observável diretamente no código ou nos recursos.
- **[INFERIDO]** — deduzido do código, mas o efeito em execução não foi observado.

---

## 1. Objetivo e âmbito

- Aplicação Android para **estimar o custo de um serviço**, desenvolvida em Kotlin com layouts XML, no Android Studio.
- O README descreve-a como "projeto em desenvolvimento, projeto académico para a UC00609".
- Funcionalidades previstas no README (nenhuma está completa — ver secção 5):
  - Introduzir descrição do serviço, horas estimadas, preço por hora e despesas adicionais.
  - Calcular e mostrar um orçamento itemizado.
  - Editar os valores introduzidos ou iniciar um novo orçamento.
  - Navegar por três Activities, com splash screen no arranque.
- **Fora do âmbito confirmado:** a **persistência de dados não faz parte dos requisitos atualmente confirmados**. O README não a prevê e o projeto não tem dependências para isso. Só deve ser considerada se o utilizador a introduzir como requisito.
- Idioma da interface: português (textos em `strings.xml`). O README está em inglês.

## 2. Arquitetura e tecnologias

| Item | Valor |
|---|---|
| Módulos | Um único módulo `:app` |
| Pacote / applicationId | `com.example.orcafacil` (valor por omissão do template) |
| Linguagem | Kotlin |
| UI | Layouts XML + ViewBinding (`viewBinding = true`) |
| Android Gradle Plugin | 9.5.0 |
| Gradle (wrapper) | 9.6.0 |
| compileSdk / targetSdk / minSdk | 37 / 37 / 23 |
| Java (source/target) | 11 |
| Tema | `Theme.Material3.DayNight.NoActionBar` (`Theme.OrcaFacil`) |
| Dependências | `androidx.activity:activity-ktx`, `appcompat`, `constraintlayout`, `core-ktx`, `material`; `junit` (teste), `espresso-core` e `androidx.test.ext:junit` (androidTest) |
| Gestão de versões | `gradle/libs.versions.toml` (version catalog) |

- **Padrão arquitetural:** simples, sem camadas. Cada Activity trata da própria UI e da lógica. Não há ViewModel, repositório, Fragments nem Navigation Component.
- **Edge-to-edge:** ambas as Activities chamam `enableEdgeToEdge()` e aplicam os insets das barras do sistema como padding à raiz do layout.
- **Não existem dependências** para Room, DataStore, SharedPreferences nem para a API de splash screen (`core-splashscreen`).
- **Convenção de commits no histórico:** `feat:`, `fix:`, `refactor:`, `build:`, `chore:`.

## 3. Activities e layouts efetivamente existentes

Existem **duas** Activities, ambas declaradas no `AndroidManifest.xml`.

| Activity | Layout | Estado | Responsabilidade |
|---|---|---|---|
| `MainActivity` (launcher, `exported="true"`) | `activity_main.xml` | [CONCLUÍDO] | Ecrã inicial. O botão `btnCriarOrcamento` abre a `OrcamentoActivity` por `Intent` explícito. |
| `OrcamentoActivity` (`exported="false"`) | `activity_orcamento.xml` | [CONCLUÍDO] (só formulário e validação) | Formulário do orçamento. O botão `btnCalcular` só valida (ver secção 4). |

**`activity_main.xml`:** `LinearLayout` raiz (`@+id/main`, centrado), com um `LinearLayout` interno (padding 24dp) que contém `tvTitulo`, `tvDescricao` e `btnCriarOrcamento`.

**`activity_orcamento.xml`:** `ScrollView` raiz (`@+id/main`, `fillViewport`) com:

| Campo | id | inputType |
|---|---|---|
| Descrição do serviço | `etDescricao` | `textCapSentences\|textMultiLine` (2 a 4 linhas) |
| Horas previstas | `etHoras` | `numberDecimal` |
| Preço por hora (€) | `etPrecoHora` | `numberDecimal` |
| Despesas adicionais (€) | `etDespesas` | `numberDecimal` |

O botão `btnCalcular` fecha o formulário. Cada campo tem um `TextView` de rótulo com `labelFor`.

**Navegação:** apenas `MainActivity` → `OrcamentoActivity`. Não existe ecrã de resultado nem navegação de retorno com dados.

**Não existem:** Splash, terceira Activity, ecrã de resultado.

## 4. Funcionalidades implementadas e validações

### Implementado [CONCLUÍDO]

- Ecrã inicial com navegação para o formulário.
- Formulário de orçamento com quatro campos.
- Validação de campos em `OrcamentoActivity.validarFormulario()`.

### Cálculo do orçamento — **NÃO IMPLEMENTADO**

- O botão "Calcular" apenas chama `validarFormulario()`. O `Boolean` devolvido é ignorado.
- Não há cálculo nem apresentação de nenhum valor.
- A fórmula esperada (horas × preço por hora + despesas) é uma **hipótese** deduzida do README e do formulário. Não está confirmada como requisito nem existe no código.

### Validações existentes [CONCLUÍDO]

A validação é sequencial e **pára no primeiro erro**. Em cada erro faz `setError(...)` com texto de `strings.xml` e `requestFocus()` no campo.

| Campo | Regra no código | Observação |
|---|---|---|
| Descrição | Obrigatória (após `trim()`) | Erro limpo após validar |
| Horas | Número `> 0`, finito; vírgula convertida em ponto | Erro limpo após validar |
| Preço/hora | Número `> 0`, finito; vírgula convertida em ponto | Erro limpo após validar |
| Despesas | Número `> 0` (ver problema P2), finito; vírgula convertida em ponto | **Erro não é limpo** após validar (ver P3) |

### Tratamento de erros

- Só existe o tratamento por validação de campos (`toDoubleOrNull()` + `isFinite()`).
- Não existem `try/catch`, limites máximos, nem gestão de estado em rotação (a análise não verificou o comportamento).

## 5. Funcionalidades planeadas, mas não implementadas

| Funcionalidade | Estado | Notas |
|---|---|---|
| Cálculo do orçamento | [PLANEADO] | Prevista no README. Não implementada. |
| Orçamento itemizado (apresentação) | [PLANEADO] | Prevista no README. |
| **Terceira Activity** (presumivelmente ecrã de resultado) | [PLANEADO] | Confirmado pelo utilizador: **apenas planeada**, nunca implementada, e não existe em trabalho local por enviar. |
| **Splash screen** | [PLANEADO] | Confirmado pelo utilizador: **apenas planeada**, nunca implementada, e não existe em trabalho local por enviar. |
| Editar valores / novo orçamento | [PLANEADO] | Prevista no README. |
| Testes reais | [PLANEADO] | Só existem os testes de exemplo (secção 7). |
| Persistência de dados | **Não é requisito confirmado** | Ver secção 1. |

**Questão em aberto:** o README diz "três Activities, com um splash screen". Não está definido se a Splash conta como uma das três ou é adicional (e se seria uma Activity ou a API `SplashScreen`). Decidir com o utilizador antes de implementar.

## 6. Problemas técnicos identificados

Referências de linha são ao commit `f591bbd`.

### Erros confirmados no código

- **P1 — Ordem do ciclo de vida em `MainActivity`** (`MainActivity.kt:13-20`) [CONFIRMADO a ordem; efeito INFERIDO]. O `binding` é `lazy` e o `setOnClickListener` (linha 16) corre **antes** de `super.onCreate()` (linha 20), forçando o inflate do layout antes de o AppCompat estar instalado. A `OrcamentoActivity` segue a ordem correta. O efeito provável é os widgets não receberem o estilo Material. Não foi observado em dispositivo.
- **P2 — Validação das despesas contradiz os textos** (`OrcamentoActivity.kt:62`) [CONFIRMADO]. O código rejeita `<= 0`, mas `strings.xml` diz "iguais ou superiores a zero" (`erro_despesas_invalidas`) e o hint diz "0 se não existirem" (`hint_despesas`). Hoje não é possível submeter 0.
- **P3 — Erro das despesas nunca é limpo** (`OrcamentoActivity.kt`, antes do `return true`) [CONFIRMADO que falta o `error = null`]. Os outros campos fazem-no. Não foi verificado se o framework limpa o erro quando o texto é editado.
- **P4 — Margem no campo das despesas** (`activity_orcamento.xml:84`) [CONFIRMADO no XML; efeito visual INFERIDO]. Tem `layout_marginTop="48dp"` onde os outros campos têm `minHeight="48dp"`. Isto cria um espaço grande entre o rótulo e o campo.
- **P5 — Testes são só exemplos** [CONFIRMADO] (secção 7).
- **P6 — Validação acoplada às views** [CONFIRMADO]. A lógica vive dentro da Activity, o que dificulta testá-la sem emulador.
- **P7 — Menores** [CONFIRMADO]: `applicationId` `com.example.*`; `;` a mais em `viewBinding = true;` no `app/build.gradle.kts` (inofensivo); hints com "Ex.:" e "Ex:" misturados; o valor devolvido por `validarFormulario()` não é usado.

### Problemas inferidos / riscos futuros [INFERIDO]

- Sem limites máximos, valores muito grandes podem dar `Infinity` quando o cálculo for implementado.
- Usar `Double` para dinheiro pode dar erros de arredondamento. Considerar `BigDecimal` quando o cálculo for implementado (decisão de desenho, não um erro).
- Valores em formato com separador de milhares (ex.: "1.234,56") serão rejeitados como inválidos.

## 7. Estado dos testes e limitações da análise

### Testes

- `app/src/test/.../ExampleUnitTest.kt` — teste de exemplo do template (`2 + 2`). Não testa o projeto.
- `app/src/androidTest/.../ExampleInstrumentedTest.kt` — teste de exemplo do template (verifica o `packageName`). Não testa o projeto.
- **Não existem testes da validação, do cálculo nem da navegação.**

### Limitações da análise [NÃO VERIFICADO]

- O projeto **não foi compilado nem executado**. Nada foi observado em emulador ou dispositivo.
- As versões de AGP, Gradle e SDK 37 foram lidas dos ficheiros, não validadas por uma compilação.
- Só existe a branch `main` no repositório remoto. Trabalho local não enviado para o GitHub não é visível. O utilizador confirmou que Splash e terceira Activity não existem localmente.
- Não foram lidos em detalhe: `gradle.properties`, `colors.xml`, `values-night/themes.xml`, `backup_rules.xml`, `data_extraction_rules.xml` e os ícones (ficheiros gerados pelo template).
- Issues e pull requests do GitHub não foram consultados.
- A análise foi feita por clone do repositório público, porque o conetor GitHub não estava disponível na primeira sessão.

## 8. Próximas tarefas, por ordem de prioridade

Cada tarefa deve ser **um passo pequeno**, com teste rápido no emulador/dispositivo e um commit próprio.

1. **Corrigir a ordem em `MainActivity.onCreate`** (P1): registar o listener depois de `super.onCreate()` e `setContentView()`. Commit `fix:`.
2. **Corrigir a validação das despesas** (P2 e P3): alinhar a condição com a mensagem, decidir se campo vazio conta como 0 e limpar o erro. Commit `fix:`.
3. **Corrigir o layout das despesas** (P4). Commit `fix:`.
4. **Implementar o cálculo:** isolar a lógica numa função testável (fora da Activity); decidir `Double` vs `BigDecimal`; confirmar a fórmula com o utilizador. Commit `feat:`.
5. **Criar o ecrã de resultado** (terceira Activity): dados por `Intent` extras, orçamento itemizado, botões "Editar" e "Novo orçamento". Commit `feat:`.
6. **Escrever testes unitários reais** da validação e do cálculo e remover os exemplos. Commit `test:`.
7. **Splash screen**, só se for requisito da UC00609; resolver primeiro a questão em aberto da secção 5. Commit `feat:`.
8. **Atualizar o README** com o estado real e instruções de build. Commit `docs:`.

## 9. Repositório e último commit analisado

- **Repositório:** https://github.com/alvaroteixeiramonteiro-dev/OrcaFacil (público)
- **Branch:** `main` (única)
- **Último commit analisado:** `f591bbd` — `feat: validate budget form inputs` — 04/11/2026 18:35 UTC
- **Total de commits à data da análise:** 10

| Commit | Data (UTC) | Mensagem |
|---|---|---|
| `f591bbd` | 04/11 18:35 | feat: validate budget form inputs |
| `d9108d8` | 04/11 16:40 | refactor: use ViewBinding in budget activity |
| `4ebdd15` | 04/11 15:48 | fix: correct budget input fields |
| `ab6b124` | 04/11 15:44 | feat: add budget form layout |
| `227ce65` | 02/11 12:16 | feat: add budget activity and home navigation |
| `b8d7280` | 02/11 12:09 | feat: add home screen layout |
| `0de7db5` | 02/11 11:52 | refactor: use ViewBinding in MainActivity |
| `d92d95e` | 02/11 11:46 | build: enable ViewBinding |
| `5364103` | 02/11 11:42 | chore: initialize Android project with README |
| `518622d` | 02/11 11:37 | Initial commit |

## 10. Como retomar o desenvolvimento numa futura conversa

### Primeiro passo: confirmar o estado real

1. Obter o código atual. Usar o conetor GitHub se estiver ativo na conversa; caso contrário, o repositório é público e pode ser clonado diretamente.
2. Comparar o `HEAD` com o commit analisado: `git log f591bbd..HEAD`. Se houver commits novos, ler as alterações e atualizar este documento antes de avançar.
3. Confirmar a branch, o último commit, as funcionalidades efetivamente implementadas e o objetivo do passo atual.
4. Se não for possível aceder ao código, dizê-lo. Não presumir classes, ficheiros ou funcionalidades.

### Estilo de colaboração

- Português europeu, explicações técnicas claras e sem elogios automáticos.
- Claude atua como **mentor técnico e revisor**: dá pistas, decomposição lógica e pequenos exemplos; **não entrega ficheiros completos nem código pronto sem pedido explícito**.
- Alterações **Kotlin/XML uma a uma**, respeitando o estilo existente.
- Ao analisar código: identificar o problema, explicar a causa e orientar uma correção incremental.
- Trabalhar por etapas, com teste rápido, **um commit Git por funcionalidade** e README atualizado.
- Não inventar classes, tabelas, endpoints ou ficheiros. Distinguir sempre o que está confirmado no código do que é memória ou hipótese.

### Decisões em aberto a levar para a próxima conversa

- Campo de despesas vazio deve valer 0, ou deve continuar obrigatório?
- Quais os limites máximos aceitáveis para horas e preços?
- `Double` ou `BigDecimal` para valores monetários?
- A Splash conta como uma das três Activities? É obrigatória na UC00609?
- Persistência: continua fora do âmbito, salvo indicação em contrário.
- Como deve aparecer o orçamento itemizado (linhas e total)? A fórmula (horas × preço/hora + despesas) precisa de confirmação.

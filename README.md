# To-Do List

![Kotlin](https://img.shields.io/badge/Kotlin-2.3.10-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-BOM%202026.02.01-4285F4?logo=jetpackcompose&logoColor=white)
![Room](https://img.shields.io/badge/Room-2.7.1-3DDC84?logo=android&logoColor=white)
![minSdk](https://img.shields.io/badge/minSdk-24-blue)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

Aplicativo Android de lista de tarefas (to-do list) desenvolvido como projeto didático para a disciplina de Sistemas de Informação da FIAP. Construído com **Kotlin**, **Jetpack Compose** e **Room**, seguindo o padrão arquitetural **MVVM**.

## Funcionalidades

- Criar, editar e excluir tarefas
- Confirmar a exclusão em diálogo Material 3 com título da tarefa, Cancelar e Excluir
- Marcar tarefas como concluídas
- Definir data e horário de prazo para uma tarefa
- Lista ordenada por prazo, com destaque visual para tarefas atrasadas
- Persistência local dos dados (SQLite via Room) — as tarefas continuam disponíveis após fechar o app

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navegação | Navigation Compose |
| Persistência | Room (SQLite) |
| Assincronismo | Kotlin Coroutines + Flow |
| Testes | JUnit, Espresso, Compose UI Test |

## Arquitetura

O projeto segue o padrão **MVVM (Model-View-ViewModel)**, sem uso de frameworks de injeção de dependência (Hilt/Koin) — a criação de objetos é feita por uma fábrica manual, propositalmente, para fins didáticos.

```
app/src/main/java/carreiras/com/github/todolist/
├── data/          # Model — Entity, DAO e configuração do banco (Room)
├── repository/     # Model — abstrai o acesso a dados para a ViewModel
├── viewmodel/       # ViewModel — estado observável (StateFlow) e regras de apresentação
├── ui/             # View — telas em Jetpack Compose
├── navigation/       # View — rotas e navegação entre telas
└── util/           # Funções puras auxiliares (formatação/conversão de data e hora)
```

Uma explicação detalhada e comparada da arquitetura (MVC, MVP, MVI e MVVM), com trechos de código reais do projeto e diagramas, está em **[ARQUITETURA-MVVM.md](ARQUITETURA-MVVM.md)**.

## Pré-requisitos

- [Android Studio](https://developer.android.com/studio) compatível com Android Gradle Plugin 9.2
- JDK 21 para executar o Gradle; JDK 11 para a toolchain de compilação
- Um emulador Android (API 24+) ou dispositivo físico

## Como executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/juhsawaya/to-do-list.git
   ```
2. Abra a pasta do projeto no Android Studio e aguarde a sincronização do Gradle.
3. Selecione um emulador ou conecte um dispositivo físico.
4. Clique em **Run** ▶ ou execute pelo terminal:
   ```bash
   ./gradlew installDebug
   ```

## Testes

O projeto tem testes unitários (JVM) e instrumentados (Android):

```bash
# Testes unitários (ex: DataHoraUtilTest)
./gradlew test

# Testes instrumentados (ex: TarefaDaoTest), requer emulador/dispositivo conectado
./gradlew connectedAndroidTest
```

## Estrutura de navegação

O app tem duas telas, conectadas via Navigation Compose e compartilhando a mesma instância de `TarefaViewModel`:

- **Lista de tarefas** — tela inicial, exibe todas as tarefas ordenadas por prazo.
- **Formulário** — criação/edição de uma tarefa, incluindo seleção opcional de data e horário.

## CP5 — confirmação de exclusão

Ao tocar na lixeira, a lista mantém o ID da tarefa selecionada em `rememberSaveable` e abre um `AlertDialog`. Cancelar, pressionar Voltar ou tocar fora fecha o diálogo sem alterar os dados. Excluir fecha o diálogo e encaminha somente a tarefa selecionada à ViewModel, que mantém o acesso ao Room pela Repository.

A implementação está em `ListaTarefasScreen.kt`, incluindo a nova `ConfirmacaoExclusaoPreview`. Nenhuma rota ou tela de confirmação foi adicionada.

- [Cinco evidências em sequência](EVIDENCIAS_EXCLUSAO.md)
- [Execuções da compilação e testes](https://github.com/juhsawaya/to-do-list/actions)
- [Histórico de commits](https://github.com/juhsawaya/to-do-list/commits/main/)

`ConfirmacaoExclusaoTest` verifica a interação e o ID excluído. `EvidenciasExclusaoTest` executa o aplicativo real com Room no emulador, captura as cinco etapas e recria a Activity para conferir a persistência do resultado. O workflow publica as imagens somente após os testes passarem e disponibiliza o APK debug como artefato da execução.

### Configuração de compilação

O projeto usa `compileSdk = 36`, `targetSdk = 36` e `minSdk = 24`. O SDK de compilação foi ajustado porque a instalação do SDK 37 falhou no ambiente de CI. Core e Activity foram alinhados ao SDK 36; Kotlin e KSP foram alinhados à compilação com AGP 9.2. O histórico registra essas correções.

## Autor

Ewerton Carreira — projeto didático original.

Adaptação do CP5 no repositório de Júlia Sawaia, com auxílio de IA para implementação e validação.

## Licença

Este projeto é distribuído sob a licença MIT.

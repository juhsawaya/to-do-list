package carreiras.com.github.todolist.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import carreiras.com.github.todolist.data.Tarefa
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ConfirmacaoExclusaoTest {
    @get:Rule val compose = createComposeRule()

    @Test
    fun cancelarPreservaListaEConfirmarExcluiSomenteSelecionada() {
        val tarefas = mutableStateListOf(
            Tarefa(id = 1, titulo = "Estudar Room"),
            Tarefa(id = 2, titulo = "Enviar atividade")
        )
        val idsExcluidos = mutableListOf<Int>()
        compose.setContent {
            ListaTarefasContent(
                tarefas = tarefas,
                onNovaTarefa = {},
                onEditarTarefa = {},
                onCheckedChange = { _, _ -> },
                onDeletar = { tarefa ->
                    idsExcluidos.add(tarefa.id)
                    tarefas.remove(tarefa)
                }
            )
        }
        compose.onAllNodesWithContentDescription("Deletar tarefa")[0].performClick()
        compose.onNodeWithText("Excluir tarefa?").assertIsDisplayed()
        compose.onNodeWithText(
            "A tarefa “Estudar Room” será excluída permanentemente. Deseja continuar?"
        ).assertIsDisplayed()
        compose.runOnIdle { assertEquals(emptyList<Int>(), idsExcluidos) }
        compose.onNodeWithText("Cancelar").performClick()
        compose.onNodeWithText("Excluir tarefa?").assertDoesNotExist()
        compose.runOnIdle { assertEquals(listOf(1, 2), tarefas.map { it.id }) }
        compose.onAllNodesWithContentDescription("Deletar tarefa")[0].performClick()
        compose.onNodeWithText("Excluir").performClick()
        compose.onNodeWithText("Excluir tarefa?").assertDoesNotExist()
        compose.onNodeWithText("Estudar Room").assertDoesNotExist()
        compose.onNodeWithText("Enviar atividade").assertIsDisplayed()
        compose.runOnIdle {
            assertEquals(listOf(1), idsExcluidos)
            assertEquals(listOf(2), tarefas.map { it.id })
        }
    }
}

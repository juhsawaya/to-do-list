package carreiras.com.github.todolist.ui

import android.graphics.Bitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import carreiras.com.github.todolist.MainActivity
import carreiras.com.github.todolist.data.Tarefa
import carreiras.com.github.todolist.data.TarefaDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.io.File

/** Executa o app real com Room e registra as cinco etapas exigidas pelo CP5. */
class EvidenciasExclusaoTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun registrarFluxoCompletoNoEmulador() {
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        val dao = TarefaDatabase.getDatabase(contexto).tarefaDao()
        runBlocking {
            dao.listarTodas().first().forEach { dao.deletar(it) }
            dao.inserir(Tarefa(titulo = "Estudar Room", descricao = "Revisar DAO e banco de dados", dataHora = System.currentTimeMillis() - 86400000L))
            dao.inserir(Tarefa(titulo = "Enviar atividade", descricao = "Entregar CP5 no portal FIAP", dataHora = System.currentTimeMillis() + 86400000L))
            dao.inserir(Tarefa(titulo = "Comprar caderno", descricao = "Material para as aulas"))
        }
        compose.waitUntil(10000) {
            compose.onAllNodesWithText("Estudar Room").fetchSemanticsNodes().size == 1
        }
        compose.onNodeWithText("Enviar atividade").assertIsDisplayed()
        compose.onNodeWithText("Comprar caderno").assertIsDisplayed()
        capturar("01-lista-antes.png")

        compose.onAllNodesWithContentDescription("Deletar tarefa")[0].performClick()
        compose.onNodeWithText("Excluir tarefa?").assertIsDisplayed()
        compose.onNodeWithText("A tarefa “Estudar Room” será excluída permanentemente. Deseja continuar?").assertIsDisplayed()
        capturar("02-dialogo.png")
        compose.onNodeWithText("Cancelar").performClick()
        compose.onNodeWithText("Excluir tarefa?").assertDoesNotExist()
        compose.onNodeWithText("Estudar Room").assertIsDisplayed()
        assertEquals(3, runBlocking { dao.listarTodas().first().size })
        capturar("03-cancelar.png")

        compose.onAllNodesWithContentDescription("Deletar tarefa")[0].performClick()
        compose.onNodeWithText("Excluir tarefa?").assertIsDisplayed()
        capturar("04-dialogo-novamente.png")
        compose.onNodeWithText("Excluir").performClick()
        compose.waitUntil(10000) {
            compose.onAllNodesWithText("Estudar Room").fetchSemanticsNodes().isEmpty()
        }
        compose.onNodeWithText("Excluir tarefa?").assertDoesNotExist()
        compose.onNodeWithText("Enviar atividade").assertIsDisplayed()
        compose.onNodeWithText("Comprar caderno").assertIsDisplayed()
        assertEquals(setOf("Enviar atividade", "Comprar caderno"), runBlocking {
            dao.listarTodas().first().map { it.titulo }.toSet()
        })
        capturar("05-excluir.png")

        // Reabrir a Activity confirma que o resultado permanece no banco.
        compose.activityRule.scenario.recreate()
        compose.waitUntil(10000) {
            compose.onAllNodesWithText("Enviar atividade").fetchSemanticsNodes().size == 1
        }
        compose.onNodeWithText("Estudar Room").assertDoesNotExist()
        compose.onNodeWithText("Comprar caderno").assertIsDisplayed()
    }

    private fun capturar(nome: String) {
        compose.waitForIdle()
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.waitForIdleSync()
        val pasta = File(instrumentation.targetContext.getExternalFilesDir(null), "evidencias")
        check(pasta.exists() || pasta.mkdirs())
        val screenshot = requireNotNull(instrumentation.uiAutomation.takeScreenshot())
        File(pasta, nome).outputStream().use {
            check(screenshot.compress(Bitmap.CompressFormat.PNG, 100, it))
        }
        screenshot.recycle()
    }
}

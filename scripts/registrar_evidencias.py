"""Só registra evidências após o teste real concluir e gerar os cinco PNGs."""
from pathlib import Path
import os

etapas = [
    ("01-lista-antes.png", "Lista antes da exclusão", "Três tarefas, incluindo uma atrasada, uma com prazo futuro e uma sem prazo."),
    ("02-dialogo.png", "Diálogo aberto", "Confirmação sobre a lista com o título Estudar Room e as ações Cancelar e Excluir."),
    ("03-cancelar.png", "Resultado ao cancelar", "O diálogo foi fechado e as três tarefas foram preservadas, inclusive no Room."),
    ("04-dialogo-novamente.png", "Nova abertura", "A lixeira da mesma tarefa foi acionada novamente."),
    ("05-excluir.png", "Resultado após confirmar", "Somente Estudar Room foi removida. Enviar atividade e Comprar caderno permaneceram no Room.")
]
base = Path("docs/images/exclusao")
for nome, _, _ in etapas:
    arquivo = base / nome
    assert arquivo.is_file(), f"Captura ausente: {arquivo}"
    assert arquivo.read_bytes().startswith(b"\x89PNG\r\n\x1a\n"), f"PNG inválido: {arquivo}"
run = f"https://github.com/{os.environ['GITHUB_REPOSITORY']}/actions/runs/{os.environ['GITHUB_RUN_ID']}"
texto = f"# Evidências — confirmação de exclusão\n\nCapturas reais do aplicativo em emulador Android API 35, geradas por teste instrumentado com MainActivity e Room.\n\n[Execução dos testes]({run}) · Código testado: `{os.environ['GITHUB_SHA']}`.\n\nO teste também recria a Activity e verifica a persistência do resultado.\n"
for numero, (nome, titulo, descricao) in enumerate(etapas, 1):
    texto += f"\n## {numero}. {titulo}\n\n{descricao}\n\n![{titulo}](docs/images/exclusao/{nome})\n"
Path("EVIDENCIAS_EXCLUSAO.md").write_text(texto, encoding="utf-8")

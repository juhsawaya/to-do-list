# Evidências — confirmação de exclusão

Capturas reais do aplicativo em emulador Android API 35, geradas por teste instrumentado com MainActivity e Room.

[Execução dos testes](https://github.com/juhsawaya/to-do-list/actions/runs/35802113756) · Código testado: `2ec5fd7f5da50600b34626f78fd60c498d38e3ec`.

O teste também recria a Activity e verifica a persistência do resultado.

## 1. Lista antes da exclusão

Três tarefas, incluindo uma atrasada, uma com prazo futuro e uma sem prazo.

![Lista antes da exclusão](docs/images/exclusao/01-lista-antes.png)

## 2. Diálogo aberto

Confirmação sobre a lista com o título Estudar Room e as ações Cancelar e Excluir.

![Diálogo aberto](docs/images/exclusao/02-dialogo.png)

## 3. Resultado ao cancelar

O diálogo foi fechado e as três tarefas foram preservadas, inclusive no Room.

![Resultado ao cancelar](docs/images/exclusao/03-cancelar.png)

## 4. Nova abertura

A lixeira da mesma tarefa foi acionada novamente.

![Nova abertura](docs/images/exclusao/04-dialogo-novamente.png)

## 5. Resultado após confirmar

Somente Estudar Room foi removida. Enviar atividade e Comprar caderno permaneceram no Room.

![Resultado após confirmar](docs/images/exclusao/05-excluir.png)

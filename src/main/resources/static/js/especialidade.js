// Ao carregar o documento (quando o DOM estiver pronto), inicializa o DataTable
$(document).ready(function () {
    // Configura a localidade do Moment.js para pt-BR (português do Brasil)
    moment.locale('pt-BR');

    // Inicializa o DataTable para a tabela com ID 'table-especializacao'
    var table = $('#table-especializacao').DataTable({
        searching: true, // Habilita a funcionalidade de busca na tabela
        order: [[1, "asc"]], // Ordena a tabela pela segunda coluna (título) em ordem ascendente por padrão
        lengthMenu: [5, 10], // Define a quantidade de itens visíveis por página
        processing: true, // Exibe um indicador de carregamento enquanto os dados estão sendo processados
        serverSide: true, // Ativa o carregamento dos dados do lado do servidor
        responsive: true, // Torna a tabela responsiva, ajustando-se automaticamente a diferentes tamanhos de tela
        ajax: {
            url: '/especialidades/datatables/server', // URL do servidor para obter os dados via AJAX
            data: 'data' // Envia os dados (se necessário) ao servidor
        },
        columns: [
            { data: 'id' }, // Exibe o ID da especialização
            { data: 'titulo' }, // Exibe o título da especialização
            {
                orderable: false, // Desabilita a ordenação desta coluna
                data: 'id', // Utiliza o ID da especialização para gerar o link de edição
                "render": function (id) {
                    // Cria um botão de edição com link para editar a especialização
                    return '<a class="btn btn-success btn-sm btn-block" href="/especialidades/editar/' +
                        id + '" role="button"><i class="fas fa-edit"></i></a>';
                }
            },
            {
                orderable: false, // Desabilita a ordenação desta coluna
                data: 'id', // Utiliza o ID da especialização para gerar o link de exclusão
                "render": function (id) {
                    // Cria um botão de exclusão com link para excluir a especialização
                    return '<a class="btn btn-danger btn-sm btn-block" href="/especialidades/excluir/' +
                        id + '" role="button" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-times-circle"></i></a>';
                }
            }
        ]
    });
});

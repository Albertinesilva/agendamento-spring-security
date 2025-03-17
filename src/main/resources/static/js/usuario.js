// Inicialização do DataTable para lista de usuários
$(document).ready(function () {
  // Configuração da localidade do Moment.js para pt-BR (português do Brasil)
  moment.locale('pt-BR');

  // Inicializa o DataTable para a tabela com o ID #table-usuarios
  var table = $('#table-usuarios').DataTable({
    searching: true, // Habilita a funcionalidade de busca na tabela
    lengthMenu: [5, 10], // Define a quantidade de itens visíveis por página
    processing: true, // Mostra um indicador de carregamento enquanto os dados são carregados
    serverSide: true, // Ativa a funcionalidade server-side para carregar os dados dinamicamente
    responsive: true, // Torna a tabela responsiva para diferentes tamanhos de tela
    ajax: {
      url: '/u/datatables/server/usuarios', // URL para obter os dados via AJAX
      data: 'data' // Envia os dados (se necessário) ao servidor
    },
    columns: [
      { data: 'id' }, // Coluna que exibe o ID do usuário
      { data: 'email' }, // Coluna que exibe o email do usuário
      {
        data: 'ativo', // Coluna que exibe o status de atividade
        render: function (ativo) {
          // Renderiza 'Sim' ou 'Não' com base no valor da propriedade 'ativo'
          return ativo == true ? 'Sim' : 'Não';
        }
      },
      {
        data: 'perfis', // Coluna que exibe os perfis do usuário
        render: function (perfis) {
          var aux = new Array();
          // Itera sobre os perfis e extrai a descrição de cada um
          $.each(perfis, function (index, value) {
            aux.push(value.desc);
          });
          return aux; // Retorna um array com as descrições dos perfis
        },
        orderable: false, // Desabilita a ordenação desta coluna
      },
      {
        data: 'id', // Coluna para o botão de edição
        render: function (id) {
          // Cria um link para editar o usuário, com um botão estilizado
          return ''.concat('<a class="btn btn-success btn-sm btn-block"', ' ')
            .concat('href="').concat('/u/editar/credenciais/usuario/').concat(id, '"', ' ')
            .concat('role="button" title="Editar" data-toggle="tooltip" data-placement="right">', ' ')
            .concat('<i class="fas fa-edit"></i></a>');
        },
        orderable: false // Desabilita a ordenação desta coluna
      },
      {
        data: 'id', // Coluna para o botão de edição de perfis
        render: function (id) {
          // Cria um link para editar os perfis do usuário, com um botão estilizado
          return ''.concat('<a class="btn btn-info btn-sm btn-block"', ' ')
            .concat('id="dp_').concat(id).concat('"', ' ')
            .concat('role="button" title="Editar" data-toggle="tooltip" data-placement="right">', ' ')
            .concat('<i class="fas fa-edit"></i></a>');
        },
        orderable: false // Desabilita a ordenação desta coluna
      }
    ]
  });

  // Evento de clique no botão de editar perfis
  $('#table-usuarios tbody').on('click', '[id*="dp_"]', function () {
    var data = table.row($(this).parents('tr')).data(); // Obtém os dados da linha clicada
    var aux = new Array();
    // Itera sobre os perfis e cria um array com os IDs dos perfis
    $.each(data.perfis, function (index, value) {
      aux.push(value.id);
    });
    // Redireciona o usuário para a página de edição dos perfis
    document.location.href = '/u/editar/dados/usuario/' + data.id + '/perfis/' + aux;
  });

});

// Função que valida se as senhas digitadas no formulário são iguais
$('.pass').keyup(function () {
  // Se as senhas nos campos senha1 e senha2 são iguais, habilita o campo senha3, caso contrário, desabilita
  $('#senha1').val() === $('#senha2').val()
    ? $('#senha3').removeAttr('readonly') // Habilita o campo senha3
    : $('#senha3').attr('readonly', 'readonly'); // Desabilita o campo senha3
});

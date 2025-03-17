// processa o auto-complete de especialidades
$(function () {
  // remove o espaço depois da vírgula
  function split(val) {
    return val.split(/,\s*/); // Divide a string em termos separados por vírgula e espaço
  }

  // extrai o último termo adicionado
  function extractLast(term) {
    return split(term).pop(); // Retorna o último termo da lista
  }

  // adiciona a tag de input com a especialidade no HTML
  function addEspecializacao(titulo) {
    $('#listaEspecializacoes')
      .append('<input type="hidden" value="' + titulo + '" name="especialidades">'); // Adiciona um campo oculto com o nome da especialidade para envio ao servidor
  }

  // mostra na página um toast com mensagem de especialidades repetidas
  function exibeMessagem(texto) {
    // Cria e exibe uma mensagem de toast (notificação temporária)
    $('.add-toast').append(""
      .concat('<div class="toast" role="alert" aria-live="assertive" aria-atomic="true" data-delay="3500">',
        '<div class="toast-header">',
        '<strong class="mr-auto">Atenção</strong>',
        '<button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">',
        '  <span aria-hidden="true">&times;</span>',
        '</button>',
        '</div>',
        '<div class="toast-body">', texto, '</div>',
        '</div>')
    );
    $('.toast').toast('show'); // Exibe o toast
    $('.toast').on('hidden.bs.toast', function (e) {
      $(e.currentTarget).remove(); // Remove o toast da página quando ele desaparece
    });
  }

  // Configura o autocomplete para o campo de especialidades
  $("#autocomplete-especialidades")
    .on("keydown", function (event) {
      // Impede que o tab seja pressionado quando o menu de sugestões está ativo
      if (event.keyCode === $.ui.keyCode.TAB
        && $(this).autocomplete("instance").menu.active) {
        event.preventDefault();
      }
    })
    .autocomplete({
      // Função de origem para buscar as especialidades via JSON
      source: function (request, response) {
        $.getJSON("/especialidades/titulo", {
          termo: extractLast(request.term) // Passa o último termo digitado
        }, response); // Retorna os resultados da busca para o autocomplete
      },
      // Verifica se o termo digitado tem comprimento maior que 1 antes de realizar a busca
      search: function () {
        var term = extractLast(this.value);
        if (term.length < 1) {
          return false;
        }
      },
      // Previne que o valor seja inserido no campo ao focar no item
      focus: function () {
        return false;
      },
      // Função chamada quando uma sugestão é selecionada
      select: function (event, ui) {
        var terms = split(this.value); // Divide os valores atuais inseridos no campo de entrada
        console.log('1. this.value: ' + this.value)
        console.log('2. terms: ' + terms)
        console.log('3. ui.item.value: ' + ui.item.value)

        // Remove o último termo inserido
        terms.pop();
        console.log('4. terms: ' + terms)

        // Verifica se o valor já foi inserido na lista de termos
        var exists = terms.includes(ui.item.value);
        if (exists === false) {
          // Se não existir, adiciona o item à lista de termos
          terms.push(ui.item.value);
          console.log('5. terms: ' + terms)
          terms.push(""); // Adiciona uma vírgula para manter o campo de entrada aberto
          console.log('6. terms: ' + terms)
          this.value = terms.join(", "); // Atualiza o campo de entrada com os termos selecionados
          console.log('7. this.value: ' + this.value)
          console.log('8. ui.item.value: ' + ui.item.value)

          // Adiciona a especialização na página para envio ao controller
          addEspecializacao(ui.item.value);
        } else {
          // Se a especialização já foi selecionada, exibe uma mensagem de erro
          exibeMessagem('A Especialização <b>' + ui.item.value + '</b> já foi selecionada.');
        }
        return false; // Previne o comportamento padrão do autocomplete
      }
    });
});

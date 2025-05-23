package com.albertsilva.projects.consultamedica.security.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.albertsilva.projects.consultamedica.model.entities.Medico;
import com.albertsilva.projects.consultamedica.security.model.entities.Perfil;
import com.albertsilva.projects.consultamedica.security.model.entities.Usuario;
import com.albertsilva.projects.consultamedica.security.model.enums.PerfilTipo;
import com.albertsilva.projects.consultamedica.security.service.UsuarioService;
import com.albertsilva.projects.consultamedica.services.MedicoService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/u")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private MedicoService medicoService;

  // abrir cadastro de usuários (medico/admin/paciente)
  @GetMapping("/novo/cadastro/usuario")
  public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {
    return "usuario/cadastro";
  }

  // abrir lista de usuários
  @GetMapping("/lista")
  public String listaUsuarios() {
    return "usuario/lista";
  }

  // listar usuários na datatables
  @GetMapping("/datatables/server/usuarios")
  public ResponseEntity<?> listaUsuariosDatatables(HttpServletRequest request) {
    return ResponseEntity.ok(usuarioService.buscarUsuarios(request));
  }

  // salvar cadastro de usuários por administrador
  @PostMapping("/cadastro/salvar")
  public String salvarUsuarios(Usuario usuario, RedirectAttributes attr) {

    if (usuario.getPerfis().size() > 2 ||
        usuario.getPerfis().stream().map(Perfil::getId).collect(Collectors.toSet())
            .containsAll(Arrays.asList(1L, 3L))
        ||
        usuario.getPerfis().stream().map(Perfil::getId).collect(Collectors.toSet())
            .containsAll(Arrays.asList(2L, 3L))) {
      attr.addFlashAttribute("falha", "Paciente não pode ser Admin e/ou Médico!");
      attr.addFlashAttribute("usuario", usuario);
    } else {
      try {
        usuarioService.salvarUsuario(usuario);
        attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
      } catch (DataIntegrityViolationException ex) {
        attr.addFlashAttribute("falha", "Cadastro não realizado, email já existente!");
      }

    }
    return "redirect:/u/novo/cadastro/usuario";
  }

  // pre edição de credenciais de usuário
  @GetMapping("/editar/credenciais/usuario/{id}")
  public ModelAndView preEditarCredenciais(@PathVariable("id") Long id) {
    ModelAndView mv = new ModelAndView("usuario/cadastro");
    mv.addObject("usuario", usuarioService.buscarPorId(id));
    return mv;
  }

  // pre edição de cadastro de usuários
  @GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
  public ModelAndView preEditarCadastroDadosPessoais(@PathVariable("id") Long usuarioId,
      @PathVariable("perfis") Long[] perfisId) {
    Usuario usuario = usuarioService.buscarPorIdEPerfis(usuarioId, perfisId);

    if (usuario.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
        !usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
      return new ModelAndView("usuario/cadastro", "usuario", usuario);

    } else if (usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {

      Medico medico = medicoService.buscarPorUsuarioId(usuarioId);
      return medico.hasNotId()
          ? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))
          : new ModelAndView("medico/cadastro", "medico", medico);

    } else if (usuario.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
      ModelAndView mv = new ModelAndView("error");
      mv.addObject("status", 403);
      mv.addObject("error", "Área Restrita");
      mv.addObject("message", "Os dados de pacientes são restritos a ele mesmo");
      return mv;
    }
    return new ModelAndView("redirect:/u/lista");

  }

  @GetMapping("/editar/senha")
  public String abrirEditarSenha() {
    return "usuario/editar-senha";
  }

  @PostMapping("/confirmar/senha")
  public String editarSenha(@RequestParam("senha1") String senha1, @RequestParam("senha2") String senha2,
      @RequestParam("senha3") String senha3, @AuthenticationPrincipal User user, RedirectAttributes attr) {

    if (!senha1.equals(senha2)) {
      attr.addFlashAttribute("falha", "Senhas não conferem, tente novamente");
      return "redirect:/u/editar/senha";
    }

    Usuario usuarioLogado = usuarioService.buscarPorEmail(user.getUsername());

    if (!UsuarioService.isSenhaCorreta(senha3, usuarioLogado.getSenha())) {
      attr.addFlashAttribute("falha", "Senha atual não confere, tente novamente");
      return "redirect:/u/editar/senha";
    }

    usuarioService.alterarSenha(usuarioLogado, senha1);
    attr.addFlashAttribute("sucesso", "Senha alterada com sucesso!");
    return "redirect:/u/editar/senha";
  }

  // abrir página de novo cadastro de paciente
  @GetMapping("/novo/cadastro")
  public String novoCadastro(Usuario usuario) {
    return "cadastrar-se";
  }

  // página de resposta do cadastro de paciente
  @GetMapping("/cadastro/realizado")
  public String cadastroRealizado() {
    return "fragments/mensagem";
  }

  // receber o form da página cadastrar-se
  @PostMapping("/cadastro/paciente/salvar")
  public String salvarCadastroPaciente(Usuario usuario, BindingResult result) throws MessagingException {
    try {
      usuarioService.salvarCadastroPaciente(usuario);
    } catch (DataIntegrityViolationException ex) {
      result.reject("email", "Ops... Este e-mail já existe na base de dados.");
      return "cadastrar-se";
    }
    return "redirect:/u/cadastro/realizado";
  }

  // recebe a requisição de confirmação de cadastro
  @GetMapping("/confirmacao/cadastro")
  public String respostaConfirmacaoCadastroPaciente(@RequestParam("codigo") String codigo, RedirectAttributes attr) {
    usuarioService.ativarCadastroPaciente(codigo);
    attr.addFlashAttribute("alerta", "successo");
    attr.addFlashAttribute("titulo", "Cadastro Ativado!");
    attr.addFlashAttribute("texto", "Parabéns, seu cadastro está ativo.");
    attr.addFlashAttribute("subtexto", "Siga com seu login/senha");
    return "redirect:/login";
  }

  // abre a pagina de pedido de redefinição de senha
  @GetMapping("/p/redefinir/senha")
  public String pedidoRedefinirSenha() {
    return "usuario/pedido-recuperar-senha";
  }

  // form de pedido de recuperar senha
  @GetMapping("/p/recuperar/senha")
  public String redefinirSenha(String email, ModelMap model) throws MessagingException {
    usuarioService.pedidoRedefinicaoDeSenha(email);
    model.addAttribute("sucesso",
        "Em instantes você receberá um e-mail para prosseguir com a redefinição de sua senha.");
    model.addAttribute("usuario", new Usuario(email));
    return "usuario/recuperar-senha";
  }

  // salvar a nova senha via recuperacao de senha
  @PostMapping("/p/nova/senha")
  public String confirmacaoDeRedefinicaoDeSenha(Usuario usuario, ModelMap model) {
    Usuario usuarioRedefinirSenha = usuarioService.buscarPorEmail(usuario.getEmail());
    if (!usuario.getCodigoVerificador().equals(usuarioRedefinirSenha.getCodigoVerificador())) {
      model.addAttribute("falha", "Código verificador não confere.");
      return "usuario/recuperar-senha";
    }
    usuarioRedefinirSenha.setCodigoVerificador(null);
    usuarioService.alterarSenha(usuarioRedefinirSenha, usuario.getSenha());
    model.addAttribute("alerta", "sucesso");
    model.addAttribute("titulo", "Senha redefinida!");
    model.addAttribute("texto", "Você já pode logar no sistema.");
    return "login";
  }

}

package com.agendamedico.spring_security.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.agendamedico.spring_security.domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

  /**
   * Busca um usuário pelo e-mail.
   *
   * @param email E-mail do usuário.
   * @return O usuário encontrado.
   */
  @Query("select u from Usuario u where u.email like :email")
  Usuario findByEmail(@Param("email") String email);

  /**
   * Busca usuários pelo e-mail ou descrição do perfil.
   * O filtro de busca pode ser feito tanto pelo e-mail do usuário quanto pela
   * descrição de um dos perfis associados.
   *
   * @param search   Termo de busca para o e-mail ou descrição do perfil.
   * @param pageable Objeto contendo as informações de paginação.
   * @return Uma página de usuários que correspondem ao critério de busca.
   */
  @Query("select distinct u from Usuario u "
      + "join u.perfis p "
      + "where u.email like :search% OR p.desc like :search%")
  Page<Usuario> findByEmailOrPerfil(String search, Pageable pageable);

  /**
   * Busca um usuário pelo ID e verifica se ele possui os perfis fornecidos.
   *
   * @param usuarioId ID do usuário.
   * @param perfisId  IDs dos perfis a serem verificados.
   * @return Um objeto Optional contendo o usuário se encontrado com os perfis
   *         especificados, ou vazio caso contrário.
   */
  @Query("select u from Usuario u "
      + "join u.perfis p "
      + "where u.id = :usuarioId AND p.id IN :perfisId")
  Optional<Usuario> findByIdAndPerfis(Long usuarioId, Long[] perfisId);

  @Query("select u from Usuario u where u.email like :email and u.ativo = true")
  Optional<Usuario> findByEmailAndAtivo(String email);

}

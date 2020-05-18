package curso.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.api.rest.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	@Query("select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin (String login);

	//convencao so Sping nem precisa colocar a query ele ja sabe que vai buscar assim:
//	findBy - busca padrão (especificação Spring Data JPA) 
//	Nome - atributo nome da entidade (especificação Spring Data JPA) 
//	ContainingIgnoreCase - ignora o case (especificação Spring Data JPA) 
//	@Query("select u from Usuario u where u.nome like %?1%")
	List<Usuario> findByNomeContainingIgnoreCase (String nome);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into usuarios_role (usuario_id, role_id) values (?1, (select id from role where nome_role = 'ROLE_USER'))")
	void insereAcessoRolePadrao (Long idUser);
}

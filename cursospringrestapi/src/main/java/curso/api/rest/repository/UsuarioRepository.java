package curso.api.rest.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	
	default Page<Usuario> findUserByNamePage (String nome, PageRequest pageResquest){
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
			.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Usuario> example = Example.of(usuario, exampleMatcher);
		
		Page<Usuario> retorno = findAll(example, pageResquest);
		
		return retorno;
	}
}

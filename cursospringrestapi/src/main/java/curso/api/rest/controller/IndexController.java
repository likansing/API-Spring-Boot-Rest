package curso.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;

@RestController /*Arquitetura REST*/
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	/*Servico RESTfull*/
	/*RequestParam tem q vir na URL ?nome=carlos*/
//	@GetMapping(value = "/", produces = "application/json")
//	public ResponseEntity<Usuario> init(@RequestParam (value = "nome", defaultValue = "Convidado nome default") String nome,
//			@RequestParam(value = "salario", defaultValue = "8000") Long salario) {
//		
//		System.out.println("Parametro recebido nome: " + nome + " Salario: " + salario);
//		
//		Usuario usuario = new Usuario();
//		usuario.setId(50L);
//		usuario.setLogin("Likansing");
//		usuario.setNome("Carlos Madureira");
//		usuario.setSenha("123456");
//		
//		Usuario usuario2 = new Usuario();
//		usuario2.setId(10L);
//		usuario2.setLogin("dumadureira@gmail.com");
//		usuario2.setNome("teste");
//		usuario2.setSenha("123");
//		
//		List<Usuario> usuarios = new ArrayList<Usuario>();
//		usuarios.add(usuario);
//		usuarios.add(usuario2);
//		
//		return new ResponseEntity (usuarios, HttpStatus.OK);
//		
//	}
	
	
	/*PathVariable URL vem apenas por exemplo abaixo o ID assim: usuario/5/relatoriopdf */
	@GetMapping(value = "/{id}/codigovenda/{venda}", produces = "application/pdf")
	public ResponseEntity<Usuario> relatorio(@PathVariable(value = "id") Long id, @PathVariable (value = "venda") Long venda) {
		
		Usuario usuario = usuarioRepository.findById(id).get();
		
		/*retorno seria um relatorio*/
		return new ResponseEntity<Usuario> (usuario, HttpStatus.OK);
	}
	
	/*PathVariable URL vem apenas por exemplo abaixo o ID assim: usuario/5 */
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id") Long id) {
		
		Usuario usuario = usuarioRepository.findById(id).get();
				
		return new ResponseEntity<Usuario> (usuario, HttpStatus.OK);
	}
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> listAll(){
		
		List<Usuario> list = usuarioRepository.findAll();
		
		return new ResponseEntity<List<Usuario>> (list, HttpStatus.OK);
	}
	
	
	/*ResquestBody vai converter o json recebido em obj da classe usuario no exemplo abaixo*/
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario){
		
		for(int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		Usuario usuarioSalvar = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvar,HttpStatus.OK);
		
	}
	
	/*ResquestBody vai converter o json recebido em obj da classe usuario no exemplo abaixo*/
	@PostMapping(value = "/vendausuario", produces = "application/json")
	public ResponseEntity<Usuario> cadastrarVenda(@RequestBody Usuario usuario){
		
		/*aqui seria o processo de venda do usuario*/
		
		Usuario usuarioSalvar = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvar,HttpStatus.OK);
		
	}
	
	/*@Pathvariable vai receber os parametros na URL, exemplo: http://localhost:8080/usuario/3/idenda/188*/
	@PostMapping(value = "/{iduser}/idenda/{idvenda}", produces = "application/json")
	public ResponseEntity<Usuario> cadastrarVenda2(@PathVariable(value = "iduser") Long iduser, @PathVariable (value = "idvenda") Long idvenda){
		
		/*aqui seria o processo de venda2 do usuario*/
		
		Usuario usuario = usuarioRepository.findById(iduser).get();
		
		return new ResponseEntity<Usuario> (usuario, HttpStatus.OK);
		
	}
	
	/*ResquestBody vai converter o json recebido em obj da classe usuario no exemplo abaixo*/
	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario){
		
		for(int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		/*Usa o save mesmo pois com ID vindo no obj ele sabe que eh update.*/
		/*sem receber o id no obj recebido da tela ira salar um novo registro no bd*/
		Usuario usuarioSalvar = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvar,HttpStatus.OK);
		
	}
	
	
	/*URL exemplo: usuario/5 */
	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String delete(@PathVariable(value = "id") Long id) {
		
		usuarioRepository.deleteById(id);
				
		return "ok";
	}
	
	/*URL: usuario/5/venda */
	@DeleteMapping(value = "/{id}/venda", produces = "application/text")
	public String deleteenda(@PathVariable(value = "id") Long id) {
		
		usuarioRepository.deleteById(id);
		return "ok";
	}
	
}

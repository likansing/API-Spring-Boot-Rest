package curso.api.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.ObjetoError;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;

@RestController
@RequestMapping(value = "/recuperar")
public class RecuperaController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@ResponseBody
	@PostMapping(value = "/")
	public ResponseEntity<ObjetoError> recuperar(@RequestBody Usuario login) {

		ObjetoError objetoError = new ObjetoError();

		Usuario user = usuarioRepository.findUserByLogin(login.getLogin());

		if (user == null) {
			objetoError.setCode("404"); /* nao encontrado */
			objetoError.setError("Usuario nao encontrado");
		} else {
			/* rotina de envio de email */
			objetoError.setCode("200"); /* user encontrado */
			objetoError.setError("Acesso enviado para seu e-mail");
		}

		return new ResponseEntity<ObjetoError>(objetoError, HttpStatus.OK);
	}

}

package curso.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.api.rest.ApplicationContextLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {

	/*TEMPO DE EXPIRACAO DO TOKEN EM ms - 2 dias no caso abaixo*/
	private static final long EXPIRATION_TIME = 172800000;
	
	/* SENHA UNICA PARA COMPOR A AUTENTICACAO - PARA COMPOR A FORMACAO DO TOKEN */
	private static final String SECRET = "SenhaExtremamenteSecreta";
	
	/* prefixo padrao de token */
	private static final String TOKEN_PREFIX = "Bearer";
	
	/* padrao abaixo. Prefixo do cabecalho */
	private static final String HEADER_STRING = "Authorization";
	
	/* Gerando Token de autenticacao e adicionando ao cabecalho e resposta HTTP */
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {
		
		/*Montagem do Token*/
		String JWT = Jwts.builder() /*chama gerador de token*/
									.setSubject(username)   /*adiciona usuario*/
									.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /* tempo de expiracao*/
									.signWith(SignatureAlgorithm.HS512, SECRET) /*algoritmo de geracao de senha*/
									.compact(); /*compactacao*/
		
		/*junta o token com o prefixo*/
		String token = TOKEN_PREFIX + " " + JWT; /* exemplo: Bearer 45465464646464646464646 */
		
		/*adiciona no cabelho HTTP*/
		response.addHeader(HEADER_STRING, token); /* exemplo de como fica: Authorization: Bearer 45465464646464646464646 */
		
		/*Para liberar a resposta para porta diferente do projeto Angular*/
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		/*escreve token como resposta no corpo do HTTP*/
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");
	}
	
	/*retorna o usuario validado com token ou caso nao seja valido retorna null*/
	public Authentication getAuthentication (HttpServletRequest request, HttpServletResponse response) {
		
		/*pega o token enviado no cabecalho HTTP*/
		String token = request.getHeader(HEADER_STRING);
		
		if(token != null) {
			/*Faz a validacao do token do usuario na requisicao*/
			String user = Jwts.parser()
									.setSigningKey(SECRET)  /* exemplo: Bearer 45465464646464646464646 */
									.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))  /* exemplo: 45465464646464646464646 */
									.getBody().getSubject();  /*fica somente user, ex: Carlos*/
			if(user != null) {
				Usuario usuario = ApplicationContextLoad.getApplicationContext()
								.getBean(UsuarioRepository.class).findUserByLogin(user);
				if (usuario != null) {
					/*retornar o usuario logado*/
					return new UsernamePasswordAuthenticationToken(usuario.getLogin(),usuario.getSenha(), usuario.getAuthorities());
				}
			}
		}
		/*Para liberar a resposta para porta diferente do projeto Angular*/
		response.addHeader("Access-Control-Allow-Origin", "*");
		return null; /*nao autorizado*/
	
	}
	
}

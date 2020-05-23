package curso.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import curso.api.rest.service.ImplementacaoUserDetailsService;

/* Classe que mapeia URL's, enderecos, autoriza ou bloqueia acessos a url's */
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	/*Configura as solicitacoes de acesso por HTTP (web)*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/*Ativando a protecao contra usuarios que nao estao validados por token*/
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		/*Ativando a permissao para acesso a pagina inicial do sistema que eh "/" ou "index"  */
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index", "/recuperar/**").permitAll()
		
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		/*URL de logout - redireciona apos user deslogar*/
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		/*mapeia URL de Logout e invalida user*/
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		/*Filtra as requisicoes de logion para autenticacao*/
		.and().addFilterBefore(new  JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)	
		/* Filtra demais requisicoes para verificar a presenca do TOKIEN JWT no reader HTTP */
		.addFilterBefore(new JwtAPIAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		/*Service que ira consultar o usuario no BD*/
		auth.userDetailsService(implementacaoUserDetailsService)
		/*padrao de codificacao de senha*/
		.passwordEncoder(new BCryptPasswordEncoder());
		
	}
	
}

package curso.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = {"curso.api.rest.model"})
@ComponentScan(basePackages = {"curso.*"})
@EnableJpaRepositories(basePackages = {"curso.api.rest.repository"})
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
public class CursospringrestapiApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(CursospringrestapiApplication.class, args);
	}
	
	
	/*Mapeamento global que reflete em todo o sistema*/
	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/usuario/**").allowedMethods("*").allowedOrigins("*");
		
		/*libera acesso a todos os controles e todos os end-points*/
//		registry.addMapping("/**");
		
		/*libera acesso a todos os end-points para o controller que cuida de /usuario  */
//		registry.addMapping("/usuario");
		
		/*libera acesso a todos os end-points para o controller que cuida de /usuario  */
//		registry.addMapping("/usuario/**");
//		ou assim, por padrao se nao informado os metodos eh todos
//		registry.addMapping("/usuario/**").allowedMethods("*");
		
		/*caso queria habilitar so os end-points de POST  */
//		registry.addMapping("/usuario/**").allowedMethods("POST");
		
		/*Assim tb define as origens, seria habilitar requisicoes POST, DELETE para o cliente 40 e 5  */
//		registry.addMapping("/usuario/**").allowedMethods("POST", "DELETE").allowedOrigins("www.cliente40.com.br","www.cliente5.com.br", "localhost:8080");
		
	}
	

}

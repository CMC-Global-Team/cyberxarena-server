package internetcafe_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Internet Cafe Management API")
                        .description("API documentation for Internet Cafe Management System")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("CMC Global Team")
                                .email("support@cmcglobal.com")
                                .url("https://github.com/CMC-Global-Team/cyberxarena-server"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/api/v1")
                                .description("Development server"),
                        new Server()
                                .url("https://cyberxarena-server.onrender.com/api/v1")
                                .description("Production server (Render.com)"),
                        new Server()
                                .url("https://cyberxarena-server.herokuapp.com/api/v1")
                                .description("Legacy server (Heroku)")
                ));
    }
}

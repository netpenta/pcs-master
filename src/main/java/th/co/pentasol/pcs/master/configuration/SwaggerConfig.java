package th.co.pentasol.pcs.master.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@SuppressWarnings("all")
@Configuration
public class SwaggerConfig {

    private final String moduleName;

    public SwaggerConfig(@Value("${app.module-name}") String moduleName) {
        this.moduleName = moduleName;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Authorization";
        final String apiTitle = String.format("%s API", StringUtils.capitalize(moduleName));
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(
                new Components()
                    .addSecuritySchemes(securitySchemeName,
                            new SecurityScheme()
                                    .name(securitySchemeName)
                                    .type(SecurityScheme.Type.APIKEY)
                                    .in(SecurityScheme.In.HEADER)
                    )
            )
            .info(new Info().title(apiTitle));
    }
}

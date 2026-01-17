package gr.hua.dit.fittrack_integration.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for FitTrack Integration Service.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("FitTrack Integration Service API")
                        .version("v1")
                        .description(
                                "Integration services for FitTrack including weather data "
                                        + "and notification delivery (SMS / Email)."
                        ));
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("fittrack-integration")
                .packagesToScan("gr.hua.dit.fittrack_integration.web.rest")
                .pathsToMatch("/api/**")
                .build();
    }
}

package cz.ivosahlik.configuration;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SwaggerConfiguration {
    @Bean
    fun generalApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
                .group("General")
                .pathsToExclude("/api/**")
                .build()
    }
}


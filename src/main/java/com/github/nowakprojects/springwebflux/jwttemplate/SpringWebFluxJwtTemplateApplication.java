package com.github.nowakprojects.springwebflux.jwttemplate;

import com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.infrastructure.time.CurrentTimeProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({
		CurrentTimeProperties.class
})
@SpringBootApplication
public class SpringWebFluxJwtTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebFluxJwtTemplateApplication.class, args);
	}
}

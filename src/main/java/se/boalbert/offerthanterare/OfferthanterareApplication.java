package se.boalbert.offerthanterare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class OfferthanterareApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		args = new String[] {"--spring.config.location=file:C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\conf\\offert\\application.properties"};
		SpringApplication.run(OfferthanterareApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(OfferthanterareApplication.class);
	}

}

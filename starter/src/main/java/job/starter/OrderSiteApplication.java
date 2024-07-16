package job.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {"job"})
@EnableJpaRepositories(basePackages = {"job.persistence"})
@EntityScan("job.persistence.entity")
@PropertySource("classpath:controller.properties")
@PropertySource("classpath:service.properties")
@PropertySource("classpath:persistence.properties")
public class OrderSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderSiteApplication.class, args);
	}
}

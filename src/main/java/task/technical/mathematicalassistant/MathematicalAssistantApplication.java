package task.technical.mathematicalassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MathematicalAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(MathematicalAssistantApplication.class, args);
	}

}

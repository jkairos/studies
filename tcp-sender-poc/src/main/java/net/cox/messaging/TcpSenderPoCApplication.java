package net.cox.messaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = { "net.cox.messaging" })
public class TcpSenderPoCApplication {
	public static void main(String[] args) {
		SpringApplication.run(TcpSenderPoCApplication.class, args);
	}
}

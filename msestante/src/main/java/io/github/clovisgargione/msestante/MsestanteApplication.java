package io.github.clovisgargione.msestante;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

@RefreshScope
@SpringBootApplication
@EnableDiscoveryClient
@EnableRabbit
@EnableFeignClients
public class MsestanteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsestanteApplication.class, args);
	}

}

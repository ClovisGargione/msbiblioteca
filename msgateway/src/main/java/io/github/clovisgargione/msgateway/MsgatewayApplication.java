package io.github.clovisgargione.msgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class MsgatewayApplication {

    public static void main(String[] args) {
	SpringApplication.run(MsgatewayApplication.class, args);
    }

}

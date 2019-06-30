package org.elastoscarrier.dposprofit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DposProfitApplication {

	public static void main(String[] args) {
		SpringApplication.run(DposProfitApplication.class, args);
	}

}

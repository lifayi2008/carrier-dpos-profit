package org.elastoscarrier.dposprofit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DPosProfitApplication {

	public static void main(String[] args) {
		SpringApplication.run(DPosProfitApplication.class, args);
	}

}

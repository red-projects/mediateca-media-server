package com.redprojects.mediateca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MediaTecaMediaServerApplication {


	public static void main(String[] args) {
		// check token validity
		System.out.println("PRINTING OUT ARGS");
		for (int i = 0; i < args.length; i++) {
			System.out.println(args);
		}

		SpringApplication.run(MediaTecaMediaServerApplication.class, args);
	}



}

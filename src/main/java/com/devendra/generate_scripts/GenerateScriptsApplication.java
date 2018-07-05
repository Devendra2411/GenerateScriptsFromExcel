package com.devendra.generate_scripts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.devendra.generate_scrpts.resource.GenerateScripts;

@SpringBootApplication
@ComponentScan(basePackageClasses=GenerateScripts.class)
public class GenerateScriptsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenerateScriptsApplication.class, args);
	}
}

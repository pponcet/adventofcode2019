package com.x4oc.adventofcode2019;

import com.x4oc.adventofcode2019.day01.Star01;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Adventofcode2019Application {

	public static void main(String[] args) {
		SpringApplication.run(Adventofcode2019Application.class, args);
		Star01.execute();
	}

}

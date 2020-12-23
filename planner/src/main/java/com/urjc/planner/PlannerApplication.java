package com.urjc.planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.amqp.core.Queue;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlannerApplication.class, args);
	}

	@Bean
	public Queue myQueue() {
		return new Queue("messages", false);
	}

}

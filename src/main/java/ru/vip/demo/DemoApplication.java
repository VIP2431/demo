package ru.vip.demo;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class DemoApplication { // Вариант 1 implements CommandLineRunner {

/* Вариант 1
	@Autowired
	private ApplicationContextProvider applicationContextProvider;

	@Override
	public void run(String... args)  throws Exception {
		applicationContextProvider.printBeanContext();
	}
*/
//  Вариант 1 + 2
	public static void main(String[] args) throws Exception {
		SpringApplication.run(DemoApplication.class, args);
	}

/**
 * Вариант 3.
	public static void main(String[] args)  throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoApplication.class);
		context.getBean( ApplicationContextProvider.class).printBeanContext();
	}
*/

}

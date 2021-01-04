package ru.vip.demo;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class DemoApplication { // Вариант 3 implements CommandLineRunner {

/**
 * Вариант 1
 *
*/
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

/**
 * Вариант 2.
 *
 public static void main(String[] args)  throws Exception {
 AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoApplication.class);
 context.getBean( ApplicationContextProvider.class).printBeanContext();
 }
 */



/**
 * Вариант 3
 *
	@Autowired
	private ApplicationContextProvider applicationContextProvider;

	public static void main(String[] args) throws Exception {
	SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args)  throws Exception {
		applicationContextProvider.printBeanContext();
	}

*/

}

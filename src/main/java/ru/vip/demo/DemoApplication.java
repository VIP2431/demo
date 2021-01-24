package ru.vip.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication { // Вариант 3 implements CommandLineRunner {
/**
 * Варианты запуска приложения для организации доступа к applicationContext
 *
 * Вариант 1   Доступ к applicationContext из других Классов
 *
*/

//	static ApplicationContextProvider provid
//	public DemoApplication(ApplicationContextProvider provider) {
//		this.provider = provider;
//	}
//
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
//		if (provider != null) {
//			provider.handleApplicationContext();
//		}
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

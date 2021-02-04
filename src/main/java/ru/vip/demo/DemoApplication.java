package ru.vip.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication { // Вариант 3 implements CommandLineRunner {
/*
 * Варианты запуска приложения для организации доступа к applicationContext
 *
 * Вариант 1   Доступ к applicationContext из других Классов
 *
*/

	static private ApplicationContextProvider provider;

	DemoApplication(ApplicationContextProvider provider) {
		this.provider = provider;
	}

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);

		if (provider != null) {
			provider.handleApplicationContext();
		}

/*
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoApplication.class);

		if (provider != null) { provider.handleApplicationContext(); }

		ClassLoader.getSystemResource(classFilter);
		context.getBean( ApplicationContextProvider.class).printBeanContext();
		private static String classFilter = "demo.";
		ApplicationContext ctx= new AnnotationConfigApplicationContext(classFilter);
		int i = 0;
		for (String str : ctx.getBeanDefinitionNames()){
			System.out.println("-- [" + ++i + "] " + str);
*/
	}

/*
 * Вариант 2.
 *
 public static void main(String[] args)  throws Exception {
 	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoApplication.class);
 	context.getBean( ApplicationContextProvider.class).printBeanContext();
 }
*/

/*
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

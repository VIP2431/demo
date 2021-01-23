package ru.vip.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Value("${names.test}")    // Передача еденичного параметра из application.yml
	private String nameTest;
	@Value("${names.number}")   // Передача еденичного параметра из application.yml
	private int number;

	@Autowired
	ApplicationContextProvider provider;

	@Test
	public void initTestProvider() {

		provider.handleApplicationContext();
	}
}

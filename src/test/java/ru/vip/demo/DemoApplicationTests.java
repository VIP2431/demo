package ru.vip.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Value("${names.test}")    // Передача еденичного параметра из application.yml
	String nameTest;

	@Value("${names.number}")   // Передача еденичного параметра из application.yml
	int number;

	@Autowired
	ApplicationContextProvider provider;

	@Test
	public void initTestProvider() {//} throws Throwable  { //}  throws InterruptedException {
		for(int i = 0; i <3; ++i) {
			provider.handleApplicationContext();
		}
	}
}

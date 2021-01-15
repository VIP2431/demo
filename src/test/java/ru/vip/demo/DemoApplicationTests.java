package ru.vip.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
	@Autowired
	ApplicationContextProvider provider;

//	@Test
//	@BeforeAll
//	public void initTestProvider() {
//		provider.setNUMBER_BEAN(9);
//		provider.setTARGET_NAME("???");
//		provider.handleApplicationContext();
//	}

	@Test
	public void initTestProvider() {
		provider.setNUMBER_BEAN(58);     // Номер Бина для распечатки Анотаций и Методов
		provider.setTARGET_NAME("Jpa");  // Строка поиска для распечатки списка Бинов null - печатать все Бины
		provider.setFLAG_TO_STRING(false);   // Больше 0 Распечатывает первичную информацию
		provider.setCNT_METHOD(3);       // Распечатывает указанное Количество методов Бина
		provider.handleApplicationContext();
	}
}

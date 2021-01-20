package ru.vip.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
	private int[] LIST_BEAN = { 34, 58};
	@Autowired
	ApplicationContextProvider provider;
	private static final Logger logger = LoggerFactory.getLogger(DemoApplicationTests.class);

//	@Test
//	@BeforeAll
//	public void initTestProvider() {
//		provider.setNUMBER_BEAN(9);
//		provider.setTARGET_NAME("???");
//		provider.handleApplicationContext();
//	}

	@Test
	public void initTestProvider() {
		logger.debug("****** TEST LOGGER ******* DEBUG -->   DemoApplication  *** DEBUG ***");
		logger.error("****** TEST LOGGER ******* ERROR -->   DemoApplication  *** ERROR ***");
		logger.info("****** TEST LOGGER ******* INFO -->   DemoApplication  *** INFO ***");
		logger.warn("****** TEST LOGGER ******* WARN -->   DemoApplication  *** WARN ***");
		provider.setNUMBER_BEAN( LIST_BEAN);     // Номер Бина для распечатки Анотаций и Методов
		provider.setTARGET_NAME("Jpa");          // Строка поиска для распечатки списка Бинов null - печатать все Бины
		provider.setFLAG_TO_STRING(false);       // true - Распечатывает первичную информацию
		provider.setCNT_METHOD(3);               // Распечатывает указанное Количество методов Бина
		provider.handleApplicationContext();
	}
}

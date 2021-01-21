package ru.vip.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	// Передача группы параметров из application.yml

//	@Autowired
//	private final ParamProvider paramProvider;
//
//
//	DemoApplicationTests(ParamProvider prm) {
//		this.paramProvider = prm;
//	}

	@Value("${names.test}")    // Передача еденичного параметра из application.yml
	private String nameTest;
	@Value("${names.number}")   // Передача еденичного параметра из application.yml
	private int number;

	@Value("${provider.params.TARGET_BEAN}")   // Передача еденичного параметра из application.yml
	private String nameBean;

	@Value("${provider.params.COUNT_METHOD}")   // Передача еденичного параметра из application.yml
	private int countMethod;

	@Value("${provider.params.LIST_BEAN}")   // Передача еденичного параметра из application.yml
	private int[] listBean;

	@Value("${provider.params.ORIGINAL_STRING}")   // Передача еденичного параметра из application.yml
	private boolean originalString;

	@Autowired
	ApplicationContextProvider provider;
	private static final Logger log = LoggerFactory.getLogger(DemoApplicationTests.class);

	@Test
	public void initTestProvider() {

		log.error("****** TEST LOGGER ******* ERROR -->   DemoApplication  *** ERROR ***");
		log.warn("****** TEST LOGGER ******* WARN -->   DemoApplication  *** WARN ***");
		log.info("****** TEST LOGGER ******* INFO -->   DemoApplication  *** INFO {} ***", nameBean);
		log.debug("****** TEST LOGGER ******* DEBUG -->   DemoApplication  *** DEBUG {} ***", number);
		log.trace("****** TEST LOGGER ******* TRACE -->   DemoApplication  *** TRACE ***");
		log.info( nameTest );

		provider.setTARGET_NAME(nameBean);            // Строка поиска для распечатки списка Бинов null - печатать все Бины
		provider.setCNT_METHOD(countMethod);          // Распечатывает указанное Количество методов Бина
		provider.setNUMBER_BEAN( listBean);           // Строка поиска для распечатки списка Бинов null - печатать все Бины
		provider.setFLAG_TO_STRING(originalString);   // true - Распечатывает первичную информацию


//		provider.setNUMBER_BEAN( prm.getLIST_BEAN());          // Строка поиска для распечатки списка Бинов null - печатать все Бины
//		provider.setTARGET_NAME(prm.getTARGET_BEAN());         // Строка поиска для распечатки списка Бинов null - печатать все Бины
//		provider.setFLAG_TO_STRING(prm.isORIGINAL_STRING());   // true - Распечатывает первичную информацию
//		provider.setCNT_METHOD(prm.getCOUNT_METHOD());         // Распечатывает указанное Количество методов Бина
//
		provider.handleApplicationContext();
	}
}

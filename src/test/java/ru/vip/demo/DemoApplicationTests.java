package ru.vip.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Value("${names.test}")    // Передача еденичного параметра из application.yml
	private String nameTest;
	@Value("${names.number}")   // Передача еденичного параметра из application.yml
	private int number;

	private static final Logger log = LoggerFactory.getLogger(DemoApplicationTests.class);

	@Autowired
	ApplicationContextProvider provider;

	@Test
	public void initTestProvider() {

		log.error("****** TEST LOGGER ******* ERROR -->   DemoApplication  *** ERROR ***");
		log.warn("****** TEST LOGGER ******* WARN -->   DemoApplication  *** WARN ***");
		log.info("****** TEST LOGGER ******* INFO -->   DemoApplication  *** INFO {} ***", nameTest);
		log.debug("****** TEST LOGGER ******* DEBUG -->   DemoApplication  *** DEBUG {} ***", number);
		log.trace("****** TEST LOGGER ******* TRACE -->   DemoApplication  *** TRACE ***");
		log.info( nameTest );

		provider.handleApplicationContext();
	}
}

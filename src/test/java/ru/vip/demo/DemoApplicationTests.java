package ru.vip.demo;

//import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vip.demo.entity.ItemDirectory;
import ru.vip.demo.serviceimpl.EstimateImpl;
import ru.vip.demo.util.LoadDB;

import java.util.List;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Value("${names.test}")    // Передача еденичного параметра из application.yml
	String nameTest;

	@Value("${names.number}")   // Передача еденичного параметра из application.yml
	int number;
	/*
	* Date Test
	*/

	@Autowired
	public EstimateImpl repository;
	@Autowired
	public LoadDB loadDB;

	//@BeforeAll
	private void init() {
		loadDB.loadItemDirectory();

	}

	@Test
	public void dateTest() {

		init();

		List<ItemDirectory>	allItem = repository.getAll();

		for ( ItemDirectory item : allItem) {
			System.out.println(item.toString());
		}
	}

	/*
	 * Test ApplicationContextProvider
	 */

//
//	@Autowired
//	ApplicationContextProvider provider;
//
//	@Test
//	public void initTestProvider() {//} throws Throwable  { //}  throws InterruptedException {
//		for(int i = 0; i < 3; ++i) {
//			provider.handleApplicationContext();
//		}
//	}


}

package ru.vip.demo;

//import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vip.demo.entity.ItemDirectory;
import ru.vip.demo.serviceimpl.EstimateImpl;

import java.util.List;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Value("${names.item_directory}")    // Передача еденичного параметра из application.yml
	String item_directory;

	@Value("${names.number}")   // Передача еденичного параметра из application.yml
	int number;
	/*
	* Data Test
	*/

	@Autowired
	public EstimateImpl repository;

	//@BeforeAll
	private void init() throws Exception {

		String outFile = "src/main/resources/test_" + item_directory;

		List<ItemDirectory> itemDirectories = repository.getJSON(item_directory);
		for (ItemDirectory item : itemDirectories) {
			repository.save(item);
		}

		repository.writeJSON( outFile, itemDirectories); // запмсь  в файл
	}

	@Test
	public void dataTest()throws Exception {
		init();
		List<ItemDirectory>	allItemDirectory = repository.getAllItemDirectory();
		for ( ItemDirectory item : allItemDirectory) {
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

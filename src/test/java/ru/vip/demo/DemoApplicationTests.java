package ru.vip.demo;

//import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.common.collect.ImmutableList;
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
	private LoadDB loadDB;

	//@BeforeAll
	private void init() throws Exception {
//		loadDB.loadItemDirectory();

		ImmutableList<ItemDirectory> itemDirectories = loadDB.getItemDirectory();
		for (ItemDirectory item : itemDirectories) {
			repository.save(item);
		}
	}

	@Test
	public void dateTest()throws Exception {

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

package ru.vip.demo;

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

	@Value("${file_json.in_item_directory}")  // Передача параметра из application.yml
	String in_item_directory;
	@Value("${file_json.out_prefix}")         // Передача параметра из application.yml
	String out_prefix;

	@Autowired
	public EstimateImpl repository;

	private void init() throws Exception {
		List<ItemDirectory> itemDirectories = repository.readJSON(in_item_directory);// Чтение из JSON file в List
		for (ItemDirectory item : itemDirectories) {								 // Запись из List в базу данных
			repository.save(item);
		}
	}

	@Test
	public void dataTest()throws Exception {
		String outFile = out_prefix + in_item_directory;
		init();
		List<ItemDirectory>	lst = repository.getAllItemDirectory();					// Чтение из базы данных в List
		for ( ItemDirectory item : lst) {											// Вывод из List в System.out
			System.out.println(item.toString());
		}
		repository.writeJSON( outFile, lst); 										// Запмсь из List в JSON файл
	}

/* *****************************************************************
*            Test ApplicationContextProvider
*/
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

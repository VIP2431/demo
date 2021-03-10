package ru.vip.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vip.demo.entity.ItemDirectory;
import ru.vip.demo.entity.Node;
import ru.vip.demo.serviceimpl.EstimateImpl;

import java.util.List;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Value("${file_json.in_node}")  // Передача параметра из application.yml
	String in_node;

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

		List<Node> nodes = repository.readJsonNode(in_node);							// Чтение из JSON file в List
		for (Node node : nodes) {								// Запись из List в базу данных
			repository.save(node);
		}
	}

	@Test
	public void dataTest()throws Exception {
		String outFile = out_prefix + in_item_directory;
		init();
		List<ItemDirectory>	lst = repository.getAllItemDirectory();					// Чтение из базы данных в List
		repository.writeJSON( outFile, lst); 										// Запмсь из List в JSON файл
		for ( ItemDirectory item : lst) {											// Вывод из List в System.out
			System.out.println(item.toString());
		}


		outFile = out_prefix + in_node;
		System.out.println("outFile:" + outFile);

		List<Node> nodeList = repository.getAllNode();
//		repository.writeJsonNode( outFile, nodeList); 									// Запмсь из List в JSON файл
		for ( Node node : nodeList) {											    // Вывод из List в System.out
//			System.out.println(node.toString());
			System.out.println("id:" + node.getId() +
					" name:" + node.getName() +
					" status:" + node.getStatus() +
					" unit:" + node.getUnit() +
					" quantity:" + node.getQuantity() +
					" price:" + node.getPrice());
		}


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

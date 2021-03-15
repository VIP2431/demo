package ru.vip.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vip.demo.entity.Item;
import ru.vip.demo.entity.ItemDirectory;
import ru.vip.demo.entity.Node;
import ru.vip.demo.serviceimpl.EstimateImpl;
import ru.vip.demo.util.LoadDB;

import java.util.List;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Value("${file_json.in_item}")  // Передача параметра из application.yml
	String in_item;

	@Value("${file_json.in_node}")  // Передача параметра из application.yml
	String in_node;

	@Value("${file_json.in_builder}")  // Передача параметра из application.yml
	String in_builder;

	@Value("${file_json.in_item_directory}")  // Передача параметра из application.yml
	String in_item_directory;

	@Value("${file_json.out_prefix}")         // Передача параметра из application.yml
	String out_prefix;

	@Autowired
	public LoadDB loadDB;

	@Autowired
	public EstimateImpl repository;

////////////////////////////////////////////////////////////////////////////
//
//
	@Test
	public void dataTest()throws Exception {

		loadDB.itemAndItemDirectToDB(in_item_directory, in_item);

		List<ItemDirectory>	lst = repository.getAllItemDirectory();			// Чтение из базы данных в List
		repository.writeJsonItemDirectory( out_prefix + in_item_directory, lst); 	// Запмсь из List в JSON файл

		List<Item>	itemList = repository.getAllItem();						// Чтение из базы данных в List
		repository.writeJsonItem(out_prefix + in_item, itemList); 	// Запмсь из List в JSON файл

		List<Node> nodes = repository.readJsonNode(in_node);				// Чтение из JSON file в List
		for (Node node : nodes) {	repository.save(node);	}				// Запись из List в базу данных

		loadDB.builderToDB(in_builder, in_node);

		loadDB.writeNodeToJson(out_prefix + in_node);
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

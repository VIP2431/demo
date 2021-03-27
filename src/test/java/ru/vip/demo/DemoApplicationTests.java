package ru.vip.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vip.demo.entity.Item;
import ru.vip.demo.entity.ItemDirectory;
import ru.vip.demo.entity.MainBuilder;
import ru.vip.demo.entity.Node;
import ru.vip.demo.serviceimpl.EstimateImpl;
import ru.vip.demo.util.LoadDB;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Value("${file_json.init_node}") 				// Передача параметра из application.yml
	String init_node;

	@Value("${file_json.init_builder}") 		// Передача параметра из application.yml
	String init_builder;

	@Value("${file_json.in_node}") 				 	// Передача параметра из application.yml
	String in_node;

	@Value("${file_json.in_builder}") 		 	// Передача параметра из application.yml
	String in_builder;

	@Value("${file_json.in_item}")  				// Передача параметра из application.yml
	String in_item;

	@Value("${file_json.in_item_directory}")  		// Передача параметра из application.yml
	String in_item_directory;

	@Value("${file_json.prefix_}") 			 		// Передача параметра из application.yml
	String prefix_;

	@Value("${file_json.prefix_test}")         		// Передача параметра из application.yml
	String prefix_test;

	@Autowired
	public LoadDB loadDB;

	@Autowired
	public EstimateImpl repository;

////////////////////////////////////////////////////////////////////////////
//
//
	@Test
	public void dataTest()throws Exception {

		loadDB.deleteCommentsForJson(prefix_ + init_builder, prefix_ + in_builder);

		loadDB.deleteCommentsForJson(prefix_ + init_node, prefix_ + in_node);

		loadDB.itemAndItemDirectToDB(prefix_ + in_item_directory, prefix_ + in_item);

		List<ItemDirectory> dirsJson = repository.readJsonItemDirectory(prefix_ + in_item_directory);
		List<ItemDirectory> dirsBD = repository.getAllItemDirectory();
		itemDirectoryTest( dirsJson, dirsBD);

		repository.writeJsonItemDirectory( prefix_test + in_item_directory, dirsBD); 	// Запмсь из List в JSON файл
		List<ItemDirectory> test_dirsJson = repository.readJsonItemDirectory(prefix_test + in_item_directory);
		itemDirectoryTest( dirsJson, test_dirsJson);

		List<Item>	itemList = repository.getAllItem();						// Чтение из базы данных в List
		repository.writeJsonItem(prefix_test + in_item, itemList); 	// Запмсь из List в JSON файл

		List<Node> nodes = repository.readJsonNode(prefix_ + in_node);				// Чтение из JSON file в List
		for (Node node : nodes) {	repository.save(node);	}				// Запись из List в базу данных

		List<MainBuilder> builders = repository.readJsonBuilder(prefix_ + in_builder);
		repository.writeJsonBuilder(prefix_test + in_builder, builders);

		loadDB.builderToDB( builders);  // Запуск "builder.json" конструктора-инициатора

		loadDB.writeNodeToJson("Шереметьевская_1", prefix_ + "NEW_" + in_node);
	}


//////////////////////////////////////////////////////////////////////////////////////////////////
//
	public void itemDirectoryTest(List<ItemDirectory> dirsJson, List<ItemDirectory> dirsBD){

		int lengthID = repository.getIdNullUUID().length();

		for (ItemDirectory dJson : dirsJson) {
			String name = dJson.getCode();
			for (ItemDirectory dBD : dirsBD) {
				if(name.equals(dBD.getCode())) {
					String headMSG = "itemDirectory.code.\"" + name + "\"  field:\"";
					String idDB = dBD.getIdItemDirectory().toString();
					assertEquals(lengthID, idDB.length(), headMSG + "id\"");
					assertEquals(dJson.getCategory(), dBD.getCategory(),  headMSG + "Category\"");
					assertEquals(dJson.getCode(),dBD.getCode(), headMSG + "Code\"");
					assertEquals(dJson.getTitle(),dBD.getTitle(), headMSG + "Title\"");
					assertEquals(dJson.getUnit(),dBD.getUnit(), headMSG + "Unit\"");
					assertEquals(dJson.getPrice(),dBD.getPrice(), headMSG + "Price\"");
					assertEquals(dJson.getVendor(),dBD.getVendor(), headMSG + "Vendor\"");
				}
			}
		}
	}
}


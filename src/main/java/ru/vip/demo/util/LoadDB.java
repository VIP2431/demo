package ru.vip.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.entity.EstimateBuilder;
import ru.vip.demo.entity.Item;
import ru.vip.demo.entity.ItemDirectory;
import ru.vip.demo.entity.Node;
import ru.vip.demo.serviceimpl.EstimateImpl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LoadDB {

	public final EstimateImpl repository;

//////////////////////////////////////////////////////////////////////////////////////////////////
//
	public void writeNodeToJson(String out_node){

		ObjectMapper mapper = repository.getMapper();
		List<Node> nodeList = repository.getAllNode();

		try (PrintWriter outFile = new PrintWriter(out_node, StandardCharsets.UTF_8)){
			int n = 0;
			outFile.print("[");
			for (Node node : nodeList) {
				if (n++ > 0) { outFile.print(","); }
				outFile.print(" {" +
						"\n    \"id\" : \"" + node.getId() + "\"," +
						"\n    \"name\" : \"" + node.getName() + "\"," +
						"\n    \"title\" : \"" + node.getTitle() + "\"," +
						"\n    \"status\" : \"" + node.getStatus() + "\"," +
						"\n    \"unit\" : \"" + node.getUnit() + "\"," +
						"\n    \"quantity\" : " + node.getQuantity() + "," +
						"\n    \"price\" : " + node.getPrice() + ",");

				List<Node> nodes = node.getNodes();
				if(nodes.isEmpty()) { outFile.print("\n    \"nodes\" : [],");}
				else{outFile.print("\n    \"nodes\" :\n" + mapper.writeValueAsString( nodes));}

				List<Item> items = node.getItems();
				if (items.isEmpty()) { outFile.print("\n    \"items\" : []"); }
				else { outFile.print("\n    \"items\" :" + mapper.writeValueAsString( items)); }
				outFile.print("\n}");
			}
			outFile.println(" ]");
		} catch (IOException e) {
			System.out.println("Ошибка сериализации \"Node\" в файл Json:" + e);
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////
//
	public void itemAndItemDirectToDB(String in_item_directory, String in_item) throws Exception {

		List<ItemDirectory> itemDirectories = repository.readJsonItemDirectory(in_item_directory);

		for (ItemDirectory item : itemDirectories){ repository.save(item);} // Запись из List в базу данных

		List<Item> items = repository.readJsonItem(in_item);                // Чтение из JSON file в List
		List<ItemDirectory>	itemDirs = repository.getAllItemDirectory();	// Чтение из базы данных в List
		for (Item item : items) {
			String name = item.getCode();
			for (ItemDirectory dir : itemDirs) {
				if(name.equals(dir.getCode())) {
					item.setIdItemDirectory(dir.getIdItemDirectory());
					item.setCategory(dir.getCategory());
					item.setCode(dir.getCode());
					item.setTitle(dir.getTitle());
					item.setUnit(dir.getUnit());
					item.setPrice(dir.getPrice());
					item.setVendor(dir.getVendor());

					repository.save(item);									// Запись из List в базу данных
				}
			}
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////
//
	public void builderToDB(String in_builder ) throws Exception {

		List<EstimateBuilder> builders = repository.readJsonBuilder(in_builder);// Чтение из JSON file в List
		List<Item> itemList = repository.getAllItem();	// Чтение из базы данных в List
		List<Node> nodeList = repository.getAllNode();
		List<Node> nodeList1 = repository.getAllNode();
		String nullUUID = repository.getIdNullUUID();

		for (EstimateBuilder builder : builders) {		// взять следующей шаблон из заданного в "builder.json" списка
			Node newNode;
			String nameNode = builder.getNameNode(); 	// взять nameNode из выбранного "builder"
			for (Node node : nodeList) {				// просмотр списка Node взятого из ВД
				if(nameNode.equals(node.getName())) {	// Node с заданным именем найден
					Node cloneNode = node.clone();// Клонировать  Node
					cloneNode.setName(builder.getNewName());
					cloneNode.setTitle(builder.getTitleNode());
//
//					System.out.println(" -1.1- cloneNode.getName()" + cloneNode.getName() + "\" cloneNode.id: \"" + cloneNode.getId() + "\"");
//					System.out.println(" -1.2- cloneNode.name: \"" + cloneNode.getName() + "\"   cloneNode.getNodes().isEmpty(): \"" + cloneNode.getNodes().isEmpty() + "\"");
//					System.out.println(" -1.3- cloneNode.name: \"" + cloneNode.getName() + "\"   cloneNode.getItems().isEmpty(): \"" + cloneNode.getItems().isEmpty() + "\"");

					boolean flagItems = cloneNode.getItems().isEmpty(); //  ?????????????????????????????????????????
					boolean flagNodes = cloneNode.getNodes().isEmpty();	//  ?????????????????????????????????????????
					cloneNode.setId(UUID.fromString(nullUUID));

					// Записать созданный Node в БД и сохранить на нег ссылку
					newNode = repository.save(cloneNode);
//					System.out.println(" -3.2- newNode.name: \"" + newNode.getName() + "\"   newNode.id: \"" + newNode.getId() + "\"");
//					System.out.println(" -3.3- newNode.name: \"" + newNode.getName() + "\"   newNode.getNodes().isEmpty(): \"" + newNode.getNodes().isEmpty() + "\"");
//					System.out.println(" -3.4- newNode.name: \"" + newNode.getName() + "\"   newNode.getItems().isEmpty(): \"" + newNode.getItems().isEmpty() + "\"");
//

					List<String> listNameNode = builder.getNodes();
					if(!listNameNode.isEmpty()) {
						for (String str : listNameNode) {
							for (Node node1 : nodeList1) {              // ??? nodeList head for
								if (str.equals(node1.getName())) {
									node1.setId(UUID.fromString(nullUUID));
									node1.getNodes().add(repository.save(node1));
								}
							}
						}
					}
					List<String> listNameItem = builder.getItems();
					if(!listNameItem.isEmpty()) {
						for (String nameItem : listNameItem) { 	// взять следующее имя из заданного в "builder" списка
							for (Item item : itemList) {	// взять следующее "item" из списка взятого в БД
								if (nameItem.equals(item.getName())) { // Item с заданным именем найден
//										System.out.println(" -5.1- nameItem: \"" + nameItem + "\" item.getName():\"" + item.getName() + "\"");
									Item cloneItem = item.clone();
									cloneItem.setId(UUID.fromString(nullUUID));
									Item newItem = repository.save(cloneItem);
//										System.out.println(" -5.3.1- nameItem: \"" + nameItem + "\" item.getName():\"" + item.getName() + "\" item.id: \"" + item.getId() + "\"");
//										System.out.println(" -5.3.2- nameItem: \"" + nameItem + "\" newItem.getName():\"" + newItem.getName() + "\" newItem.id: \"" + newItem.getId() +  "\"");
//
//										System.out.println(" -5.3.3- newNode.name: \"" + newNode.getName() + "\" newNode.id: \"" + newNode.getId() + "\"");
//										System.out.println(" -5.3.4- newNode.name: \"" + newNode.getName() + "\"   newNode.getItems().isEmpty(): \"" + newNode.getItems().isEmpty() + "\"");
									newNode.getItems().add(newItem);
//										System.out.println(" -5.5.1- newNode.name: \"" + newNode.getName() + "\"   newNode.getItems().isEmpty(): \"" + newNode.getItems().isEmpty() + "\"");
//										System.out.println(" -5.5.2- newItem.getName: \"" + newItem.getName() + "\"   newItem.id: \"" + newItem.getId() + "\"");
//										System.out.println(" -5.5.3- newNode.name: \"" + newNode.getName() + "\"");
									break;
								}
							}
						}
					}
					repository.save(newNode);
					break;
				}
			}
		}
	}

// ///////////////////////////////////////////////////////////////////////////////////////////////
//        JSON Десериализация нескольких объектов с применением "Jackson" и "com.google.guava"
//
//	public static ImmutableList<ItemDirectory> getItemDirectory() throws IOException {
//		// JSON Десериализация нескольких объектов
//		ObjectMapper mapper = new ObjectMapper();
//		InputStream inputStream = Resources.getResource("item_directory.json").openStream();
//		List<ItemDirectory> items = mapper.readValue(inputStream, new TypeReference<List<ItemDirectory>>() {});
//		return ImmutableList.copyOf(items);
//	}

// ///////////////////////////////////////////////////////////////////////////////////////////////
//    Сериализация и десериализация JSON
//
//	public void writeJsonItemDirectory(ItemDirectory itemDirectory) throws Exception {
//		// JSON Сериализация объекта
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			mapper.writeValue( new File("src/test/resources/data/item_directory.json"), itemDirectory);
//		} catch (IOException e) {
//			System.out.println("Исключение при сериализации: " + e);
//		}
//	}

//	public void readJsonItemDirectory() throws Exception {
//		// JSON Десериализация объекта
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//				ItemDirectory itemDirectory1 =
//						mapper.readValue(new File("src/test/resources/data/item_directory.json"), ItemDirectory.class);
//				System.out.println("\n +++++ JSON После чтения из файла --> itemDirectory1:\n" + itemDirectory1);
//		} catch (IOException e) {
//			System.out.println("Исключение при десериализации: " + e);
//		}
//		System.out.println(" ");
//	}

// ///////////////////////////////////////////////////////////////////////////////////////////////
//    Сериализация и десериализация стандартная библиотека
//
//	public void serialItemDirectory(ItemDirectory itemDirectory)throws Exception {
//
//		System.out.println("\n >>>>> Перед записью в файл --> itemDirectory:\n" + itemDirectory);
//
//		// Сериализация объекта
//		try {
//			ObjectOutputStream objectOutputStream = new ObjectOutputStream( new FileOutputStream("itemDirectory"));
//			objectOutputStream.writeObject( itemDirectory);
//		} catch (IOException e) {
//			System.out.println("Исключение при сериализации: " + e);
//		}
//
//		// Десериализация объекта
//		try {
//			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("itemDirectory"));
//			ItemDirectory itemDirectory1 = (ItemDirectory) objectInputStream.readObject();
//
//			System.out.println(" +++++ После чтения из файла --> itemDirectory1:\n" + itemDirectory1 + "\n");
//		} catch (IOException e) {
//			System.out.println("Исключение при десериализации: " + e);
//		}
//	}
}


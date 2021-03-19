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

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LoadDB {

	public final EstimateImpl repository;

	public void deleteCommentsForJson(String in_nameFile, String out_nameFile) {

		try ( FileReader inFile = new FileReader( in_nameFile, StandardCharsets.UTF_8);
			  FileWriter outFile = new FileWriter(out_nameFile, StandardCharsets.UTF_8);
				PushbackReader f = new PushbackReader( inFile);){

			int c, ch;
			while ( (c = f.read()) != -1) {
				if(c == '/') {
					if( (ch = f.read()) == -1) { return; }
					if( ch == '/') {
						while ((c = f.read()) != -1 & c >= ' ');
						if(c == -1) { return; }
						outFile.write( c);
						while ((c = f.read()) != -1 & c < ' ') {
							outFile.write( c);
						}
						if(c == -1) { return; }
						f.unread(c);
					} else {
						outFile.write( c);
						outFile.write( ch);
					}
				}else {
					outFile.write( c);
				}
			}
		} catch (IOException e) {
			System.out.println("Ошибка удаления комментариев из Json inFile/outFile:\"" + in_nameFile + "\"/\"" + out_nameFile +   "\"   " + e);
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////
//
	public void writeNodeToJson(String out_node) {

		ObjectMapper mapper = repository.getMapper();
		List<Node> nodeList = repository.getAllNode();

		try (PrintWriter outFile = new PrintWriter(out_node, StandardCharsets.UTF_8)) {
			int n = 0;
			outFile.print("[\n    ");
			for (Node node : nodeList) {
				if (n++ > 0) {
					outFile.print(",\n  ");
				}
				outFile.print(" { // *** Блок node: \"" + node.getStatus().getName() + "\" -> \"" + node.getName() + "\" ***" +
						"\n    \"id\" : \"" + node.getId() + "\"," +
						"\n    \"name\" : \"" + node.getName() + "\"," +
						"\n    \"title\" : \"" + node.getTitle() + "\"," +
						"\n    \"status\" : \"" + node.getStatus() + "\"," + "  //  \"" + node.getStatus().getName() + "\"" +
						"\n    \"unit\" : \"" + node.getUnit() + "\"," +
						"\n    \"quantity\" : " + node.getQuantity() + "," +
						"\n    \"price\" : " + node.getPrice() + ",");

				List<Node> nodes = node.getNodes();
				if (nodes.isEmpty()) {
					outFile.print("\n    \"nodes\" : [],");
				} else {
					outFile.print("\n    \"nodes\" :\n" + mapper.writeValueAsString(nodes));
					outFile.print("\n    \"nodes\" :  // [ Начало списока nodes\n");
					outFile.print(mapper.writeValueAsString(nodes));
					outFile.print("  // ] Конец списка nodes \n}");
				}

				List<Item> items = node.getItems();
				if (items.isEmpty()) {
					outFile.print("\n    \"items\" : []\n}");
				} else {
					outFile.print("\n    \"items\" :  // [ Начало списока items\n");
					outFile.print(mapper.writeValueAsString(items));
					outFile.print("  // ] Конец списка items \n}");
				}


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

		for (ItemDirectory item : itemDirectories) {
			repository.save(item);
		} // Запись из List в базу данных

		List<Item> items = repository.readJsonItem(in_item);                // Чтение из JSON file в List
		List<ItemDirectory> itemDirs = repository.getAllItemDirectory();    // Чтение из базы данных в List
		for (Item item : items) {
			String name = item.getCode();
			for (ItemDirectory dir : itemDirs) {
				if (name.equals(dir.getCode())) {
					item.setIdItemDirectory(dir.getIdItemDirectory());
					item.setCategory(dir.getCategory());
					item.setCode(dir.getCode());
					item.setTitle(dir.getTitle());
					item.setUnit(dir.getUnit());
					item.setPrice(dir.getPrice());
					item.setVendor(dir.getVendor());

					repository.save(item);                                    // Запись из List в базу данных
				}
			}
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////
//
	private void addNodeToNodeNodes( Node newNode, List<Node> allNodeDB, List<String> listNameNode) throws Exception  {

		if(listNameNode.isEmpty()) { return; }
		for (String str : listNameNode) {
			for (Node node : allNodeDB) {              // ??? nodeList head for
				if (str.equals(node.getName())) {
					Node cloneNode = node.clone();
					cloneNode.setId(repository.getUuidNull());
					newNode.getNodes().add(repository.save(cloneNode));
					break;
				}
			}
		}
	}

	private void addItemsToNodeItems( Node newNode, List<String> listNameItem) throws Exception {

		if(listNameItem.isEmpty()) { return; }

		List<Item> itemList = repository.getAllItem();	// Чтение из базы данных в List

		for (String nameItem : listNameItem) { 	// взять следующее имя из заданного в "builder" списка
			for (Item item : itemList) {	// взять следующее "item" из списка взятого в БД
				if (nameItem.equals(item.getName())) { // Item с заданным именем найден
					Item cloneItem = item.clone();
					cloneItem.setId(repository.getUuidNull());
					Item newItem = repository.save(cloneItem);
					newNode.getItems().add(newItem);
					break;
				}
			}
		}
	}

	public void builderToDB(String in_builder ) throws Exception {

		List<EstimateBuilder> builders = repository.readJsonBuilder(in_builder);// Чтение из JSON file в List
		List<Node> allNodeDB = repository.getAllNode();

		for (EstimateBuilder builder : builders) {		// взять следующей шаблон из заданного в "builder.json" списка
			Node newNode;
			String nameNode = builder.getNameNode(); 	// взять nameNode из выбранного "builder"
			for (Node node : allNodeDB) {				// просмотр списка Node взятого из ВД
				if(nameNode.equals(node.getName())) {	// Node с заданным именем найден
					Node cloneNode = node.clone();// Клонировать  Node
					cloneNode.setName(builder.getNewName());
					cloneNode.setTitle(builder.getTitleNode());

					boolean flagItems = cloneNode.getItems().isEmpty(); //  ?????????????????????????????????????????
					boolean flagNodes = cloneNode.getNodes().isEmpty();	//  ?????????????????????????????????????????

					cloneNode.setId(repository.getUuidNull());
					newNode = repository.save(cloneNode);
					addNodeToNodeNodes( newNode, allNodeDB, builder.getNodes());
					addItemsToNodeItems( newNode, builder.getItems());
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


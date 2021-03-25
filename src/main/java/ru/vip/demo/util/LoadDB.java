package ru.vip.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.entity.Item;
import ru.vip.demo.entity.ItemDirectory;
import ru.vip.demo.entity.MainBuilder;
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

//////////////////////////////////////////////////////////////////////////////////////////////////
//
	public void deleteCommentsForJson(String in_nameFile, String out_nameFile) {

//		System.out.println("[deleteCommentsForJson]-->in_file:\"" + in_nameFile + "                   out_file:\"" + out_nameFile + "\"");

		try ( FileReader inFile = new FileReader( in_nameFile, StandardCharsets.UTF_8);
			  PushbackReader f = new PushbackReader( inFile);
			  FileWriter outFile = new FileWriter(out_nameFile, StandardCharsets.UTF_8)) {

			int c, ch;
			while ( (c = f.read()) != -1) {
				if(c == '/') {
					if( (ch = f.read()) == -1) { return; }
					if( ch == '/') {
						while ((c = f.read()) != -1 & c >= ' ');
						if(c == -1) { return; }
						outFile.write( c);
						while ((c = f.read()) != -1 & c < ' ') { outFile.write( c); }
						if(c == -1) { return; }
						f.unread(c);
					}
					else {
						if( ch == '*'){
							do {
								while ((c = f.read()) != -1 & c != '*');
								if (c == -1) { return; }
								if ((c = f.read()) != -1 & c == '/') { break; }
								if (c == -1) { return; }
								if(c == '*') { f.unread(c);  }
							}while (true);
						}
						else {
							outFile.write(c);
							outFile.write(ch);
						}
					}
				}
				else {
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
		Node node = null;
		for ( Node nd : nodeList) {
			if (nd.getName().equals( "Шереметьевская_1")) { node = nd; break; }
		}

		try (PrintWriter outFile = new PrintWriter(out_node, StandardCharsets.UTF_8)) {
			int n = 0;
			outFile.print("[  // [ Начало файла\n");
//			for (Node node : nodeList) {
			if(node != null) {
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

				List<Item> items = node.getItems();
				if (items.isEmpty()) {
					outFile.print("\n    \"items\" : [],\n");
				} else {
					outFile.print("\n    \"items\" :  // [ Начало списока позиций\n");
					outFile.print(mapper.writeValueAsString(items));
					outFile.print(",  // ] Конец списка позиций \n");
				}

				List<Node> nodes = node.getNodes();
				if (nodes.isEmpty()) {
					outFile.print("\n    \"nodes\" : []\n}");
				} else {
					outFile.print("\n    \"nodes\" :  // [ Начало списока (комнат/блоков работ)\n");
					outFile.print(mapper.writeValueAsString(nodes));
					outFile.print(" // ] Конец списка (комнат/блоков работ)\n}");
				}
			}
			outFile.println("\n ]// ] Конец файла");
		} catch (IOException e) {
			System.out.println("Ошибка сериализации \"Node\" в файл Json:" + e);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
//
	public void itemAndItemDirectToDB(String in_item_directory, String in_item) throws Exception {

		List<ItemDirectory> itemDirectories = repository.readJsonItemDirectory(in_item_directory);

		for (ItemDirectory item : itemDirectories) { repository.save(item); } // Запись из List в базу данных

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

//////////////////////////////////////////////////////////////////////////////////////////////////////////
//
	private void newItemAddNode( Item item, Node dNode) throws Exception {

		Item cloneItem = item.clone();
		cloneItem.setId(repository.getUuidNull());
	//	System.out.println(" -2.2-  + <newItemAddNode> + \n" + repository.getMapper().writeValueAsString(cloneItem));

		try {
			Item cloneItem1 = repository.save(cloneItem);
			dNode.getItems().add(cloneItem1);
//				System.out.println(" -2.5-  item:\"" + cloneItem1.getName() + "\"  id=\"" + cloneItem1.getId() + "\"");

		}catch (NullPointerException e) {
			System.out.println(" -2.8- ***** Error -->nameNod:\"" +dNode.getName() + "\"  Ex:" + "\"" + e.getMessage() + "\" *****");
		}catch (IndexOutOfBoundsException ex) {
			System.out.println(" -2.9- ***** Error -->nameNod:\"" +dNode.getName() + "\"  ExIndx:" + "\"" + ex + "\" *****");
		}
	}

	private void itemListAddNode( List<String> listNameItem, List<Item> allItemDB, Node dNode) throws Exception {
//		System.out.println(" -2- +++++ <itemListAddNode> +++++");

		for (String nameItem : listNameItem) { 			// взять следующее имя из заданного в "builder" списка
			for (Item item : allItemDB) {				// взять следующее "item" из списка взятого в БД
				if (nameItem.equals(item.getName())) { 	// Item с заданным именем найден
					newItemAddNode( item, dNode);
					break;
				}
			}
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////
//
	private void newNodeAddNode( Node node, Node newNode) throws Exception  {

//		String name = node.getName();
//		System.out.println(" -3.1- node:\"" + name + "\" ++ <nodeAddNode> ++");

		try {
			List<Item> items = node.getItems();

			Node cloneNode = node.clone();
			cloneNode.setId(repository.getUuidNull());
			Node cloneNode1 = repository.save(cloneNode);
			cloneNode1.getItems().clear();
			if (!items.isEmpty()) {
				for (Item item : items) {
					newItemAddNode( item, cloneNode1);
				}
			}
			cloneNode = repository.save(cloneNode1);
			newNode.getNodes().add(repository.save( cloneNode));

//			System.out.println(repository.getMapper().writeValueAsString(cloneNode));
//
//			if (name.equals("Демонтаж_1")) {
//				System.out.println("\n -3.3- +++  \"" + name + "\"");
//				System.out.println(repository.getMapper().writeValueAsString(newNode));
//			}
		}catch (IndexOutOfBoundsException ex) {
			System.out.println(" -3.9- ***** Error -->nameNod:\"" + newNode.getName() + "\"  Ex:" + "\"" + ex + "\" *****");
		}
	}

	private void nodeListAddNode( List<String> listNameNode, Node newNode) throws Exception  {

//		System.out.println(" -3- +++++ <nodeListAddNode> +++++");

		List<Node> allNodeDB = repository.getAllNode();

		for (String nameNode : listNameNode) {
			for (Node node : allNodeDB) {              // ??? nodeList head for
				if ( nameNode.equals(node.getName())) {
					newNodeAddNode( node, newNode);
					break;
				}
			}
		}
	}

	public void builderToDB( List<MainBuilder> builders ) throws Exception {

//		ObjectMapper mapper = new ObjectMapper();
//		StringBuilder buff = new StringBuilder(2000);

		List<Item> allItemDB = repository.getAllItem();
		List<Node> allNodeDB = repository.getAllNode();

		for (MainBuilder builder : builders) {
			Node newNode;
			String nameNode = builder.getNameNode();
			for (Node node : allNodeDB) {
				if(nameNode.equals(node.getName())) {
//					System.out.println("\n ===   builderToDB 1 ===============>\"" + nameNode + "\"      id=\"" + node.getId() + "\"");
					Node cloneNode = node.clone();						// Клонировать  Node
					cloneNode.setName(builder.getNewName());
					cloneNode.setTitle(builder.getTitleNode());

					boolean flagItems = cloneNode.getItems().isEmpty(); //  ?????????????????????????????????????????
					boolean flagNodes = cloneNode.getNodes().isEmpty();	//  ?????????????????????????????????????????

					cloneNode.setId(repository.getUuidNull());
					newNode = repository.save(cloneNode);

					List<String> listNameItem = builder.getItems();
					if(!listNameItem.isEmpty()) {
						itemListAddNode( listNameItem, allItemDB, newNode);
					}

					List<String> listNameNode = builder.getNodes();
					if(!listNameNode.isEmpty()) {
						nodeListAddNode( listNameNode, newNode);
					}

					repository.save(newNode);

//					String name = newNode.getName();
//					if(newNode.getStatus() != Status.STAT_HOUSE) {
//						System.out.print("\n  ===   builderToDB	5 -->\"" + name + "\"               id=\"" + newNode.getId() + "\"");
//						int n = newNode.getNodes().size();
//						int i = newNode.getItems().size();
//						Item itm = newNode.getItems().get(i - 1);
//						System.out.print(" nodes:items=[" + n + ":" + i + "]   items[i].getName():" + itm.getName());
//
//						if (name.equals("Демонтаж_1")) {
//							System.out.println("\n" + repository.getMapper().writeValueAsString(newNode));
//						}
//					}
//					else {
//						System.out.println("  === 	***** Объект: Дом-Квартира:\"" + name + "\"   id=\"" + newNode.getId() + "\"");
//					}
////					System.out.println(repository.getMapper().writeValueAsString(newNode));
//					System.out.println(" ");
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


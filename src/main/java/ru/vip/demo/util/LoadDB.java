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
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LoadDB {

	static private int recCount = 0;
	static private final int recMax   = 15;

	public final EstimateImpl repository;

	private ObjectMapper map = new ObjectMapper();

//////////////////////////////////////////////////////////////////////////////////////////////////
//
	public void deleteCommentsForJson(String in_nameFile, String out_nameFile) {

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
//////////////////////////////////////////////////////////////////////////////////////////////////
//
	public void writeNodeToJson(String nameNode, String out_json){

		ObjectMapper mapper = repository.getMapper();
		List<Node> nodeList = repository.getAllNode();
		Node node = null;
		for ( Node nd : nodeList) {
			if (nd.getName().equals( nameNode)) { node = nd; break; }
		}
		if(node == null) return;

		try (PrintWriter outFile = new PrintWriter(out_json, StandardCharsets.UTF_8)) {
			 outFile.print(mapper.writeValueAsString(node));
		} catch (IOException e) {
			System.out.println("Ошибка сериализации в файл Json:" + e);
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
		try {
			Item cloneItem1 = repository.save(cloneItem);
			dNode.getItems().add(cloneItem1);
		}catch (NullPointerException e) {
			System.out.println(" -2.8- ***** Error -->nameNod:\"" +dNode.getName() + "\"  Ex:" + "\"" + e.getMessage() + "\" *****");
		}catch (IndexOutOfBoundsException ex) {
			System.out.println(" -2.9- ***** Error -->nameNod:\"" +dNode.getName() + "\"  ExInd:" + "\"" + ex + "\" *****");
		}
	}

	private void itemListAddNode( List<String> listNameItem, List<Item> allItemDB, Node dNode) throws Exception {
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
	private Node newNodeAddNode( Node node, Node newNode) throws Exception  {

		try {
			List<Item> items = node.getItems();

			Node cloneNode = node.clone();
			cloneNode.setId(repository.getUuidNull());
			Node cloneNode1 = repository.save(cloneNode);

			cloneNode1.getItems().clear();
			if (!items.isEmpty()) {
				for (Item item : items) { newItemAddNode( item, cloneNode1); }
			}
			cloneNode = repository.save(cloneNode1);
			newNode.getNodes().add(repository.save( cloneNode));
			return cloneNode;

		}catch (IndexOutOfBoundsException ex) {
			System.out.println(" -3.9- ***** Error -->nameNod:\"" + newNode.getName() + "\"  Ex:" + "\"" + ex + "\" *****");
		}
		return null;
	}
/////////////////////////////////////////////////////////////////////////////////////////////
//
	private void nodesAddNode( Node subNode, List<Node> allNodeDB) throws Exception  {

		if(subNode == null) { return; }
		List<Node> nodes1 = subNode.getNodes();
		if(nodes1.isEmpty() || recCount > recMax) { return; }

		List<Node> nodes = nodes1.stream().collect(Collectors.toList());

		subNode.getNodes().clear();
		++recCount;
		for(Node node : nodes) {
			Node cloneNode = newNodeAddNode( node, subNode);
			nodesAddNode( cloneNode , allNodeDB);		// Рекурсия. "recCount" глубина Рекурсии
		}
	}
	private void nodeListAddNode( List<String> listNameNode, Node newNode) throws Exception  {
		List<Node> allNodeDB = repository.getAllNode();

		recCount = 0;
		for (String nameNode : listNameNode) {
			for (Node node : allNodeDB) {
				if ( nameNode.equals(node.getName())) {
					Node subNode = newNodeAddNode( node, newNode);
					nodesAddNode(subNode, allNodeDB);
					break;
				}
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////////
//
	public void builderToDB( List<MainBuilder> builders ) throws Exception {

		List<Item> allItemDB = repository.getAllItem();
		List<Node> allNodeDB = repository.getAllNode();

		for (MainBuilder builder : builders) {
			Node newNode;
			String nameNode = builder.getNameNode();
			for (Node node : allNodeDB) {
				if(!nameNode.equals(node.getName())) { continue; }

				Node cloneNode = node.clone();						// Клонировать  Node
				cloneNode.setName(builder.getNewName());
				cloneNode.setTitle(builder.getTitleNode());

				boolean flagItems = cloneNode.getItems().isEmpty(); //  ?????????????????????????????????????????
				boolean flagNodes = cloneNode.getNodes().isEmpty();	//  ?????????????????????????????????????????

				cloneNode.setId(repository.getUuidNull());
				newNode = repository.save(cloneNode);

				List<String> listNameItem = builder.getItems();
				if(!listNameItem.isEmpty()) { itemListAddNode( listNameItem, allItemDB, newNode); }

				List<String> listNameNode = builder.getNodes();
				if(!listNameNode.isEmpty()) { nodeListAddNode( listNameNode, newNode); }
				repository.save(newNode);
				break;
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


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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LoadDB {

	static private final String sTab = "    ";
	static private  int nTab = 0;

	static private int recCount = 0;
	static private final int recMax = 9;

	public final EstimateImpl repository;

	private ObjectMapper map = new ObjectMapper();
	//private Object StringBuilder;

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
	private void printItems(List<Item> items, int nTab) {
		if(!items.isEmpty()) {
			StringBuilder tab = new StringBuilder(80);
			nTab = nTab + 2;
			for (int i = 0; i++ <= nTab;) {tab.append(sTab); }
			for (Item item : items) {
				System.out.println(tab + item.getName() + " id=\"" + item.getId() + "\"");
			}
		}
	}

	private void printHeadNode(Node node, int nTab) {
		StringBuilder tab = new StringBuilder(80);
		for (int i = 0; i++ <= nTab;) { tab.append(sTab); }
		System.out.println(tab + "<" + node.getStatus().getName() + ">  \"" + node.getName() + "\"   id=\"" + node.getId() + "\"");
	}

	private void printNode(Node srcNode, int nTab) {
		printHeadNode(srcNode, nTab);
		printItems(srcNode.getItems(), nTab);
	}

	private void printTreeNodes(Node srcNode){
		List<Node> nodes = srcNode.getNodes();
		if(!nodes.isEmpty()) {
			for (Node node : nodes) {
				printNode(node, nTab++);
				printTreeNodes(node);
				--nTab;
			}
		}
	}

	private void printNodes(Node srcNode){
		printNode( srcNode, nTab++);
		printTreeNodes(srcNode);
	}

//////////////////////////////////////////////////////////////////////////////////////////////////
//
	public void writeNodeToJson(String nameNode, String out_json) throws Exception  {

		List<Node> nodes = repository.getAllNode();

		try (PrintWriter outFile = new PrintWriter(out_json, StandardCharsets.UTF_8)) {
			for (Node node : nodes) {
				if (node.getName().equals(nameNode)) {
					UUID idSrc = node.getId();
					outFile.print( repository.getMapper().writeValueAsString(node));
					UUID id = cloneNodeForId( idSrc);
					Optional<Node> optional = repository.findByIdNode(id);
					if(optional.isPresent()) {
						Node cloneNode = optional.get();
						outFile.print( repository.getMapper().writeValueAsString(cloneNode));
//						System.out.println("--<writeNodeToJson>-- kodNode:[" + node.hashCode() +
//								        "]\n                 KodCloneNode:[" + cloneNode.hashCode() + "]");
						printNodes(cloneNode);
					}
				}
			}
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
	private Node cloneNodeToDB(Node node) throws Exception {
		Node cloneNode = node.clone();
		cloneNode.setId(repository.getUuidNull());
		Node saveNode = repository.save(cloneNode);
		return saveNode;
	}

	private Item cloneItemToDB( Item item) throws Exception {
		Item cloneItem = item.clone();
		cloneItem.setId(repository.getUuidNull());
		Item saveItem = repository.save(cloneItem);
		return saveItem;
	}

	private void cloneItemAddNode( Item item, Node dNode) throws Exception {
		Item cloneItem = cloneItemToDB(item);
		dNode.getItems().add(cloneItem);
	}

	private Node cloneNodeAddNode( Node node, Node newNode) throws Exception  {
		Node cloneNode = cloneNodeToDB(node);
		cloneListItemAddNode( cloneNode);
		Node saveNode = repository.save(cloneNode);
		newNode.getNodes().add(repository.save( saveNode));
		return saveNode;
	}

	private void cloneListItemAddNode (Node cloneNode) throws Exception {
		List<Item> items = cloneNode.getItems().stream().collect(Collectors.toList());
		cloneNode.getItems().clear();
		if (!items.isEmpty()) {
			for (Item item : items) {
				cloneItemAddNode(item, cloneNode);
			}
		}
	}

	private void cloneListNodeAddNode( Node subNode) throws Exception  {

		if(subNode == null) { return; }
		List<Node> nodes1 = subNode.getNodes();
		if(nodes1.isEmpty()) { return; }
		if( recCount > recMax) {
			System.out.println("** <nodesAddNode> node: \"" + subNode.getName() + "\""
					+ " Превышена глубина рекурсии [recCount>=recMax]:" + recCount + ">=" + recMax);
			return;
		}

		List<Node> nodes = nodes1.stream().collect(Collectors.toList());

		subNode.getNodes().clear();
		++recCount;
		for(Node node : nodes) {
			Node cloneNode = cloneNodeAddNode( node, subNode);
			cloneListNodeAddNode( cloneNode );	// Рекурсия. "recCount" глубина Рекурсии
		}
	}

/////////////////////////////////////////////////////////////////////////////////////////////
//
	private void cloneNodesAddNode( Node cloneNode) throws Exception  {

		if(cloneNode == null) { return; }
		List<Node> cloneNodes = cloneNode.getNodes();
		if(cloneNodes.isEmpty()) { return; }
		if( recCount > recMax) {
			System.out.println("** <nodesAddNode> node: \"" + cloneNode.getName() + "\""
					+ " Превышена глубина рекурсии [recCount>=recMax]:" + recCount + ">=" + recMax);
			return;
		}

		List<Node> nodes = cloneNodes.stream().collect(Collectors.toList());

		cloneNode.getNodes().clear();
		++recCount;
		for(Node node : nodes) {
			Node newNode = cloneNodeAddNode( node, cloneNode);
			cloneNodesAddNode( newNode);		// Рекурсия. "recCount" глубина Рекурсии
		}
	}


	private UUID cloneNodeForId( UUID idNode) throws Exception  {

		UUID id = repository.getUuidNull();
		Optional<Node> optional = repository.findByIdNode(idNode);
		if(optional.isPresent()) {
			Node node = optional.get();
			Node cloneNode = cloneNodeToDB(node);
			id = cloneNode.getId();
			cloneNodesAddNode( cloneNode);
		}
		return id;
	}

/////////////////////////////////////////////////////////////////////////////////////////////
//
	private void builderItemListAddNode( List<String> listNameItem, List<Item> allItemDB, Node dNode) throws Exception {
		for (String nameItem : listNameItem) { 			// взять следующее имя из заданного в "builder" списка
			for (Item item : allItemDB) {				// взять следующее "item" из списка взятого в БД
				if (nameItem.equals(item.getName())) { 	// Item с заданным именем найден
					cloneItemAddNode( item, dNode);
					break;
				}
			}
		}
	}

	private void builderNodeListAddNode( List<String> listNameNode, Node newNode) throws Exception  {
		List<Node> allNodeDB = repository.getAllNode();

		recCount = 0;
		for (String nameNode : listNameNode) {
			for (Node node : allNodeDB) {
				if ( nameNode.equals(node.getName())) {
					Node subNode = cloneNodeAddNode( node, newNode);
					cloneListNodeAddNode(subNode);
					break;
				}
			}
		}
	}

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

				try {
					boolean flagItems = cloneNode.getItems().isEmpty(); //  ?????????????????????????????????????????
					boolean flagNodes = cloneNode.getNodes().isEmpty();	//  ?????????????????????????????????????????

					cloneNode.setId(repository.getUuidNull());
					newNode = repository.save(cloneNode);

					List<String> listNameItem = builder.getItems();
					if(!listNameItem.isEmpty()) { builderItemListAddNode( listNameItem, allItemDB, newNode); }

					List<String> listNameNode = builder.getNodes();
					if(!listNameNode.isEmpty()) { builderNodeListAddNode( listNameNode, newNode); }
					repository.save(newNode);
				} catch ( Exception e) {
					System.out.println("** <builderToDB> Exception node:\"" + cloneNode.getName() + "\"  " + e);
				}
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


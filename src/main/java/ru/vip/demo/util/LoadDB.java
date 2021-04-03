package ru.vip.demo.util;

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

	private final String bufTab = new String("                                                ");
	private final int endBuf = bufTab.length();
	private final int sizeTab = 4;
	private  int nTab = 0;

	static private int recCount = 0;
	static private final int recMax = 15;

	static private PrintWriter outFile;

	public final EstimateImpl repository;

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
			System.out.println("** Ошибка удаления комментариев из Json inFile/outFile:\"" + in_nameFile + "\"/\"" + out_nameFile +   "\"   " + e);
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////
//
	private void printItems(List<Item> items) throws Exception  {
		try{
			for (Item item : items) {
				this.outFile.println(bufTab.substring(endBuf - (sizeTab * (nTab + 2)))
							+ item.getName() + " id=\"" + item.getId() + "\"");
			}
		}catch (NullPointerException e) {
			System.out.println("** <printItems>  Ex:" + e);
		}
	}

	private void printNode(Node srcNode) throws Exception {
		try {
			this.outFile.println(bufTab.substring(endBuf - (sizeTab * nTab))
					+ "<" + srcNode.getStatus().getName() + ">  \""
					+ srcNode.getName()
					+ "\"   id=\"" + srcNode.getId() + "\"");
			List<Item> items = srcNode.getItems();
			if(items != null) {
				printItems(items);
			}
		}catch (Exception e) {
			System.out.println("** <printNode> srcNode:[" + srcNode.getName() + "] Ex:" + e); // "] n=[" + n +
		}
	}

	private void printTreeNodes(Node srcNode) throws Exception  {

		List<Node> nodes = srcNode.getNodes();
		if(nodes != null) {
			try {
				for (Node node : nodes) {
					printNode(node);
					++nTab;
					printTreeNodes(node);
					--nTab;
				}
			}catch (Exception e) {
				System.out.println("** <printTreeNodes> srcNode:[" + srcNode.getName() + "]  nTab:[" + nTab + "] Ex:" + e);
			}
		}
	}

	private void printNodes(Node srcNode) throws Exception  {
		try {
			nTab = 0;
			printNode(srcNode);
			++nTab;
			printTreeNodes(srcNode);
		}catch (Exception e) {
			System.out.println("** <printNodes> srcNode:[" + srcNode.getName() + "] Ex:" + e);
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////
//
	private void scanTreeNodes(Node srcNode){
		boolean flag = false;
		List<Node> nodes = srcNode.getNodes();
		if(nodes == null) { return; }
		try {
			for (Node node : nodes) {
				flag = node.getItems().isEmpty();
				scanTreeNodes(node);
			}
		}catch (NullPointerException e) {
			System.out.println("** <scanTreeNodes> srcNode:[" + srcNode.getName() + "] flag= " + flag + "  Ex:" + e);
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////
//
	public void writeNodeToJson(String nameNode, String out_json) throws Exception  {

		List<Node> nodes = repository.getAllNode();
		try (PrintWriter outFile = new PrintWriter(out_json, StandardCharsets.UTF_8)) {
			this.outFile = outFile;
			outFile.println("\n // Начало теста\n");
			for (Node node : nodes) {
				if (node.getName().equals(nameNode)) {
					scanTreeNodes(node);
					UUID idSrc =  node.getId();
					UUID idClone = cloneNodeForId( idSrc); //idSrc);
					Optional<Node> optional = repository.findByIdNode(idClone);
					if(optional.isPresent()) {
						Node cloneNode = optional.get();
						outFile.println(" // Начало теста cloneNode:[" + cloneNode.getName() + "]\n");
						printNodes(cloneNode);
						outFile.println("\n // Конец теста  cloneNode:[" + node.getName() + "]");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("** <writeNodeToJson> Ошибка сериализации в файл Json:" + e); // err=[" + err + "]
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
		return repository.save(cloneNode);
	}

	private Item cloneItemToDB( Item item) throws Exception {
		Item cloneItem = item.clone();
		cloneItem.setId(repository.getUuidNull());
		return repository.save(cloneItem);
	}

	private void cloneItemAddNode( Item item, Node dNode) throws Exception {
		dNode.getItems().add(cloneItemToDB(item));
	}

	private Node cloneNodeAddNode( Node node, Node newNode) throws Exception  {
		try {
			Node cloneNode = cloneNodeToDB(node);
			exCloneNode = cloneNode;
			cloneListItemAddNode(cloneNode);
			Node saveNode = repository.save(cloneNode);
			newNode.getNodes().add(repository.save(saveNode));
			return saveNode;
		}catch (Exception e){
			System.out.println("\r\n** <cloneNodeAddNode> exCloneNode:[" + exCloneNode.getName()
					+ "] exCloneNode.id=[" + exCloneNode.getId()
					+ "] ex:" + e);
		}
		return null;
	}

	private void cloneListItemAddNode (Node cloneNode) throws Exception {
		try{
		List<Item> items = cloneNode.getItems().stream().collect(Collectors.toList());
		cloneNode.getItems().clear();
		for (Item item : items) {
			cloneItemAddNode( item, cloneNode);
		}
		}catch (Exception e){
			System.out.println("** <cloneListItemAddNode> ex:" + e);
		}

	}

	private void cloneListNodeAddNode( Node subNode) throws Exception  {

		if(subNode == null) { return; }
		List<Node> nodes1 = subNode.getNodes();
		if(nodes1.isEmpty()) { return; }
		if( recCount > recMax) {
			System.out.println("** <cloneListNodeAddNode> node: \"" + subNode.getName() + "\""
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
	private Node exNode = null;
	private Node exNewNode = null;
	private Node exCloneNode = null;
	private Item exItem = null;

	private void cloneNodesAddNode( Node cloneNode) throws Exception  {
		if(cloneNode == null) { return; }
		List<Node> cloneNodes  = cloneNode.getNodes();
		if(cloneNodes == null || cloneNodes.isEmpty()) { return; } //
		if( recCount > recMax) {
			System.out.println("** <nodesAddNode> node: \"" + cloneNode.getName() + "\""
					+ " Превышена глубина рекурсии [recCount>=recMax]:" + recCount + ">=" + recMax);
			return;
		}
		List<Node> nodes = cloneNodes.stream().collect(Collectors.toList());
		cloneNode.getNodes().clear();
		++recCount;
		for(Node node : nodes) {
			exNode = node;
			Node newNode = cloneNodeAddNode( node, cloneNode);
			exNewNode = newNode;
			exCloneNode = cloneNode;
			cloneNodesAddNode( newNode);		// Рекурсия. "recCount" глубина Рекурсии
		}
	}

	private UUID cloneNodeForId( UUID idNode) throws Exception  {

		UUID id = repository.getUuidNull();
		Optional<Node> optional = repository.findByIdNode(idNode);
		if(!optional.isPresent()) { return id; }
		Node node = optional.get();
		exNode = node;
		try {
			Node cloneNode = cloneNodeToDB(node);
			id = cloneNode.getId();
			cloneNodesAddNode( cloneNode);
			return id;
		}catch (Exception e) {
			System.out.println("\r\n** <cloneNodeForId> node:[" // + node.getName()
					+ "] id=[" + id
					+ "] exNode:[" + exNode.getName()
					+ "] exNewNode:[" + exNewNode.getName()
					+ "] exCloneNode:[" + exCloneNode.getName()
					+ "]\r\n**   NullPointerException:" + e);
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
//					System.out.println("-- <builderToDB> node:\"" + cloneNode.getName()
//							+ "\"  flagNodes:\"" + flagNodes + "\""
//							+ "\"  flagItems:\"" + flagItems + "\"");
//
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


package ru.vip.demo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

	private final ItemDirectory itemDirectory = new ItemDirectory();

	@Autowired
	public EstimateImpl repository;


	public void writeNodeToJson(String out_node){

		List<Node> nodeList = repository.getAllNode();

		try (PrintWriter outFile = new PrintWriter(out_node, StandardCharsets.UTF_8)){
			int n = 0;
			outFile.print("[");
			for (Node node : nodeList) {
				if (n++ > 0) { outFile.print(","); }
				outFile.print(" {" +
						"\n  \"id\" : \"" + node.getId() + "\"," +
						"\n  \"name\" : \"" + node.getName() + "\"," +
						"\n  \"title\" : \"" + node.getTitle() + "\"," +
						"\n  \"status\" : \"" + node.getStatus() + "\"," +
						"\n  \"unit\" : \"" + node.getUnit() + "\"," +
						"\n  \"quantity\" : " + node.getQuantity() + "," +
						"\n  \"price\" : " + node.getPrice() + "," +
						"\n  \"nodes\" : []," +
						"\n  \"items\" : []");
				outFile.print("\n}");
			}
			outFile.println("]");
			outFile.println(" ");
		} catch (IOException e) {
			System.out.println("Ошибка сериализации \"Node\" в файл Json:" + e);
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////
//
//
	public void itemAndItemDirectToDB(String in_item_directory, String in_item) throws Exception {

//		List<ItemDirectory> itemDirectories;

//		try {// Чтение из JSON file в List
//			itemDirectories = repository.readJSON(in_item_directory);
//		}catch (JsonParseException e) {
//			e.printStackTrace ();
//		}catch (JsonMappingException e) {
//			e.printStackTrace ();
//		}catch (IOException e) {
//			e.printStackTrace ();
//		}

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
//
	public void builderToDB(String in_builder, String in_node) throws Exception {

		List<EstimateBuilder> builders = repository.readJsonBuilder(in_builder);// Чтение из JSON file в List
		List<Node> nodes = repository.readJsonNode(in_node);
		List<Item> itemList = repository.getAllItem();	// Чтение из базы данных в List

		List<Node> nodeList = repository.getAllNode();
		List<Node> nodeList1 = repository.getAllNode();

		System.out.println(" ");
		for (EstimateBuilder builder : builders) {
			String name = builder.getNameNode();
			for (Node node : nodeList) {
				int i = 0;
				if(name.equals(node.getName())) {
//					System.out.println("-3-++>name:" + name + "           node.name:" + node.getName() + "                node.id:" + node.getId());
					Node cloneNode = (Node) node.clone();
					try {
						cloneNode.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
					}catch (Exception e) {
						System.out.println(" Exception_2:" + e);
					}
					cloneNode.setName(builder.getNewName());
					cloneNode.setTitle(builder.getTitleNode());
//					System.out.println("-4-::>name:" + name + "  cloneNode.newName:" + cloneNode.getName() + "      node.id:" + node.getId());

					cloneNode.setNodes(null);
					cloneNode.setItems(null);

					Node newNode = repository.save(cloneNode);
//					System.out.println("-7-==>name:" + name + "   cloneNode.newName:" + cloneNode.getName() + "   cloneNode.id:" + cloneNode.getId());
//					System.out.println("-9-==>name:" + name + "     newNode.newName:" + newNode.getName() + "     newNode.id:" + newNode.getId());

//					for (String str : builder.getListNode()) {
//						for (Node node1 : nodeList1) {              // ??? nodeList head for
//							if (str.equals(node1.getName())) {
//								node1.setId(null);
////								node1.getListNode().add(repository.save(node1));
//								++i;
//							}
//						}
//					}
//					for (String str : builder.getListItem()) {
//						for (Item item :  itemList) {
//							if(str.equals(item.getName())) {
//								item.setId(null);
//								node.getListItem().add(repository.save(item));
//								++i;
//							}
//						}
//					}
				}
//				if(i>0){
//					node.setId(null);
//					repository.save(node);
//				}
			}
		}
//		System.out.println(" ");
	}











// ///////////////////////////////////////////////////////////////////////////////////////////////
//        JSON Десериализация нескольких объектов с применением "Jackson" и "com.google.guava"
//
	public static ImmutableList<ItemDirectory> getItemDirectory() throws IOException {
		// JSON Десериализация нескольких объектов
		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = Resources.getResource("item_directory.json").openStream();
		List<ItemDirectory> items = mapper.readValue(inputStream, new TypeReference<List<ItemDirectory>>() {});
		return ImmutableList.copyOf(items);
	}

// ///////////////////////////////////////////////////////////////////////////////////////////////
//        JSON Десериализация нескольких объектов с применением "Jackson" и "com.google.guava"
//                и загрузка их в Базу Данных
//	public static ImmutableList<T> getData(T t, String resourceName ) throws IOException {
//		// JSON Десериализация нескольких объектов
//		ObjectMapper mapper = new ObjectMapper();
//		InputStream inputStream = Resources.getResource(resourceName).openStream();
//		List<ItemDirectory> lst = mapper.readValue(inputStream, new TypeReference<List<T>>() {});
//		return ImmutableList.copyOf(lst);
//	}
//	// Загрузка объектов в базу данных из JSON файла
//	private void loadData( T t, String resourceName) throws Exception {
//		ImmutableList<T> lst = this.getData( t, resourceName);
//		for (T item : lst) {
//			repository.save(item);
//		}
//	}

// ///////////////////////////////////////////////////////////////////////////////////////////////
//    Сериализация и десериализация JSON
//
	public void writeJsonItemDirectory(ItemDirectory itemDirectory) throws Exception {
		// JSON Сериализация объекта
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue( new File("src/test/resources/data/item_directory.json"), itemDirectory);
		} catch (IOException e) {
			System.out.println("Исключение при сериализации: " + e);
		}
	}

	public void readJsonItemDirectory() throws Exception {
		// JSON Десериализация объекта
		ObjectMapper mapper = new ObjectMapper();
		try {
				ItemDirectory itemDirectory1 =
						mapper.readValue(new File("src/test/resources/data/item_directory.json"), ItemDirectory.class);
				System.out.println("\n +++++ JSON После чтения из файла --> itemDirectory1:\n" + itemDirectory1);
		} catch (IOException e) {
			System.out.println("Исключение при десериализации: " + e);
		}
		System.out.println(" ");
	}

// ///////////////////////////////////////////////////////////////////////////////////////////////
//    Сериализация и десериализация стандартная библиотека
//
	public void serialItemDirectory(ItemDirectory itemDirectory)throws Exception {

		System.out.println("\n >>>>> Перед записью в файл --> itemDirectory:\n" + itemDirectory);

		// Сериализация объекта
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream( new FileOutputStream("itemDirectory"));
			objectOutputStream.writeObject( itemDirectory);
		} catch (IOException e) {
			System.out.println("Исключение при сериализации: " + e);
		}

		// Десериализация объекта
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("itemDirectory"));
			ItemDirectory itemDirectory1 = (ItemDirectory) objectInputStream.readObject();

			System.out.println(" +++++ После чтения из файла --> itemDirectory1:\n" + itemDirectory1 + "\n");
		} catch (IOException e) {
			System.out.println("Исключение при десериализации: " + e);
		}
	}

// ///////////////////////////////////////////////////////////////////////////////////////////////
//
//

	public void loadItemDirectory() {

		repository.save(itemDirectory);
//		repository.save(ItemDirectory.builder()
//				.category(Category.SERVICE)
//				.code("Код поставщика2")
//				.name("Накладные расходы2")
//				.unit(Unit.STEP)
//				.price(BigDecimal.valueOf(1.2)
//				.vendor("Поставщик2"))
//
//				.build());
	}


}


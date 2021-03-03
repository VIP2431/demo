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
import ru.vip.demo.entity.ItemDirectory;
import ru.vip.demo.serviceimpl.EstimateImpl;
import ru.vip.demo.type.Category;
import ru.vip.demo.type.Unit;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LoadDB {

	private final ItemDirectory itemDirectory = new ItemDirectory();

	@Autowired
	public EstimateImpl repository;

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
		repository.save(ItemDirectory.builder()
				.category(Category.SERVICE)
				.vendor("Поставщик2")
				.code("Код поставщика2")
				.name("Накладные расходы2")
				.unit(Unit.STEP)
				.price(BigDecimal.valueOf(1.2))
				.build());
		repository.save(ItemDirectory.builder()
				.category(Category.MATERIAL)
				.vendor("Поставщик3")
				.code("Код поставщика3")
				.name("Накладные расходы3")
				.unit(Unit.STEP)
				.price(BigDecimal.valueOf(1.3))
				.build());
		repository.save(ItemDirectory.builder()
				.category(Category.TOOLS)
				.vendor("Поставщик4")
				.code("Код поставщика4")
				.name("Накладные расходы4")
				.unit(Unit.STEP)
				.price(BigDecimal.valueOf(1.4))
				.build());
		repository.save(ItemDirectory.builder()
				.category(Category.WORK)
				.vendor("Поставщик5")
				.code("Код поставщика5")
				.name("Накладные расходы5")
				.unit(Unit.STEP)
				.price(BigDecimal.valueOf(1.5))
				.build());
	}


}


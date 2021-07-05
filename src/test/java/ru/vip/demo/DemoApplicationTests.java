package ru.vip.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vip.demo.entity.MainBuilder;
import ru.vip.demo.serviceimpl.EstimateImpl;
import ru.vip.demo.util.CreatDateBaseEstimate;
import ru.vip.demo.util.LoadDB;

import java.util.List;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	public LoadDB loadDB;
	@Autowired
	public EstimateImpl repository;
	@Autowired
	public CreatDateBaseEstimate creatDateBaseEstimate;

	@Value("${file_json.init_item}")  	// Передача параметра из application.yml
	String init_item;

	@Value("${file_json.init_node}") 	// Передача параметра из application.yml
	String init_node;

	@Value("${file_json.init_builder}")
	String init_builder;

	@Value("${file_json.in_node}")
	String in_node;

	@Value("${file_json.in_builder}")
	String in_builder;

	@Value("${file_json.in_item}")
	String in_item;

	@Value("${file_json.prefix_}")
	String prefix_;

	@Value("${file_json.prefix_test}")
	String prefix_test;

	////////////////////////////////////////////////////////////////////////////
//
	public void dataTest2()throws Exception {
		creatDateBaseEstimate.initDataBase();
		// Распечатка сметы сгенерированной и загруженной в БД конструктором-инициатором init_builder
		loadDB.writeNodeToJson("Шереметьевская_3", prefix_ + "NEW_2" + in_node);
	}

	@Test
	public void dataTest()throws Exception {
		// Удалить комментарии и пустые строки из файлов инициализации БД
		creatDateBaseEstimate.clearCommentFileInitJason();

		creatDateBaseEstimate.loadItemToDB();// Загрузить Справочник <Item>
		// Запмсь <Item> из БД в test_JSON для визуальног контроля
		repository.writeJsonItem(prefix_test + in_item, repository.getAllItem());
		creatDateBaseEstimate.loadNodeToDB();// Запись в БД Блоков <Node>

		// Чтения из Jason в List builders и запись из List в test_Jason для визуального контроля
		List<MainBuilder> builders = repository.readJsonBuilder(prefix_ + in_builder);
		repository.writeJsonBuilder(prefix_test + in_builder, builders);

		// Запуск "builder.json" конструктора-инициатора
		creatDateBaseEstimate.builderToDB( builders);

		// Распечатка сметы сгенерированной и загруженной в БД конструктором-инициатором init_builder
		loadDB.writeNodeToJson("Шереметьевская_3", prefix_ + "NEW_" + in_node);
	}
}

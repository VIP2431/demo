package ru.vip.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.entity.Item;
import ru.vip.demo.entity.ItemDirectory;
import ru.vip.demo.entity.MainBuilder;
import ru.vip.demo.entity.Node;
import ru.vip.demo.serviceimpl.EstimateImpl;
import ru.vip.demo.type.TypeItem;

import java.util.List;

@RequiredArgsConstructor
@Component
@Transactional
public class CreatNewDateBase {

    public final EstimateImpl repository;
    public final InitBuilder initBuilder;

    @Value("${file_json.init_node}")            // Передача параметра из application.yml
    String init_node;

    @Value("${file_json.init_builder}")        // Передача параметра из application.yml
    String init_builder;

    @Value("${file_json.in_node}")                // Передача параметра из application.yml
    String in_node;

    @Value("${file_json.in_builder}")            // Передача параметра из application.yml
    String in_builder;

    @Value("${file_json.in_item}")            // Передача параметра из application.yml
    String in_item;

    @Value("${file_json.in_item_directory}")    // Передача параметра из application.yml
    String in_item_directory;

    @Value("${file_json.prefix_}")                // Передача параметра из application.yml
    String prefix_;

    @Value("${file_json.prefix_test}")            // Передача параметра из application.yml
    String prefix_test;

    ////////////////////////////////////////////////////////////////////////////
//
    public void initDataBase() throws Exception {
//  Убрать комментарии и пустые строки из входных файлов Init Jason
        clearCommentFileInitJason();
// Загрузить Справочники <ItemDirectory> и <Item> в Базу Данных
        itemAndItemDirectToDB(prefix_ + in_item_directory, prefix_ + in_item);
// Запись в БД Блоков <Node>
        List<Node> nodes = repository.readJsonNode(prefix_ + in_node); // Чтение из JSON file в List
        for (Node node : nodes) {	repository.save(node);	}				        // Запись из List в базу данных
// Чтения из Jason в List builders
        List<MainBuilder> builders = repository.readJsonBuilder(prefix_ + in_builder);
// Запуск "builder.json" конструктора-инициатора
        initBuilder.builderToDB( builders);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
//  Убрать комментарии и пустые строки из входных файлов Init Jason
    public void clearCommentFileInitJason() {
        StrUtil.deleteComment(prefix_ + init_builder, prefix_ + in_builder);
        StrUtil.deleteComment(prefix_ + init_node, prefix_ + in_node);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
//
    public void itemAndItemDirectToDB(String in_item_directory, String in_item) throws Exception {

        List<ItemDirectory> itemDirectories = repository.readJsonItemDirectory(in_item_directory);

        for (ItemDirectory itemDirectory : itemDirectories) {
            repository.save(itemDirectory);
        } // Запись из List в базу данных

        List<Item> items = repository.readJsonItem(in_item);            // Чтение из JSON file
        List<ItemDirectory> itemDirs = repository.getAllItemDirectory();// Чтение из базы данных
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

                    item.setType(TypeItem.TYPE_ITEM);
                    repository.save(item);                              // Запись в базу данных
                }
            }
        }
    }
}
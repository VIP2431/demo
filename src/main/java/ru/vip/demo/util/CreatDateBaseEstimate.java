package ru.vip.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.entity.Item;
import ru.vip.demo.entity.MainBuilder;
import ru.vip.demo.entity.Node;
import ru.vip.demo.service.NodeService;
import ru.vip.demo.serviceimpl.EstimateImpl;

import java.util.List;

//@Service
// @Slf4j

@RequiredArgsConstructor
@Transactional
@Component
public class CreatDateBaseEstimate {

    public final LoadDB loadDB;
    public final NodeService nodeService;
    public final EstimateImpl repository;

    @Value("${file_json.init_item}")    // Передача параметра из application.yml
    String init_item;

    @Value("${file_json.init_node}")
    String init_node;

    @Value("${file_json.init_builder}")
    String init_builder;

    @Value("${file_json.in_item}")
    String in_item;

    @Value("${file_json.in_node}")
    String in_node;

    @Value("${file_json.in_builder}")
    String in_builder;

    @Value("${file_json.prefix_}")
    String prefix_;

    @Value("${file_json.prefix_test}")
    String prefix_test;

//////////////////////////////////////////////////////////////////////////////////////////////
//
    public void initDataBase() throws Exception {

        clearCommentFileInitJason();//  Убрать комментарии и пустые строки из входных файлов Init Jason

        loadItemToDB(); // Загрузить Справочник <Item> в Базу Данных
        loadNodeToDB(); // Загрузить в БД блоки <Node>

        List<MainBuilder> builders = repository.readJsonBuilder(prefix_ + in_builder);

        builderToDB(builders); // Запуск "builder.json" конструктора-инициатора
    }

    //  Убрать комментарии и пустые строки из входных файлов Init Jason
    public void clearCommentFileInitJason() {
        StrUtil.deleteComment(prefix_ + init_item, prefix_ + in_item);
        StrUtil.deleteComment(prefix_ + init_node, prefix_ + in_node);
        StrUtil.deleteComment(prefix_ + init_builder, prefix_ + in_builder);
    }

    // Загрузить Справочник <Item> в Базу Данных
    public void loadItemToDB() throws Exception {
        List<Item> items = repository.readJsonItem(prefix_ + in_item);
        for (Item item : items) { repository.save(item); }
    }
    // Запись в БД Блоков <Node>
    public void loadNodeToDB()throws Exception {
        List<Node> nodes = repository.readJsonNode(prefix_ + in_node);
        for (Node node : nodes) { repository.save(node); }
    }


//////////////////////////////////////////////////////////////////////////////////////////////
//
    private void builderItemListAddNode(List<String> listNameItem, List<Item> allItemDB, Node dNode) {
        for (String nameItem : listNameItem) {            // взять следующее имя из заданного в "builder" списка
            for (Item item : allItemDB) {                // взять следующее "item" из списка взятого в БД
                if (nameItem.equals(item.getName())) {    // Item с заданным именем найден
                    nodeService.cloneItemAddNode(item, dNode);
                    break;
                }
            }
        }
    }

    private void builderNodeListAddNode(List<String> listNameNode, Node newNode) {
        List<Node> allNodeDB = repository.getAllNode();
        nodeService.clearRecCount();
        for (String nameNode : listNameNode) {
            for (Node node : allNodeDB) {
                if (nameNode.equals(node.getName())) {
                    Node subNode = nodeService.cloneNodeAddNode(node, newNode);
                    nodeService.cloneListNodeAddNode(subNode);
                    break;
                }
            }
        }
    }

    public void builderToDB(List<MainBuilder> builders) throws Exception {

        List<Item> allItemDB = repository.getAllItem();
        List<Node> allNodeDB = repository.getAllNode();

        for (MainBuilder builder : builders) {
            Node newNode;
            String nameNode = builder.getNameNode();
            for (Node node : allNodeDB) {
                if (!nameNode.equals(node.getName())) {
                    continue;
                }
                Node cloneNode = node.clone();                        // Клонировать  Node
                cloneNode.setName(builder.getNewName());
                cloneNode.setTitle(builder.getTitleNode());

                try {
                    /////////////////////////////////////////////////////////////////////////////////////////////
                    if (cloneNode.getItems() != null && cloneNode.getItems().isEmpty())
                        nodeService.clearRecCount(); // FetchType.LAZY
                    if (cloneNode.getNodes() != null && cloneNode.getNodes().isEmpty())
                        nodeService.clearRecCount(); // FetchType.LAZY
                    /////////////////////////////////////////////////////////////////////////////////////////////

                    cloneNode.setId(repository.getUUID_NULL());
                    newNode = repository.save(cloneNode);

                    List<String> listNameItem = builder.getItems();
                    if (!listNameItem.isEmpty()) {
                        builderItemListAddNode(listNameItem, allItemDB, newNode);
                    }
                    List<String> listNameNode = builder.getNodes();
                    if (!listNameNode.isEmpty()) {
                        builderNodeListAddNode(listNameNode, newNode);
                    }
                    repository.save(newNode);
                } catch (Exception e) {
                    System.out.println("** <builderToDB> Exception node:\"" + cloneNode.getName() + "\"  " + e);
                }
                break;
            }
        }
    }
}


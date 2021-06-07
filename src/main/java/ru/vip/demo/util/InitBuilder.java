package ru.vip.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.entity.Item;
import ru.vip.demo.entity.MainBuilder;
import ru.vip.demo.entity.Node;
import ru.vip.demo.serviceimpl.EstimateImpl;

import java.util.List;


//@Service
// @Slf4j

@RequiredArgsConstructor
@Component
@Transactional
public class InitBuilder {

    public final LoadDB loadDB;
    public final EstimateImpl repository;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void builderItemListAddNode(List<String> listNameItem, List<Item> allItemDB, Node dNode) {
//        //noinspection LambdaCanBeReplacedWithAnonymous
//        listNameItem
//                .forEach((nameItem) -> { allItemDB.stream()
//                                        .filter((Item item) -> nameItem.equals( item.getName()))
//                                        .forEach((Item item) -> loadDB.cloneItemAddNode(item, dNode));
//                                       });
//    }
//
//	private void builderNodeListAddNode( List<String> listNameNode, Node newNode) {
//
//		List<Node> allNodeDB = repository.getAllNode();
//		loadDB.clearRecCount();
//
//		listNameNode
//                .forEach((nameNode) -> { allNodeDB.stream()
//                                        .filter((Node node) -> nameNode.equals(node.getName()))
//                                        .forEach((Node node) -> {
//                                                                Node subNode = loadDB.cloneNodeAddNode( node, newNode);
//                                                                loadDB.cloneListNodeAddNode( subNode);
//                                                                });
//		                                });
//	}
/////////////////////////////////////////////////////////////////////////////////////////////
//
    private void builderItemListAddNode(List<String> listNameItem, List<Item> allItemDB, Node dNode) {
        for (String nameItem : listNameItem) {            // взять следующее имя из заданного в "builder" списка
            for (Item item : allItemDB) {                // взять следующее "item" из списка взятого в БД
                if (nameItem.equals(item.getName())) {    // Item с заданным именем найден
                    loadDB.cloneItemAddNode(item, dNode);
                    break;
                }
            }
        }
    }

    private void builderNodeListAddNode(List<String> listNameNode, Node newNode) {
        List<Node> allNodeDB = repository.getAllNode();
        loadDB.clearRecCount();
        for (String nameNode : listNameNode) {
            for (Node node : allNodeDB) {
                if (nameNode.equals(node.getName())) {
                    Node subNode = loadDB.cloneNodeAddNode(node, newNode);
                    loadDB.cloneListNodeAddNode(subNode);
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
                        loadDB.clearRecCount(); // FetchType.LAZY
                    if (cloneNode.getNodes() != null && cloneNode.getNodes().isEmpty())
                        loadDB.clearRecCount(); // FetchType.LAZY
                    /////////////////////////////////////////////////////////////////////////////////////////////

                    cloneNode.setId(repository.getUuidNull());
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


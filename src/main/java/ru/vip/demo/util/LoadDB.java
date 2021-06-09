package ru.vip.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.entity.HeadingItem;
import ru.vip.demo.entity.Item;
import ru.vip.demo.entity.ItemDirectory;
import ru.vip.demo.entity.Node;
import ru.vip.demo.serviceimpl.EstimateImpl;
import ru.vip.demo.type.TypeItem;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Component
public class LoadDB {

    private int nTab = 0;
    private final int sizeTab = 4;
    private final String bufTab = " ".repeat(sizeTab * 12);

    private final int endBuf = bufTab.length();

    static private final int recMax = 15;
    static private int recCount = 0;

    static private PrintWriter outFile;
    public final EstimateImpl repository;

    public List<HeadingItem> headingItems;

    public void clearRecCount() {
        recCount = 0;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
//
    private void doPrintItem(Item item){
        outFile.println(bufTab.substring(endBuf - (sizeTab * (nTab + 2)))
                + "<" + item.getType().getName() + ">  "
                + "<" + item.getStatus().getName() + ">  \""
                + item.getName() + "\" id=\"" + item.getId() + "\"");
    }

   private void doPrintNode(Node srcNode){
       outFile.println(bufTab.substring(endBuf - (sizeTab * nTab))
               + "<" + srcNode.getType().getName() + ">  "
               + "<" + srcNode.getStatus().getName() + ">  \""
               + srcNode.getName()
               + "\"   id=\"" + srcNode.getId() + "\"");
       List<Item> items = srcNode.getItems();
       if (items != null) {
           printItems(items);
       }
   }

    //////////////////////////////////////////////////////////////////////////////////////////////////
//
    private void printItems(List<Item> items) {
        try {
            for (Item item : items) { doPrintItem( item); }
        } catch (NullPointerException e) {
            System.out.println("** <printItems>  Ex:" + e);
        }
    }

    private void printNode(Node srcNode) {
        try {
            doPrintNode( srcNode);
        } catch (Exception e) {
            System.out.println("** <printNode> srcNode:[" + srcNode.getName() + "] Ex:" + e); // "] n=[" + n +
        }
    }

    private void printNodes(Node srcNode) {
        try {
            nTab = 0;
            printNode(srcNode);
            ++nTab;
            printTreeNodes(srcNode);
        } catch (Exception e) {
            System.out.println("** <printNodes> srcNode:[" + srcNode.getName() + "] Ex:" + e);
        }
    }

    private void printTreeNodes(Node srcNode) {

        List<Node> nodes = srcNode.getNodes();
        if (nodes == null) return;
        try {
            for (Node node : nodes) {
                printNode(node);
                ++nTab;
                printTreeNodes(node);
                --nTab;
            }
        } catch (Exception e) {
            System.out.println("** <printTreeNodes> srcNode:[" + srcNode.getName() + "]  nTab:[" + nTab + "] Ex:" + e);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void scanTreeNodes(Node srcNode) {
//        List<Node> nodes = srcNode.getNodes();
//        if (nodes == null) return;
//        try {
//            for (Node node : nodes) {
//                List<Item> items = node.getItems();
//                if (items != null) {
//                    for (Item item : items) {
//                        if (item == null) break;
//                    }
//                }
//                scanTreeNodes(node);
//            }
//        } catch (NullPointerException e) {
//            System.out.println("** <scanTreeNodes> srcNode:[" + srcNode.getName() + "]   Ex:" + e); // flag= " + flag + "
//        }
//    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
//
    public void writeNodeToJson(String nameNode, String out_json) {

        final long start = System.currentTimeMillis();
//System.out.println("\n-01- <writeNodeToJson>:" + nameNode + "\n");
        List<Node> nodes = repository.getAllNode();
//System.out.println("\n-02- <writeNodeToJson>:" + nameNode  + "\n");
        try (PrintWriter outFl = new PrintWriter(out_json, StandardCharsets.UTF_8)) {
            outFile = outFl;
            for (Node node : nodes) {
                if (node.getName().equals(nameNode)) {
                    createListHeadingItemFromTreeNodes( node);

//                    UUID idSrc = node.getId();
//                    UUID idClone = cloneNodeForId(idSrc);
//                    Optional<Node> optional = repository.findByIdNode(idClone);
//                    if (optional.isPresent()) {
//                        Node cloneNode = optional.get();
//                        System.out.println( "// " + ZonedDateTime.now() + " Начало теста cloneNode:[" + cloneNode.getName() + "]\n");
//
//                        headingItems = createListHeadingItemFromTreeNodes( cloneNode);
//
//                        printNodes(cloneNode);
//                        System.out.println("\n // Конец теста  cloneNode:[" + node.getName() + "]  " + ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME));
//                    }
                    break;
                }
            }
            final long executionTime = System.currentTimeMillis() - start;
            System.out.println("\n // Время исполнения теста=[" + executionTime + "ms]" );
        } catch (Exception e) {
            System.out.println("** <writeNodeToJson> Ошибка сериализации в файл Json:" + e);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
//
    public List<HeadingItem> createListHeadingItemFromTreeNodes(Node srcNode) {
        final long start =  System.currentTimeMillis();

        List<HeadingItem> headingItems = new ArrayList<>();
        headingItems.add(srcNode);
        createListHeadingItem(srcNode, headingItems);

        final long executionTime =  System.currentTimeMillis() - start;
        System.out.println("\n // Время создания List=[" + executionTime + "ms]" );
        printListHeadingItem( headingItems);

        final long executionTime2 =  System.currentTimeMillis() - start;
        System.out.println("\n // Время создания и распечатки List=[" + executionTime2 + "ms]" );

        return headingItems;
    }

    private void createListHeadingItem( Node srcNode, List<HeadingItem> headingItems) {
        List<Node> nodes = srcNode.getNodes();
        if (nodes == null) return;
        try {
            for (Node node : nodes) {
                headingItems.add(node);
                List<Item> items = node.getItems();
                if (items != null) {
                    headingItems.addAll(items);
                }
                createListHeadingItem(node, headingItems );
            }
        } catch (NullPointerException e) {
            System.out.println("** <createListHeadingItemFromTreeNodes> srcNode:[" + srcNode.getName() + "]   Ex:" + e);
        }
    }

    public void printListHeadingItem(List<HeadingItem> headingItems) {
        int key = 0;
        outFile.println("[");
        for(HeadingItem headingItem : headingItems) {
             if(key++ != 0)  outFile.print(",");
            outFile.print("{\n"
                     +  " \"key\": " + key + ",\n"
                     +  " \"id\": \"" + headingItem.getId() + "\",\n"
                     +  " \"type\": \"" + headingItem.getType().name() + "\",\n"
                     +  " \"status\": \"" + headingItem.getStatus().name() + "\",\n"
                     +  " \"title\": \"" + headingItem.getTitle() + "\",\n"
                     +  " \"name\": \"" + headingItem.getName() + "\",\n"
                     +  " \"percentItem\": " + headingItem.getPercentItem() + ",\n"
                     +  " \"unit\": \"" + headingItem.getUnit().name() + "\",\n"
                     +  " \"quantity\": " + headingItem.getQuantity() + ",\n"
                     +  " \"price\": " + headingItem.getPrice() + ",\n"
                     +  " \"cost\": " + headingItem.getCost() + ",\n"
                     +  " \"sum\": " + headingItem.getSum() + ",\n"
             );

            if(headingItem.getType().name().equals("TYPE_ITEM")) {
                Item item = (Item) headingItem;
                outFile.print(" \"idItemDirectory\": \"" + item.getIdItemDirectory() + "\",\n"
                        + " \"category\": \"" + item.getCategory().name() + "\",\n"
                        + " \"code\": \"" + item.getCode() + "\",\n"
                        + " \"vendor\": \"" + item.getVendor() + "\"\n"
                );
            }else {
                outFile.print(" \"idItemDirectory\": \"\",\n"
                        + " \"category\": \"\",\n"
                        + " \"code\": \"\",\n"
                        + " \"vendor\": \"\"\n"
                );
            }
            outFile.print("}");
        }
        outFile.println("]");
     }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
//
    private Node cloneNodeToDB(Node node) throws Exception {
        Node cloneNode = node.clone();
//System.out.println("-10-  save->cloneNode:" + cloneNode.getName());
        cloneNode.setId(repository.getUuidNull());
//System.out.println("-11-  save->cloneNode:" + cloneNode.getName());
        return repository.save(cloneNode);
    }

    private Item cloneItemToDB(Item item) {
        try {
            Item cloneItem = item.clone();
//System.out.println("-20-  save->cloneItem:" + cloneItem.getName());
            cloneItem.setId(repository.getUuidNull());
//System.out.println("-21-  save->cloneItem:" + cloneItem.getName());
            return repository.save(cloneItem);

        } catch (Exception e) {
            System.out.println("** <cloneItemToDB> ex:" + e);
        }
        return null;
    }

    public void cloneItemAddNode(Item item, Node dNode) {
        dNode.getItems().add(cloneItemToDB(item));
    }

    public Node cloneNodeAddNode(Node node, Node newNode) {
        try {
            Node cloneNode = cloneNodeToDB(node);
            cloneListItemAddNode(cloneNode);
//System.out.println("-30-  save->cloneNode:" + cloneNode.getName());
            Node saveNode = repository.save(cloneNode);
//System.out.println("-31-  save->cloneNode:" + cloneNode.getName());
            newNode.getNodes().add(repository.save(saveNode));
//System.out.println("-32-  save->add.cloneNodes:" + cloneNode.getName());
            return saveNode;
        } catch (Exception e) {
            System.out.println("\r\n** <cloneNodeAddNode>  ex:" + e);
        }
        return null;
    }

    private void cloneListItemAddNode(Node cloneNode) {
        try {
            List<Item> items = new ArrayList<>(cloneNode.getItems());
            cloneNode.getItems().clear();
            for (Item item : items) {
                cloneItemAddNode(item, cloneNode);
            }
        } catch (Exception e) {
            System.out.println("** <cloneListItemAddNode> ex:" + e);
        }

    }

    public void cloneListNodeAddNode(Node subNode) {
        if (subNode == null) {
            return;
        }
        List<Node> nodes1 = subNode.getNodes();
        if (nodes1.isEmpty()) {
            return;
        }
        if (recCount > recMax) {
            System.out.println("** <cloneListNodeAddNode> node: \"" + subNode.getName() + "\""
                    + " Превышена глубина рекурсии [recCount>=recMax]:" + recCount + ">=" + recMax);
            return;
        }

        List<Node> nodes = new ArrayList<>(nodes1);
        subNode.getNodes().clear();
        ++recCount;
        for (Node node : nodes) {
            Node cloneNode = cloneNodeAddNode(node, subNode);
            cloneListNodeAddNode(cloneNode);    // Рекурсия. "recCount" глубина Рекурсии
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
//
    private void cloneNodesAddNode(Node cloneNode) {
        if (cloneNode == null) {
            return;
        }
        List<Node> cloneNodes = cloneNode.getNodes();
        if (cloneNodes == null || cloneNodes.isEmpty()) {
            return;
        } //
        if (recCount > recMax) {
            System.out.println("** <nodesAddNode> node: \"" + cloneNode.getName() + "\""
                    + " Превышена глубина рекурсии [recCount>=recMax]:" + recCount + ">=" + recMax);
            return;
        }
        List<Node> nodes = new ArrayList<>(cloneNodes);
        cloneNode.getNodes().clear();
        ++recCount;
        for (Node node : nodes) {
            Node newNode = cloneNodeAddNode(node, cloneNode);
            cloneNodesAddNode(newNode);        // Рекурсия. "recCount" глубина Рекурсии
        }
    }

    public UUID cloneNodeForId(UUID idNode) {

        UUID id = repository.getUuidNull();
        Optional<Node> optional = repository.findByIdNode(idNode);
        if (optional.isEmpty()) return id;
        Node node = optional.get();
        try {
            Node cloneNode = cloneNodeToDB(node);
            id = cloneNode.getId();
            recCount = 0;
            cloneNodesAddNode(cloneNode);
            return id;
        } catch (Exception e) {
            System.out.println("\r\n** <cloneNodeForId> node:["
                    + "] id=[" + id
                    + "]\r\n**   NullPointerException:" + e);
        }
        return id;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
//
    public void itemAndItemDirectToDB(String in_item_directory, String in_item) throws Exception {

        List<ItemDirectory> itemDirectories = repository.readJsonItemDirectory(in_item_directory);

        for (ItemDirectory itemDirectory : itemDirectories) {
            repository.save(itemDirectory);
        } // Запись из List в базу данных

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

                    item.setType(TypeItem.TYPE_ITEM);
                    repository.save(item);                                    // Запись из List в базу данных
                }
            }
        }
    }

}


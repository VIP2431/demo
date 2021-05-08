package ru.vip.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.entity.Item;
import ru.vip.demo.entity.ItemDirectory;
import ru.vip.demo.entity.Node;
import ru.vip.demo.serviceimpl.EstimateImpl;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

    public void clearRecCount() {
        recCount = 0;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
//
    private void printItems(List<Item> items) {
        try {
            for (Item item : items) {
                outFile.println(bufTab.substring(endBuf - (sizeTab * (nTab + 2)))
                        + item.getName() + " id=\"" + item.getId() + "\"");
            }
        } catch (NullPointerException e) {
            System.out.println("** <printItems>  Ex:" + e);
        }
    }

    private void printNode(Node srcNode) {
        try {
            outFile.println(bufTab.substring(endBuf - (sizeTab * nTab))
                    + "<" + srcNode.getStatus().getName() + ">  \""
                    + srcNode.getName()
                    + "\"   id=\"" + srcNode.getId() + "\"");
            List<Item> items = srcNode.getItems();
            if (items != null) {
                printItems(items);
            }
        } catch (Exception e) {
            System.out.println("** <printNode> srcNode:[" + srcNode.getName() + "] Ex:" + e); // "] n=[" + n +
        }
    }

    private void printTreeNodes(Node srcNode) {

        List<Node> nodes = srcNode.getNodes();
        if (nodes != null) {
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

    //////////////////////////////////////////////////////////////////////////////////////////////////
//
    private void scanTreeNodes(Node srcNode) {
        List<Node> nodes = srcNode.getNodes();
        if (nodes == null) {
            return;
        }
        try {
            for (Node node : nodes) {
                List<Item> items = node.getItems();
                if (items != null) {
                    for (Item item : items) {
                        if (item == null) break;
                    }
                }
                scanTreeNodes(node);
            }
        } catch (NullPointerException e) {
            System.out.println("** <scanTreeNodes> srcNode:[" + srcNode.getName() + "]   Ex:" + e); // flag= " + flag + "
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
//
    public void writeNodeToJson(String nameNode, String out_json) {

        final long start = System.currentTimeMillis();
        List<Node> nodes = repository.getAllNode();
        try (PrintWriter outFl = new PrintWriter(out_json, StandardCharsets.UTF_8)) {
            outFile = outFl;
            for (Node node : nodes) {
                if (node.getName().equals(nameNode)) {
                    scanTreeNodes(node);
                    UUID idSrc = node.getId();
                    UUID idClone = cloneNodeForId(idSrc);
                    Optional<Node> optional = repository.findByIdNode(idClone);
                    if (optional.isPresent()) {
                        Node cloneNode = optional.get();
                        outFile.println( "// " + ZonedDateTime.now() + " Начало теста cloneNode:[" + cloneNode.getName() + "]\n");
                        printNodes(cloneNode);
                        outFile.println("\n // Конец теста  cloneNode:[" + node.getName() + "]  " + ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME));
                    }
                }
            }
            final long executionTime = System.currentTimeMillis() - start;
            outFile.println("\n // Время исполнения теста=[" + executionTime + "ms]" );
        } catch (Exception e) {
            System.out.println("** <writeNodeToJson> Ошибка сериализации в файл Json:" + e);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
//
    private Node cloneNodeToDB(Node node) throws Exception {
        Node cloneNode = node.clone();
        cloneNode.setId(repository.getUuidNull());
        return repository.save(cloneNode);
    }

    private Item cloneItemToDB(Item item) {
        try {
            Item cloneItem = item.clone();
            cloneItem.setId(repository.getUuidNull());
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
            Node saveNode = repository.save(cloneNode);
            newNode.getNodes().add(repository.save(saveNode));
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

        for (ItemDirectory item : itemDirectories) {
            repository.save(item);
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

                    repository.save(item);                                    // Запись из List в базу данных
                }
            }
        }
    }


}


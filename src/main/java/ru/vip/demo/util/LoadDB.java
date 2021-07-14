package ru.vip.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.dto.ItemDto;
import ru.vip.demo.entity.Item;
import ru.vip.demo.entity.Node;
import ru.vip.demo.mapper.ItemMapper;
import ru.vip.demo.mapper.NodeMapper;
import ru.vip.demo.serviceimpl.NodeService;
import ru.vip.demo.serviceimpl.EstimateImpl;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Component
public class LoadDB {

    private final EstimateImpl repository;
    private final NodeService nodeService;
    private final ItemMapper itemMapper;
    private final NodeMapper nodeMapper;


    private int nTab = 0;
    private final int sizeTab = 4;
    private final String bufTab = " ".repeat(sizeTab * 12);

    private final int endBuf = bufTab.length();

    static private PrintWriter outFile;

    //////////////////////////////////////////////////////////////////////////////////////////////////
//
    private void doPrintItem(Item item){

        ItemDto itemDto = itemMapper.itemToItemDto(item);

        outFile.println(bufTab.substring(endBuf - (sizeTab * (nTab + 2)))
                + "item->itemDto "
                + "\" itemDto.name=\"" + itemDto.getName() + "\""
                + " <" + item.getId() + ">"
                + " \"" + ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME) + "\"");

    }

   private void doPrintNode(Node srcNode){

       ItemDto nodeDto = nodeMapper.nodeToItemDto(srcNode);

       outFile.println(bufTab.substring(endBuf - (sizeTab * nTab))
               + "<" + srcNode.getType().getName() + ">  "
               + srcNode.getName()
               + "\"  nodeDto.name=\"" + nodeDto.getName() + "\""
               + "<" + srcNode.getId() + ">  \"");
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
        List<Node> nodes = repository.getAllNode();
        try (PrintWriter outFl = new PrintWriter(out_json, StandardCharsets.UTF_8)) {
            outFile = outFl;
            for (Node node : nodes) {
                if (node.getName().equals(nameNode)) {
                    List<ItemDto> itemDtoList = nodeService.NodeTreeToItemDtoList(node);
                    nodeService.printListItemDto( out_json, itemDtoList);
                }
            }
            final long executionTime = System.currentTimeMillis() - start;
            System.out.println("\n // Время исполнения теста=[" + executionTime + "ms]" );
        } catch (Exception e) {
            System.out.println("** <writeNodeToJson> Ошибка сериализации в файл Json:" + e);
        }
    }
}


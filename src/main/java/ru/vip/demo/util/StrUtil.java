package ru.vip.demo.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StrUtil {

    private static final String charPoint = " ,()@{}=\"";

    public static String clearToPoint(String str, StringBuffer strOut) {
        int iOut = 0;
        strOut.setLength(0);
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if (charPoint.indexOf(str.charAt(i)) != -1) {
                if (!(iOut + 1 >= i)) {
                    strOut.append(str, iOut, i);
                }
                strOut.append(c);
            } else if (c != '.') {
                continue;
            }
            iOut = i + 1;
        }
        return String.valueOf(strOut);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
// Удаление комментариев и пустых строк из файла. Комментарии в формате "С++": "//"
// и упрощенный вариант "/* ... */" полные строки и неболее одного комм. в строке.
    public static void deleteComment(String in_nameFile, String out_nameFile) {

        try (BufferedReader inFile = new BufferedReader(new FileReader(in_nameFile, StandardCharsets.UTF_8));
             BufferedWriter outFile = new BufferedWriter(new FileWriter(out_nameFile, StandardCharsets.UTF_8))) {
            int i, n;
            String strIn, strOut;
            while ((strIn = inFile.readLine()) != null) {
                strOut = strIn;
                i = strIn.indexOf("//");
                n = strIn.indexOf("/*");
                if ((n == -1) | ((i >= 0) && (n > i))) {
                    if(i > 0) strOut = strIn.substring(0, i);
                } else {
                    strOut = strIn.substring(0, n);
                    if ((i = strIn.indexOf("*/", n)) != -1) {
                        strOut = strOut + strIn.substring(i + 2);
                        if ((i = strOut.indexOf("//")) != -1) { strOut = strOut.substring(0, i);}
                    } else if ((strOut = scanCommentLine(inFile)) == null) break; // конец файла.
                }
                if (strOut.trim().length() != 0) outFile.write(strOut + "\r\n"); // Непустая строка - сохранить.
            }
        } catch (IOException e) {
            System.out.println("** Ошибка удаления комментариев из Json inFile/outFile:\"" + in_nameFile + "\"/\"" + out_nameFile + "\"   " + e);
        }
    }

    private static String scanCommentLine(BufferedReader inFile) throws IOException {
        int i;
        String strIn;
        while ((strIn = inFile.readLine()) != null) {
            if ((i = strIn.indexOf("*/")) == -1) continue;
            strIn = strIn.substring(i + 2);
            break;
        }
        return strIn;
    }

}
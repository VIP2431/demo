package ru.vip.demo.parse;

import java.lang.management.BufferPoolMXBean;

public class SimpleName  {
    private StringBuffer strOut = new StringBuffer( 500);

    public String get( String str) {
       int i = 0;
       int iCopy = 0;
       int len = str.length();
       strOut.setLength( 0);
       String sPoint = " ,():;@#<>[]{}";
       for( i=0; i < len; ++i) {
            char c = str.charAt(i);
            if(sPoint.indexOf(c) != -1) {
                strOut.append(str.substring( iCopy, ++i));
                iCopy = i;
                if(c == ',') { strOut.append(' '); }
            } else if ( c == '.') {
                iCopy = ++i;
            }
        }
        if(len >= i) { strOut.append(str.substring( iCopy, i)); }
        return String.valueOf(strOut);
    }
}

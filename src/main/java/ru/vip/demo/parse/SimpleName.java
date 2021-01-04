package ru.vip.demo.parse;

import java.lang.management.BufferPoolMXBean;

public class SimpleName  {
    private StringBuffer strOut = new StringBuffer( 500);

    public String get( String str) {
       int i = 0;
       int iOut = 0;
       int len = str.length();
       strOut.setLength( 0);
       String charPoint = " ,():-+;@#<>[]{}";
       for( i=0; i < len; ++i) {
            char c = str.charAt(i);
            if(charPoint.indexOf(c) != -1) {
                strOut.append(str.substring( iOut, ++i));
                iOut = i;
                if(c == ',') { strOut.append(' '); }
            } else if ( c == '.') {
                iOut = ++i;
            }
        }
        if(len >= i) { strOut.append(str.substring( iOut, i)); }
        return String.valueOf(strOut);
    }
}

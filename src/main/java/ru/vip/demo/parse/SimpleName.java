package ru.vip.demo.parse;

public class SimpleName  {
    private StringBuffer strOut = new StringBuffer( 500);

    public String get( String str) {
       int i = 0;
       int iOut = 0;
       int len = str.length();
       strOut.setLength( 0);
       String charPoint = " ,()@{}=\"";
       char c;
       for( i=0; i < len; ++i) {
            c = str.charAt(i);
            if(charPoint.indexOf(c) != -1) {
                if (!(iOut + 1 >= i)) { strOut.append(str.substring(iOut, i)); }
                strOut.append(c);
            }else if( c != '.') { continue; }
            iOut = i + 1;
       }
       return String.valueOf(strOut);
    }
}

package com.honda.galc.client.gts.view;

import java.awt.Component;
import java.awt.HeadlessException;
import java.util.StringTokenizer;

public class MultiLineMessageDialog {
    
    public static void showMessageDialog(Component parentComponent,
                    Object message, String title, int messageType,int count)
                    throws HeadlessException {
        
        
    }
    
    private static String getHtmlText(String str, int count){
        
        if(str == null) return "";
        
        StringTokenizer st = new StringTokenizer(str, " ");
        StringBuilder buf = new StringBuilder();
        buf.append("<html>");
        int k = 0;
        for(int i=0; st.hasMoreTokens();i++){
            String s = st.nextToken();
            if(k + s.length() >= count && k > 0) {
                buf.append("<br>");
                k = 0;
            }
            buf.append(s);
            buf.append(" ");
            k += s.length();
        }
        buf.append("</html>");
        return buf.toString();
        
    }

}

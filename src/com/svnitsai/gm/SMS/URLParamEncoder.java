package com.svnitsai.gm.SMS;

/*
 * URLParamEncoder.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Encodes parameter sent on URL inline; eg. replace space by %20
 * 
 */
public class URLParamEncoder {

    public static String encode(String input) {
        StringBuilder resultStr = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (isUnsafe(ch)) {
                resultStr.append('%');
                resultStr.append(toHex(ch / 16));
                resultStr.append(toHex(ch % 16));
            } else {
            	if (ch == '\\') {
            		resultStr.append('%');
            		resultStr.append('5');
            		resultStr.append('C');
            	} else resultStr.append(ch);
            }
        }
        return resultStr.toString();
    }

    private static char toHex(int ch) {
        return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
    }

    private static boolean isUnsafe(char ch) {
        if (ch > 128 || ch < 0)
            return true;
        return "`~^[]{}'|\" %$&+,/:;=?@<>#%".indexOf(ch) >= 0;
    }
//	public static void main(String[] args) {
//		//String inString = " \" < > & + # % * ! , \' \\ = $";
//		String inString = "\\~!@#$%^&*()_+`-=<>,.:;?/\"'[]{}|\\";
//		System.out.println(" in string: " + inString);
//		System.out.println("out string: " + encode(inString));
//	}
}


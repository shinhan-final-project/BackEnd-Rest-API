package com.shinhan.friends_stock.utils;

public class HtmlEscapeUtil {

    public static String unescapeHtml(String input) {
        return input
                .replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "")
                .replaceAll("\n|&amp;|&lt;|&gt;|&quot;|&apos;|&nbsp;", "");
    }

}

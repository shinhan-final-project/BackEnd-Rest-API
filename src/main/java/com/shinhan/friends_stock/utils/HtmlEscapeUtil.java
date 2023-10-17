package com.shinhan.friends_stock.utils;

public class HtmlEscapeUtil {

    public static String unescapeHtml(String input) {
        return input
                .replace("\n", "<br>")
                .replace("&nbsp;", "<br>")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&apos;", "'")
                .replace("&amp;", "&");
    }

}

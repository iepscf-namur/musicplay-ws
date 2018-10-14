package WS.utils;

import org.unbescape.html.HtmlEscape;

public class EscapeUtils {

    public static String html2text(String str) {
        return HtmlEscape.escapeHtml5(str);
    }
}

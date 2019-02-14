package util;

/**
 * @author Hanyu King
 * @since 2018-12-28 16:43
 */
public class RemoveHTMLTag {
    /**
     * 删除所有的HTML标签
     *
     * @param source 需要进行除HTML的文本
     * @return
     */
    public static String deleteAllHTMLTag(String source) {

        if(source == null) {
            return "";
        }

        String s = source;
        /** 删除普通标签  */
        s = s.replaceAll("<(S*?)[^>]*>.*?|<.*? />", "");
        /** 删除转义字符 */
        s = s.replaceAll("&.{2,6}?;", "");
        return s;
    }

    public static void main(String[] args) {
        String html = "<head>\n" +
                "\t\t<meta charset=\"UTF-8\">\n" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "<title>统一工作平台</title>\n" +
                "<link href=\"/resource/img/favicon.ico\" type=\"image/x-icon\" rel=\"shortcut icon\">\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/common.css?v=20181228\">\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/main.css?v=20181228\">\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/notice.css?v=20181228\">\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/jquery-ui.min.css?v=20181228\">\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/jquery.validator.css?v=20181228\">\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/zTreeStyle.css?v=20181228\">\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/powerFloat.css?v=20181228\">\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/codemirror.css?v=20181228\">\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/jquery.treetable.css?v=20181228\">\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/chosen.css?v=20181228\">\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/bootstrap-switch.min.css?v=20181228\">\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/js/lib/jquery-multiselect/jquery.multiselect.css\">\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/common/jquery.multiselector.plugin.css?v=20181228\">\n" +
                "<link href=\"/resource/js/lib/jquery-magicsuggest/magicsuggest-min.css\" rel=\"stylesheet\">\n" +
                "\n" +
                "\n" +
                "<!--<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/bootstrap.min.css?v=20181228\" />\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/font-awesome.min.css?v=20181228\"  />\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/ace.min.css?v=20181228\" />\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/ace-rtl.min.css?v=20181228\" />\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/ace-skins.min.css?v=20181228\" />\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/generics.css?v=20181228\" />\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/page.css?v=20181228\" />\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/codemirror.css?v=20181228\" />\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/lib/jquery.treetable.css?v=20181228\" />\n" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"/resource/css/common/jone.css?v=20181228\" />-->\n" +
                "<script type=\"text/javascript\" async=\"\" src=\"//wl.jd.com/joya.js\"></script><script type=\"text/javascript\" src=\"/resource/js/lib/sea.min.js?v=20181228\" id=\"seajsnode\"></script>\n" +
                "<script type=\"text/javascript\" src=\"/resource/js/lib/seajs-preload.min.js?v=20181228\"></script>\n" +
                "<script type=\"text/javascript\" src=\"/resource/js/sea-config.js?v=20181228\"></script>\n" +
                "\n" +
                "\n" +
                "<script type=\"text/javascript\">\n" +
                "\tvar contextPath = '';\n" +
                "\tconfigInit();\n" +
                "</script>\n" +
                "<script type=\"text/javascript\">\n" +
                "    window.jdpts={};jdpts._st=new Date().getTime();\n" +
                "</script>\n" +
                "    </head>";
        System.out.println(deleteAllHTMLTag(html).replaceAll("\n", ""));
    }
}

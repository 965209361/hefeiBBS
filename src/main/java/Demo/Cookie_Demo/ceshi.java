package Demo.Cookie_Demo;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ceshi {
    public static void main(String[] args) {
        try {
            firefox("http://home.firefoxchina.cn/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void firefox(String url) throws IOException {
        Document doc = null;
        Elements elems = null;
        Map<String, String> map = new HashMap<String, String>();
        doc = Jsoup.connect(url).get();
        elems = doc.select(".tab-detail .item-list a");
        String content = null;
        try {
            for (Element item : elems) {
                doc = Jsoup.connect(item.attr("href")).get();
                content = doc.select(".content").first().text();
                if(StringUtil.isBlank(content)){
                    continue;
                }
                map.put(item.attr("href"), content);
            }
        } catch (NullPointerException e) {
            System.out.println("网站空指针");
        }
        for (Map.Entry<String, String> item : map.entrySet()) {
            System.out.println("\n" + "网址：" + item.getKey() + "\n" + "内容：" + item.getValue());
        }
    }

}

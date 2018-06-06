package Demo.Cookie_Demo;

import org.jsoup.Jsoup;
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
        for (Element item : elems) {
            try {
                doc = Jsoup.connect(item.attr("href")).get();
                content = doc.select(".content").first().text();
                System.out.println("标题:" + item.text() + "\t内容:" + content);
                map.put(item.attr("href"), content);
            } catch (NullPointerException e) {
                System.out.println("网站空指针");
                continue;
            }
        }
        /*for (Map.Entry<String, String> item : map.entrySet()) {
            System.out.println("\n" + "网址：" + item.getKey() + "\n" + "内容：" + item.getValue());
        }*/
    }

}

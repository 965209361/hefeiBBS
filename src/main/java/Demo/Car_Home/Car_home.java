package Demo.Car_Home;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import redis.clients.jedis.Jedis;

import java.io.IOException;

/**
 * created by zengqintao on 2018-04-25 9:44 .
 **/
public class Car_home {

    public static void main(String[] args) {
        Car_home car_home = new Car_home();
//        String url = "https://k.autohome.com.cn/135/ge0/0-0-2/?pvareaid=2099118#dataList";
        String url = "https://k.autohome.com.cn/135/ge0/0-0-2/index_%s.html?pvareaid=2099118#dataList";
        for (int i = 1; i <= 5; i++) {
            car_home.Start(String.format(url, i));
        }
    }

    public void Start(String url) {
        Document doc = null;
        Elements elems = null;
        String webName = "之家";
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        try {
            doc = Jsoup.connect(url).get();
            elems = doc.select(".cont-title .title-name b a");
//            StringBuffer stringBuffer = new StringBuffer("insert into car_home(webName,title,fbsj,autor,listurl,content) values");
            for (Element item : elems) {
                String listurl = "https:" + item.attr("href");
                doc = Jsoup.connect(listurl).get();
                String title = null;
//                String fbsj = null;
                String autor = null;
                try {
                    title = doc.select(".kou-tit h3").first().text();
//                    fbsj = doc.select(".title-name b").first().text();
                    autor = doc.select(".mouth-title-end h1 a").text();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    continue;
                }
                if (StringUtils.isBlank(title)) {
                    title = "标题空";
                }
                String content = doc.select(".text-con").text();
                jedis.append(title, autor + "|||" + content);
//                stringBuffer.append(String.format("('%s','%s','%s','%s','%s','%s'),", webName, title, fbsj, autor, listurl, content));
            }
            //region 存入数据库
            /*String sql = stringBuffer.toString().substring(0, stringBuffer.length() - 1) + ";";
            Boolean boo = DbUtils.insert(sql);
            if (boo) {
                System.out.println("存入成功!");
            } else {
                System.out.println("存入失败!");
            }*/
            //endregion
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

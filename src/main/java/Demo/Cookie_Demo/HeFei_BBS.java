/*
package Demo.Cookie_Demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class HeFei_BBS {
    private static Properties PropKit = HeFei_BBS.prop("weibo/jdbc.properties");

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(6);
        pool.execute(() -> {
            Document doc = null;
            Document docList = null;
            Elements elems = null;
            Elements elemsList = null;
            String listUrl = null;
            String title = null;
            String content = null;
            String fbsj = null;
            StringBuffer buffer = new StringBuffer("insert into hefei_bbs(listUrl,title,content,fbsj,comment,commentCount) values");
            try {
                doc = Jsoup.connect("http://bbs.hefei.cc/forum-196-1.html").get();
                elems = doc.select("tbody[id^=\"normalthread\"] .xst");
                for (int k = 0; k < elems.size(); k++) {
                    listUrl = elems.get(k).attr("href");
                    title = elems.get(k).text();
                    docList = Jsoup.connect(listUrl).get();
                    content = docList.select(".pcb").first().text();
                    fbsj = docList.select(".hm span:first-child").text();
                    elemsList = docList.select(".pcb");
                    List<String> list = new ArrayList<>();
                    if (fbsj == null || content == null || title == null) {
                        continue;
                    }
                    if (elemsList.size() == 1) {
                        if (k < elems.size() - 1) {
                            buffer.append(String.format("('%s','%s','%s','%s','%s',%s),", listUrl, title, content, fbsj, "空", 0));
                        } else {
                            buffer.append(String.format("('%s','%s','%s','%s','%s',%s);", listUrl, title, content, fbsj, "空", 0));
                        }
                    } else {
                        for (int i = 1; i < elemsList.size(); i++) {
                            list.add(i + " 楼:" + elemsList.get(i).text() + "\t||");
                        }
                        if (k < elems.size() - 1) {
                            buffer.append(String.format("('%s','%s','%s','%s','%s',%s),", listUrl, title, content, fbsj, list.toString(), elemsList.size()));
                        } else {
                            buffer.append(String.format("('%s','%s','%s','%s','%s',%s);", listUrl, title, content, fbsj, list.toString(), elemsList.size()));
                        }
                    }
                    list.clear();
                    System.out.println("------------------------------------------------");
                }
                if (insert(buffer.toString())>0){
                    System.out.println("存入成功");
                }else {
                    System.out.println("存入失败");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println(title + "出错!!!!!!!!!!!!!!");
            }
        });
        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    */
/**
     * 新增语句 编辑语句
     * @param sql
     * @return
     *//*

    public static int insert(String sql) {
        PreparedStatement prep = null;
        int num =0;
        try {
            prep = connection().prepareStatement(sql);
            num = prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }

    */
/**
     * 对数据库的id和网址进行查询
     * @return
     *//*

    public static Map<Integer,String> query(){
        PreparedStatement prep = null;
        String sql = "";
        Map<Integer,String> map =new HashMap<>();
        try {
            prep = connection().prepareStatement(sql);
            ResultSet resultSet= prep.executeQuery();
            while (resultSet.next()){
                map.put(resultSet.getInt("id"),resultSet.getString("listUrl"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    */
/**
     * 连接JDBC的Connection语句
     * @return
     *//*

    public static Connection connection(){
        Connection conn = null;
        try {
            Class.forName(PropKit.getProperty("driver"));
            conn = DriverManager.getConnection(PropKit.getProperty("url"), PropKit.getProperty("username"), PropKit.getProperty("password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static Properties prop(String name) {
        InputStream inputStream = HeFei_BBS.class.getClassLoader().getResourceAsStream(name);
        Properties prop = new Properties();
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
*/

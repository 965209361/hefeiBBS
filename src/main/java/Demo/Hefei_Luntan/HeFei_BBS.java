package Demo.Hefei_Luntan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
            String sql = null;
            try {
                doc = Jsoup.connect("http://bbs.hefei.cc/forum.php?mod=forumdisplay&fid=196&filter=author&orderby=dateline").get();
                elems = doc.select("tbody[id^=\"normalthread\"] .xst");
                for (int k = 0; k < elems.size(); k++) {
                    listUrl = elems.get(k).attr("href");
                    title = elems.get(k).text();
                    int nums = 0;
                    while (nums < 3) {
                        try {
                            docList = Jsoup.connect(listUrl).get();
                            break;
                        } catch (Exception e) {
                            nums++;
                        } finally {
                            while (nums == 3)
                                continue;
                        }
                    }
                    content = docList.select(".pcb").first().text();
                    fbsj = docList.select(".hm span:first-child").text().replace("发表于","").replace("更新于","");
                    elemsList = docList.select(".pcb");
                    List<String> list = new ArrayList<>();
                    if (fbsj == null || content == null || title == null) {
                        continue;
                    }
                    //region修改设置
                    /*if (tostring.contains(title)) {
                        if (elemsList.size() == 1) {
                            sql = String.format("update hefei_bbs set listUrl='%s',content='%s',fbsj='%s',comment='%s',commentCount=%s where title='%s';", listUrl, content, fbsj, "空", 0, title);
                            updateList.add(sql);
                        } else {
                            for (int i = 1; i < elemsList.size(); i++) {
                                list.add(i + " 楼:" + elemsList.get(i).text() + "\t||");
                            }
                            sql = String.format("update hefei_bbs set listUrl='%s',content='%s',fbsj='%s',comment='%s',commentCount=%s where title='%s';", listUrl, content, fbsj, list.toString(), elemsList.size(), title);
                            updateList.add(sql);
                        }
                        list.clear();
                        System.out.println("***************************");
                        continue;
                    }*/
                    //endregion
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
                    while (content.length() >= 100) {
                        content = content.substring(1, 100);
                    }
                    System.out.println("标题:" + title +"-------------内容: "+ content + "\t网址: " + listUrl);
                }
                String buffTo = buffer.toString();
                if (!buffTo.isEmpty()) {
                    buffTo.substring(buffTo.length() - 1, buffTo.length()).replaceAll("[,;]", ";");
                    int numInsert = insert(buffTo);
                    if (numInsert > 0) {
                        System.out.println(numInsert + "条数据存入成功");
                    } else {
                        System.out.println(numInsert + "条存入失败");
                    }
                }
                System.out.println("本次刷新结束!");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(title + "出错!!!!!!!!!!!!!!");
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

    /**
     * 新增语句 编辑语句
     *
     * @param sql
     * @return
     */
    public static int insert(String sql) {
        PreparedStatement prep = null;
        int num = 0;
        try {
            prep = connection().prepareStatement(sql);
            num = prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }

    /**
     * 对数据库的id和网址进行查询
     *
     * @return
     */
    public static List<String> query() {
        PreparedStatement prep = null;
        String sql = "SELECT title from hefei_bbs;";
        List<String> list = new ArrayList<>();
        try {
            prep = connection().prepareStatement(sql);
            ResultSet resultSet = prep.executeQuery();
            while (resultSet.next()) {
                list.add(resultSet.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 连接JDBC的Connection语句
     *
     * @return
     */
    public static Connection connection() {
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

package Demo.Cookie_Demo;


import name.iaceob.kit.httphelper.common.HttpConst;
import name.iaceob.kit.httphelper.entity.HttpEntity;
import name.iaceob.kit.httphelper.kit.HttpKit;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class getCookie {
    private static String URL = "jdbc:mysql://localhost:3306/local?useUnicode=true&characterEncoding=utf-8";
    private static String USER = "root";
    private static String PASSWORD = "";

    public static void main(String[] args) throws Exception {
        String html = null;
        Map<String, String> header = new HashMap<>();
        header.put(HttpConst.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        header.put(HttpConst.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");
        header.put(HttpConst.COOKIE, query());
        try {
            HttpEntity httpEntity = HttpKit.get("https://tieba.baidu.com/index.html", null, header, Charset.defaultCharset());
            html = httpEntity.getHtml();
            if (html.contains("天枰丶情狠真")) {
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void edit(String newCookie) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        //3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
        String s = "update `cookie` set newCookie="+newCookie+"where id=1 ";
        PreparedStatement pst = conn.prepareStatement(s);
        pst.execute();
        //关闭资源
        pst.close();
        conn.close();
    }

    public static String query() throws Exception {
        //1.加载驱动程序
        Class.forName("com.mysql.jdbc.Driver");
        //2.获得数据库链接
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        //3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
        String s = "SELECT oldcookie FROM `cookie`;";
        PreparedStatement pst = conn.prepareStatement(s);
        ResultSet rs = pst.executeQuery();
        String result = null;
        while (rs.next()) {  //ResultSet参数columnIndex索引从1开始,而不是0!
            result=rs.getString("oldcookie");
        }
        //关闭资源
        rs.close();
        pst.close();
        conn.close();
        return result;
    }
}

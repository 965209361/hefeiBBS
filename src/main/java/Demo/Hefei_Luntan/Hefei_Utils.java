package Demo.Hefei_Luntan;

import Demo.Domain.HeFeiBBS_Domain;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class Hefei_Utils {
    private static Properties proKit = Hefei_Utils.prop("weibo/jdbc.properties");


    /**
     * 获取配置
     *
     * @param propName
     * @return
     */
    public static Properties prop(String propName) {
        InputStream is = Hefei_Utils.class.getClassLoader().getResourceAsStream(propName);
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 连接
     *
     * @return
     */
    public static Connection conn() {
        Connection conn = null;
        try {
            Class.forName(proKit.getProperty("driver"));
            conn = DriverManager.getConnection(proKit.getProperty("url"), proKit.getProperty("username"), proKit.getProperty("password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 返回新的cookies
     *
     * @param webClient
     * @return
     */
    public static String getCookies(WebClient webClient) {
        Set<Cookie> cookies = webClient.getCookieManager().getCookies();
        StringBuffer buffer = new StringBuffer();
        for (Cookie cookie : cookies) {
            buffer.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        }
        return buffer.toString();
    }

    /**
     * 从数据库中调取相关新闻资料
     *
     * @return
     */
    public static List<HeFeiBBS_Domain> Query() {
        PreparedStatement prep = null;
        ResultSet resultSet = null;
//        String sql = "select listUrl,title,Content,comment from hefei_bbs;";
        String sql = "select listUrl,title,Content,comment from hefei_bbs ORDER BY fbsj DESC;";
        List<HeFeiBBS_Domain> list = new ArrayList<>();
        try {
            prep = conn().prepareStatement(sql);
            resultSet = prep.executeQuery();
            while (resultSet.next()) {
                HeFeiBBS_Domain hefei = new HeFeiBBS_Domain();
                hefei.setTitle(resultSet.getString("title"));
                hefei.setContent(resultSet.getString("Content"));
                hefei.setListUrl(resultSet.getString("listUrl"));
                hefei.setComment(resultSet.getString("comment"));
                list.add(hefei);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 新旧cookie进行修改
     *
     * @param oldCookie
     * @param newCookie
     * @return
     */
    public static int UpdateCookie(String oldCookie, String newCookie) {
        PreparedStatement prep = null;
        String sql = String.format("update hefei_bbscookie SET oldcookie ='%s',newcookie ='%s',updatetime=%s WHERE newcookie ='%s';", oldCookie, newCookie, "now()", oldCookie);
        int num = 0;
        try {
            prep = conn().prepareStatement(sql);
            num = prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }

    /**
     * 查询cookie
     *
     * @return
     */
    public static String QueryCookie() {
        PreparedStatement prep = null;
        String sql = "select newcookie from hefei_bbscookie;";
        ResultSet resultSet = null;
        String newCookie = null;
        try {
            prep = conn().prepareStatement(sql);
            resultSet = prep.executeQuery();
            while (resultSet.next()) {
                newCookie = resultSet.getString("newcookie");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newCookie;
    }
}

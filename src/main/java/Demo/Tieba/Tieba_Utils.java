package Demo.Tieba;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class Tieba_Utils {
    private static Properties proKit = prop("tieba/tieba.properties");

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

    public static Properties prop(String propName) {
        InputStream is = Tieba_Utils.class.getClassLoader().getResourceAsStream(propName);
        Properties prop = new Properties();
        try {
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    public static String getNewCookie() {
        PreparedStatement prep = null;
        String sql = "select newcookie from tieba_cookie;";
        String newCookie = null;
        try {
            prep = conn().prepareStatement(sql);
            ResultSet resultSet = prep.executeQuery();
            while (resultSet.next()) {
                newCookie = resultSet.getString("newcookie");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newCookie;
    }

}

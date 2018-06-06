package Demo.Car_Home;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

/**
 * created by zengqintao on 2018-04-25 9:45 .
 **/

public class DbUtils {
    private static Properties proKit = prop("car_home/car.properties");

    public static Properties prop(String name) {
        InputStream inputStream = DbUtils.class.getClassLoader().getResourceAsStream(name);
        Properties prop = new Properties();
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    public static Connection conn() {
        Connection conn = null;
        try {
            Class.forName(proKit.getProperty("car.jdbc"));
            conn = DriverManager.getConnection(proKit.getProperty("car.url"), proKit.getProperty("car.username"), proKit.getProperty("car.password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Boolean insert(String sql) {
        PreparedStatement prep = null;
        int num = 0;
        try {
            prep = conn().prepareStatement(sql);
            num = prep.executeUpdate();
            return num > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

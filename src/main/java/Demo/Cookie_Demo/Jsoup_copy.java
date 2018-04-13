//package cookie;
//
//import com.gargoylesoftware.htmlunit.BrowserVersion;
//import com.gargoylesoftware.htmlunit.CookieManager;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.gargoylesoftware.htmlunit.util.Cookie;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.logging.Level;
//
///**
// * Mr.zeng
// */
//public class JsoupTest {
//    private static Logger log = LoggerFactory.getLogger(JsoupTest.class);
//    private static Properties prop = JdbcConnection("cookie.properties");
//    private static String driver = prop.getProperty("JDBC.driver");
//    private static String URL = prop.getProperty("JDBC.url");
//    private static String USER = prop.getProperty("JDBC.username");
//    private static String PASSWORD = prop.getProperty("JDBC.password");
//    //间隔时间
//    private static long lastTime = Long.parseLong(prop.getProperty("lastTime"));
//    public static void main(String[] args) {
//        //定时任务，每隔11个小时重新开始任务
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm,ss");
//                try {
//                    generateCookieHU();
//                    System.out.println("刷新时间: " + simpleFormatter.format(System.currentTimeMillis()) + " 每隔11小时刷新一次");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        Calendar calendar = Calendar.getInstance();
//        Date firstTime = calendar.getTime();
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(timerTask, firstTime, lastTime);
//    }
//
//    public static void generateCookieHU() throws Exception {
//        StringBuffer buffer = new StringBuffer();
//        Map<String, String> mapDbquery = query();
//        for (Map.Entry<String, String> map : mapDbquery.entrySet()) {
//            try (WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52)) {
//                //通过query调用数据库中的老cookie
//                //规避script报错
//                webClient.getOptions().setThrowExceptionOnScriptError(false);
//                webClient.getOptions().setCssEnabled(false);
//                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
//                webClient.getOptions().setPrintContentOnFailingStatusCode(false);
//                java.util.logging.Logger.getLogger("net.sourceforge.htmlunit").setLevel(Level.OFF);
//                java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
//                //页面和标签绑定
//                webClient.addRequestHeader("Host", "tieba.baidu.com");
//                webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");
//                webClient.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//                String[] id_username=map.getKey().split("∞");
//                int id = Integer.parseInt(id_username[0]);
//                String  username = id_username[1];
//                String cook = map.getValue();
//                CookieManager cm = new CookieManager();
//                String[] column = cook.split(";");
//                for (int i = 0; i < column.length; i++) {
//                    String[] ico = column[i].split("=");
//                    if (column[i].contains("FG=1")) {
//                        cm.addCookie(new Cookie("tieba.baidu.com", ico[0], ico[1].replace("FG", "FG=1")));
//                    } else {
//                        cm.addCookie(new Cookie("tieba.baidu.com", ico[0], ico[1]));
//                    }
//                }
//                webClient.setCookieManager(cm);
//                HtmlPage logPage = null;
//                int cnt = 0;
//                while (cnt < 3) {
//                    try {
//                        logPage = webClient.getPage("https://tieba.baidu.com/index.html");
//                        break;
//                    } catch (Exception e) {
//                        cnt++;
//                    }
//                }
//
//                String html = logPage.asXml();
//                if (html.contains(username)){
//                    System.out.println("------用户名存在");
//                } else {
//                    log.error("cookie failed, 用户不存在");
//                }
//                logPage.refresh(); //刷新页面获取新cookie
//                CookieManager cookieManager = webClient.getCookieManager();
//                Set<Cookie> ckSet = cookieManager.getCookies();
//                for (Cookie cookie : ckSet)
//                    buffer.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
//                String newcookie = null;
//                newcookie = buffer.toString().replaceAll("BAI.*?FG=1;\\s", "").replaceAll("BAI.*?FG=1;(?=BAI)", "");
//                String updatesql = String.format("update cookie set oldcookie='%s',newcookie='%s',updatetime=%s WHERE id=%s;", cook, newcookie, "now()", id);
//                if (DBUpdate(updatesql)) {
//                    System.out.println("------新cookie修改成功");
//                } else {
//                    log.error("cookie failed, 修改失败,字段长度过长");
//                }
//                webClient.getCookieManager().clearCookies();
//                webClient.close();
//            } catch (IOException ioe) {
//                log.error("generate cookie failed, msg:", ioe);
//            } catch (RuntimeException re) {
//                log.error("generate cookie failed, msg:", re);
//            }
//        }
//    }
//
//    //查询旧的cookie方法
//    public static Map<String, String> query() throws Exception {
//        Map<String, String> map = new HashMap<>();
//        Class.forName(driver);
//        java.sql.Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//        String s = "SELECT id,username,newcookie FROM `cookie`;";
//        PreparedStatement pst = conn.prepareStatement(s);
//        ResultSet rs = pst.executeQuery();
//        String result = null;
//        String username=null;
//        int id = 0;
//        while (rs.next()) {
//            id = rs.getInt("id");
//            username = rs.getString("username");
//            result = rs.getString("newcookie");
//            map.put(id+"∞"+username, result);
//        }
//        rs.close();
//        pst.close();
//        conn.close();
//        return map;
//    }
//
//    //修改新cookie的操作
//    public static boolean DBUpdate(String sql) {
//        int resCnt = 0;
//        try {
//            Class.forName(driver);
//            java.sql.Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//            PreparedStatement pst = conn.prepareStatement(sql);
//            resCnt = pst.executeUpdate();
//            pst.close();
//            conn.close();
//            return resCnt > 0;
//        } catch (Exception e) {
//            log.error("DBUpdate error, " + e.toString());
//            return false;
//        }
//    }
//
//    public static Properties JdbcConnection(String name) {
//        InputStream inputStream = JsoupTest.class.getClassLoader().getResourceAsStream(name);
//        Properties prop = new Properties();
//        try {
//            prop.load(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return prop;
//    }
//
//}
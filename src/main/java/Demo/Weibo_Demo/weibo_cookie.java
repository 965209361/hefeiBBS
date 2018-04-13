package Demo.Weibo_Demo;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

import java.io.IOException;
import java.util.Set;

public class weibo_cookie {
    public static void main(String[] args) {
        run();
    }

    public static void run() {

        try {
            WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

            webClient.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");
            webClient.addRequestHeader("Host", "weibo.com");

            CookieManager cm = new CookieManager();
            String cookieValue = "UOR=news.ifeng.com,widget.weibo.com,www.baidu.com; SINAGLOBAL=1216602177565.9397.1522132919764; ULV=1522379044582:3:3:3:615919426352.5536.1522379044578:1522202361044; SCF=AgXb9z4t4L_rFN-QRVvibolw4xGprXgA9XfpVDDOsSj0mI39ePmJT0zx4VhEG1g0PhkAaR140r_EYtoLf4eIR-c.; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhULv1eVSuL_pi2sJOjAM315JpX5K2hUgL.FozcehzNSo501K-2dJLoIE9PUgpDUs8aUs8aPEH8Sb-4BbHFxCH81C-4ebHWSbH8SbHFSb-4Bntt; SUHB=0KAw9f15tQ-9XU; ALF=1553916637; un=17681138931; wvr=6; YF-Ugrow-G0=b02489d329584fca03ad6347fc915997; SUB=_2A253ud8xDeRhGeRI61AW9i7PwjmIHXVUzrf5rDV8PUNbmtBeLXfBkW9NUsUPrJX0tvLZzM06dzsUi5tmYVczuSA-; login_sid_t=4b6a30ec06c3f69fe50aabd2b8953dc9; cross_origin_proto=SSL; YF-V5-G0=16139189c1dbd74e7d073bc6ebfa4935; _s_tentry=passport.weibo.com; Apache=615919426352.5536.1522379044578; SSOLoginState=1522380638";
            String[] spilitCookie = cookieValue.split(";");
            for (int i = 0; i < spilitCookie.length; i++) {
                String[] item = spilitCookie[i].split("=");
                cm.addCookie(new Cookie("weibo.com", item[0], item[1]));
            }
            webClient.setCookieManager(cm);
            HtmlPage htmlPage = null;
            int num = 0;
            while (num < 3) {
                try {
                    htmlPage = webClient.getPage("https://weibo.com/");
                    break;
                } catch (IOException e) {
                    num++;
                }
            }
            String html = htmlPage.asXml();
            if (html.contains("曾钦涛")) {
                System.out.println("登陆成功");
            } else {
                System.out.println("登陆失败咯！");
            }
            htmlPage.refresh();
            webClient.getRefreshHandler();
            StringBuffer stringBuffer = new StringBuffer();
            CookieManager cookieManager =webClient.getCookieManager();
            Set<Cookie> setCoo = cookieManager.getCookies();
            for (Cookie cookie : setCoo) {
                stringBuffer.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
            }
            String newCookie = stringBuffer.toString();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

package Demo.Tieba;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.Scanner;

public class Tieba_HF {
    public static void main(String[] args) {
        Tieba_HF tieba_hf = new Tieba_HF();
        tieba_hf.run("http://tieba.baidu.com/p/5639914038");
    }

    public void run(String url) {
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        CookieManager cm = new CookieManager();
        String newCookie = Tieba_Utils.getNewCookie();
        String[] newCookieStr = newCookie.split(";");
        for (String str : newCookieStr) {
            String[] cookie = str.split("=");
            cm.addCookie(new Cookie("tieba.baidu.com", cookie[0], cookie[1]));
        }
        webClient.setCookieManager(cm);
        HtmlPage htmlPage = null;
        try {
            htmlPage = webClient.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc = Jsoup.parse(htmlPage.asXml());
        String title = doc.select(".core_title_txt").text();
        String content = doc.select(".d_post_content_main cc").first().text();
        Elements elems = doc.select(".d_post_content_main cc");
        StringBuffer buffer = new StringBuffer();
        for (int i = 1; i < elems.size(); i++) {
            buffer.append(i).append("楼:").append(elems.get(i)).append("----");
        }
        System.out.println("标题:"+title+"\n"+"内容:"+content+"\n"+"评论:"+buffer.toString());
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("您是否对此帖回复:(1回复2不回复)");
        int nums = scanner.nextInt();
        if (nums == 2) {
            System.out.println("本次浏览结束");
            return;
        }
        String Pinglun = scanner.next();

    }
}

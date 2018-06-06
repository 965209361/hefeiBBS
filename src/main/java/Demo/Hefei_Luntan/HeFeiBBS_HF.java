package Demo.Hefei_Luntan;

import Demo.Domain.HeFeiBBS_Domain;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.apache.commons.codec.binary.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;


public class HeFeiBBS_HF {
    /**
     * 启动资讯
     *
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<HeFeiBBS_Domain> listDomain = HeFei_Luntan();
        HeFeiBBS_HF heFeiBBSHF = new HeFeiBBS_HF();
        System.out.println("皇上上朝,有事启奏,无事退朝!");
        System.out.println();
        for (HeFeiBBS_Domain item : listDomain) {
            System.out.println("标  题: " + item.getTitle() + " 发布时间:" + item.getFbsj());
            System.out.println("内  容: " + item.getContent());
            System.out.println("回复数:" + item.getCommentCount() + "\t回复内容:" + item.getComment());
            System.out.println();
            System.out.println("皇上是否批阅此奏折:(1批阅--2查看网址--3退出)");
            String isTrue = scanner.next();
            if (isTrue.equals("1")) {
                System.out.println("正为吾皇打开奏折,皇上请稍后...");
                heFeiBBSHF.pinglun(item.getListUrl(), item.getTitle());
            } else if (isTrue.equals("2") || isTrue.equals("url")) {
                System.out.println("网址详情为: " + item.getListUrl());
            } else if (isTrue.equals("3") || isTrue.equals("bye")) {
                break;
            }
            System.out.println("陛下,微臣为您翻阅下一条奏章,请皇上稍后...");
            System.out.println();
        }
        System.out.println("退朝!");
    }

    //region原版本
    /*public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<HeFeiBBS_Domain> listDomain = Hefei_Utils.Query();
        HeFeiBBS_HF hefei_hf = new HeFeiBBS_HF();
        System.out.println("皇上上朝,有事启奏,无事退朝!");
        for (HeFeiBBS_Domain item : listDomain) {
            System.out.println("标  题: " + item.getTitle());
            System.out.println("内  容: " + item.getContent());
            System.out.println("刁民回复:" + item.getComment());
            System.out.println();
            System.out.println("皇上是否批阅此奏折:(1批阅2滚)");
            String isTrue = scanner.next();
            if (isTrue.equals("1")) {
                System.out.println("皇上请稍后...");
                hefei_hf.pinglun(item.getListUrl(), item.getTitle());
            }
            System.out.println("陛下,微臣为您翻阅下一条奏章,请皇上稍后...");
            System.out.println();
        }
        System.out.println("退朝!");

    }*/
    //endregion
    int num = 1;

    /**
     * 评论资讯
     *
     * @param url
     * @param title
     */
    public void pinglun(String url, String title) {
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
//        webClient.getOptions().setJavaScriptEnabled(false); //关闭JS
        webClient.getOptions().setCssEnabled(false); //禁用Css,可避免自动二次请求CSS进行渲染
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        /**
         * 去除CSS相关控制台警告
         */
        java.util.logging.Logger.getLogger("net.sourceforge.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        String cookie = Hefei_Utils.QueryCookie();
        String[] cookieArr = cookie.split(";");
        CookieManager cookieManager = new CookieManager();
        for (int i = 0; i < cookieArr.length; i++) {
            String[] cookieStr = cookieArr[i].split("=");
            cookieManager.addCookie(new Cookie("bbs.hefei.cc", cookieStr[0], cookieStr[1]));
        }
        webClient.setCookieManager(cookieManager);
        HtmlPage htmlPage = null;
        try {
            htmlPage = webClient.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HtmlForm form = (HtmlForm) htmlPage.getElementById("fastpostform");
        Scanner scanner = new Scanner(System.in);
        System.out.println(title + "\t请皇上批阅奏章:(不少于5个字)");
        String PingLun = scanner.next();
        HtmlTextArea input = form.getTextAreaByName("message");
        input.setText(PingLun);
        HtmlButton submit = form.getButtonByName("replysubmit");
        try {
            submit.click();
            htmlPage.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (num == 1) {
            String newCookies = Hefei_Utils.getCookies(webClient);
            int updateNum = Hefei_Utils.UpdateCookie(cookie, newCookies);
            if (updateNum > 0) {
                System.out.println("cookie存入成功________________________");
            } else {
                break;
            }
            num++;
        }
        String pageText = htmlPage.asXml();
        System.out.println("输入字体长度:" + PingLun.length());
        if (pageText.contains(PingLun) && PingLun.length() >= 5) {
            System.out.println("O(∩_∩)O奏章批阅成功--请点击详情页查看→\t" + url);
        } else {
            System.out.println("😔（＞人＜；）奏章批阅失败,来人呐,推出去" + "\n" + "是否重新评论:(1重新评论2退出)");
            System.out.println();
            int rePingLun = scanner.nextInt();
            if (rePingLun == 1) {
                System.out.println(title + "\t请皇上重新批阅奏章:(不少于5个字)");
                PingLun = scanner.next();
                input.setText(PingLun);
                try {
                    submit.click();
                    htmlPage.refresh();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pageText = htmlPage.asXml();
                if (pageText.contains(PingLun) && PingLun.length() >= 5) {
                    System.out.println("O(∩_∩)O奏章批阅成功--请点击详情页查看→\t" + url);
                } else {
                    System.out.println("😔（＞人＜；）奏章都不会批,换奏折");
                }
            } else {
                System.out.println("退出此奏折!" + url);
            }
        }

        webClient.close();
    }

    /**
     * 采集资讯
     *
     * @return
     */
    public static List<HeFeiBBS_Domain> HeFei_Luntan() {
        System.out.println("奏折采集中...请皇上稍等");
        Document doc = null;
        Document docList = null;
        Elements elems = null;
        Elements elemsList = null;
        String listUrl = null;
        String title = null;
        String content = null;
        String fbsj = null;
        String comment = null;
        StringBuffer buffer = new StringBuffer();
        List<HeFeiBBS_Domain> hefeiList = new ArrayList<>();
        try {
            doc = Jsoup.connect("http://bbs.hefei.cc/forum.php?mod=forumdisplay&fid=196&filter=author&orderby=dateline").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            fbsj = docList.select(".hm span:first-child").text().replace("发表于", "").replace("更新于", "");
            elemsList = docList.select(".pcb");
            List<String> list = new ArrayList<>();
            if (fbsj == null || content == null || title == null) {
                continue;
            }
            if (elemsList.size() == 1) {
                comment = "未评论";
            } else {
                for (int i = 1; i < elemsList.size(); i++) {
                    list.add((i + 1) + " 楼:" + elemsList.get(i).text() + "\t||");
                }
                comment = list.toString();
                list.clear();
            }
            HeFeiBBS_Domain heFeiBBS_domain = new HeFeiBBS_Domain();
            heFeiBBS_domain.setListUrl(listUrl);
            heFeiBBS_domain.setTitle(title);
            heFeiBBS_domain.setFbsj(fbsj);
            heFeiBBS_domain.setContent(content);
            heFeiBBS_domain.setComment(comment);
            heFeiBBS_domain.setCommentCount(elemsList.size() - 1);
            hefeiList.add(heFeiBBS_domain);
            if (hefeiList.size() == 40) {
                System.out.println("奏折已采集40份,请君稍后...");
            }


        }
        System.out.println("奏章整理完毕,准备上朝!");
        return hefeiList;
    }

}

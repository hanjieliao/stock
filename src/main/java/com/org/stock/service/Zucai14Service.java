package com.org.stock.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 足彩14场数据
 * @author: jianchen
 * @date 2024/04/11
 */
public class Zucai14Service {


    public static void main(String[] args) {
        String url = "https://m.okooo.com/zucai/";
        HttpRequest httpRequest = HttpRequest.get(url);
        HttpResponse httpResponse = httpRequest.execute();
        String json = httpResponse.body();

        Document document = Jsoup.parse(json);
        Elements elements = document.select("#lotteryBox > div > div.list > a");

        for (Element element : elements) {
            String text = element.text().replace("第", "").replace("期", "");
            doSpider(text);
        }
    }


    public  static void doSpider(String id) {
        String url = "https://m.okooo.com/zucai/?LotteryNo=" + id;
        HttpRequest httpRequest = HttpRequest.get(url);
        HttpResponse httpResponse = httpRequest.execute();
        String json = httpResponse.body();

        Document document = Jsoup.parse(json);
        Elements elements = document.select("#match_list > div.listItemjczucai > div > div.width320");

        for (Element element : elements) {
            //联赛
            String liansai = element.select(".liansai").get(0).text();
            //数据
            String datalink = element.select(".datalink").get(0).attr("href");
            //主队
            String homename = element.select(".ctrl_homename").get(0).text();
            //客队
            String awayname = element.select(".ctrl_awayname").get(0).text();

            //结果
            String result = null;

            //胜赔率
            String win = element.selectFirst("#lno1s").selectFirst("p").text();
            if(element.selectFirst("#lno1s").selectFirst("p").attribute("class").getValue().contains("listbetbtnSed")){
                result = win;
            }
            //平赔率
            String pin = element.selectFirst("#lno1p").selectFirst("p").text();
            if(element.selectFirst("#lno1p").selectFirst("p").attribute("class").getValue().contains("listbetbtnSed")){
                result = pin;
            }
            //负赔率
            String fu = element.selectFirst("#lno1f").selectFirst("p").text();
            if(element.selectFirst("#lno1f").selectFirst("p").attribute("class").getValue().contains("listbetbtnSed")){
                result = fu;
            }

            System.out.println(result);
        }
    }


}

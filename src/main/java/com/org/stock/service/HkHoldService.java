package com.org.stock.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.org.stock.repository.StockRepository;
import com.org.stock.repository.entity.Stock;
import org.nutz.dao.Cnd;
import org.nutz.mvc.annotation.GET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 港仔持仓分析
 */
@Component
public class HkHoldService {

    @Autowired
    private StockRepository stockRepository;

    /**
     * 抓数据
     * 30秒执行一次
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void loadStock(){
        doSpider1();
        doSpider2();
    }


    public void doSpider1(){
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        int pageNum = 1;
        while (pageNum < 2000){
            String requestUrl = "https://datacenter-web.eastmoney.com/api/data/v1/get?sortColumns=ADD_MARKET_CAP&sortTypes=-1&pageSize=50&pageNumber="+pageNum+"&reportName=RPT_MUTUAL_STOCK_NORTHSTA&columns=ALL&source=WEB&client=WEB&filter=(TRADE_DATE%3D%272023-10-20%27)(INTERVAL_TYPE%3D%221%22)%20Request%20Method:%20GET";
            HttpRequest httpRequest = HttpRequest.get(requestUrl);
            HttpResponse httpResponse = httpRequest.execute();
            String json = httpResponse.body();
            JSONObject jsonObject = JSON.parseObject(json);
            JSONObject result = jsonObject.getJSONObject("result");
            int pages = result.getIntValue("pages");
            JSONArray data = result.getJSONArray("data");
            for (int i = 0; i < data.size(); i++) {
                JSONObject target = data.getJSONObject(i);

                String code = target.getString("SECURITY_CODE");
                String securityName = target.getString("SECURITY_NAME");
                String totalSharesRatio = target.getString("TOTAL_SHARES_RATIO");

                System.out.println(securityName);
                Stock stock = stockRepository.get(code);
                if(stock == null){
                    stock = new Stock();
                    stock.setId(code);
                    stock.setName(securityName);
                    stock.setTotalSharesRatio(Double.parseDouble(totalSharesRatio));
                    stockRepository.insert(stock);
                }else{
                    stock.setTotalSharesRatio(Double.parseDouble(totalSharesRatio));
                    stockRepository.update(stock);
                }
            }

            if(pageNum >= pages){
                break;
            }

            pageNum++;
        }
    }

    public void doSpider2(){
        int pageNum = 1;
        while (pageNum < 2000){
            String requestUrl = "https://datacenter-web.eastmoney.com/api/data/v1/get?sortColumns=HOLD_NOTICE_DATE%2CSECURITY_CODE&sortTypes=-1%2C-1&pageSize=50&pageNumber=" + pageNum  + "&reportName=RPT_HOLDERNUMLATEST&columns=SECURITY_CODE%2CSECURITY_NAME_ABBR%2CEND_DATE%2CINTERVAL_CHRATE%2CAVG_MARKET_CAP%2CAVG_HOLD_NUM%2CTOTAL_MARKET_CAP%2CTOTAL_A_SHARES%2CHOLD_NOTICE_DATE%2CHOLDER_NUM%2CPRE_HOLDER_NUM%2CHOLDER_NUM_CHANGE%2CHOLDER_NUM_RATIO%2CEND_DATE%2CPRE_END_DATE&quoteColumns=f2%2Cf3&quoteType=0&source=WEB&client=WEB";
            HttpRequest httpRequest = HttpRequest.get(requestUrl);
            HttpResponse httpResponse = httpRequest.execute();
            String json = httpResponse.body();
            JSONObject jsonObject = JSON.parseObject(json);
            JSONObject result = jsonObject.getJSONObject("result");
            int pages = result.getIntValue("pages");
            JSONArray data = result.getJSONArray("data");
            for (int i = 0; i < data.size(); i++) {
                JSONObject target = data.getJSONObject(i);

                String code = target.getString("SECURITY_CODE");
                String holder_num = target.getString("HOLDER_NUM");
                Long totalMarketCap = target.getLong("TOTAL_MARKET_CAP");

                System.out.println("SECURITY_NAME_ABBR " + target.getString("SECURITY_NAME_ABBR"));

                Stock stock = stockRepository.get(code);
                if(stock != null){
                    stock.setNum(Long.parseLong(holder_num));
                    if(totalMarketCap != null){
                        stock.setTotalValue(totalMarketCap/100000000L);
                    }else{
                        System.out.println(target);
                    }
                    stockRepository.update(stock);
                }
            }

            if(pageNum >= pages){
                break;
            }

            pageNum++;
        }
    }
}

package com.org.stock.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.org.stock.repository.ContractDataRepository;
import com.org.stock.repository.EtfContractRepository;
import com.org.stock.repository.EtfRepository;
import com.org.stock.repository.entity.ContractData;
import com.org.stock.repository.entity.Etf;
import com.org.stock.repository.entity.EtfContract;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 东财爬虫服务
 */
@Slf4j
@Component
public class SpiderService {

    @Autowired
    private EtfRepository etfRepository;
    @Autowired
    private EtfContractRepository etfContractRepository;
    @Autowired
    private ContractDataRepository contractDataRepository;
    @Autowired
    private AnalysisService analysisService;


    /**
     * 抓etf数据
     * 30秒执行一次
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void loadEtfContract(){
        log.debug("开始抓取数据");
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        //初始化etf列表 创业板 深中证500 深沪深300 上证50 沪中证500 沪沪深300
        String[] secids = new String[]{"0.159915", "0.159922", "0.159919", "1.510050", "1.510500", "1.510300"};
        for (String secid : secids) {
            Etf etf = buildOrUpdateDCEtf(secid, dateStr);
            List<EtfContract> etfContracts = buildOrUpdateDCEtfContract(etf);
            for (EtfContract etfContract : etfContracts) {
                updateContractData(etf, etfContract, dateStr);
                analysisService.analysisEtfContract(etfContract.getId());
            }
        }

        log.debug("完成抓取数据。。。");
    }


    /**
     * 更新合约数据
     * @param etfContract
     */
    private void updateContractData(Etf etf, EtfContract etfContract, String dateStr){
        String url = "https://7.push2.eastmoney.com/api/qt/slist/get?secid="+ etfContract.getSecid()+"&exti="+ etfContract.getExpiredDay()/100 +"&spt=9&fltt=2&invt=2&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fields=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf12%2Cf13%2Cf14%2Cf108%2Cf152%2Cf161%2Cf249%2Cf250%2Cf330%2Cf334%2Cf339%2Cf340%2Cf341%2Cf342%2Cf343%2Cf344%2Cf345%2Cf346%2Cf347&fid=f161&pn=1&pz=20&po=0&wbp2u=8394365578044180%7C0%7C1%7C0%7Cweb&_=" + System.currentTimeMillis();
        HttpRequest httpRequest = HttpRequest.get(url);
        HttpResponse httpResponse = httpRequest.execute();
        String json = httpResponse.body();
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray diff = data.getJSONArray("diff");
        for (int i = 0; i < diff.size(); i++) {
            JSONObject jsonObject1 = diff.getJSONObject(i);

            //认购code
            String gouCode = jsonObject1.getString("f12");
            //认沽code
            String guCode = jsonObject1.getString("f12");

            //认购名
            String gouName = jsonObject1.getString("f14");
            //当前认购价
            String curGouPrice = jsonObject1.getString("f2");
            //认购涨跌额
            String gouChg = jsonObject1.getString("f4");
            //认购涨跌比
            String gouChgRate = jsonObject1.getString("f3");

            //行权价
            BigDecimal exercisePrice = new BigDecimal(jsonObject1.getString("f161")).setScale(3, BigDecimal.ROUND_HALF_UP);

            //认沽名
            String guName = jsonObject1.getString("f340");
            //当前认沽价
            String curGuPrice = jsonObject1.getString("f341");
            //认沽涨跌额
            String guChg = jsonObject1.getString("f342");
            //认沽涨跌比
            String guChgRate = jsonObject1.getString("f343");

            //购内在价值
            BigDecimal gouRealValue = etf.getCurPrice().subtract(exercisePrice).setScale(4, BigDecimal.ROUND_HALF_UP);
            gouRealValue = gouRealValue.doubleValue() > 0? gouRealValue : new BigDecimal("0.0000");
            //购时间价值
            BigDecimal gouTimeValue = new BigDecimal(curGouPrice.equals("-")? "0.0000" : curGouPrice).subtract(gouRealValue).setScale(4, BigDecimal.ROUND_HALF_UP);


            //沽内在价值
            BigDecimal guRealValue = exercisePrice.subtract(etf.getCurPrice()).setScale(4, BigDecimal.ROUND_HALF_UP);
            guRealValue = guRealValue.doubleValue() > 0? guRealValue : new BigDecimal("0.0000");
            //沽时间价值
            BigDecimal guTimeValue = new BigDecimal(curGuPrice.equals("-")? "0.0000" : curGuPrice).subtract(guRealValue).setScale(4, BigDecimal.ROUND_HALF_UP);

            ContractData contractData = contractDataRepository.findByEtfContractIdAndCode(etfContract.getId(), gouCode, guCode, dateStr);
            if(contractData == null){
                contractData = new ContractData();
                contractData.setEtfContractId(etfContract.getId());
                contractData.setGouCode(gouCode);
                contractData.setGuCode(guCode);
                contractData.setDateStr(dateStr);
                contractData.setGouName(gouName);
                contractData.setCurGouPrice(curGouPrice);
                contractData.setGouRealValue(gouRealValue);
                contractData.setGouTimeValue(gouTimeValue);
                contractData.setGouChg(gouChg);
                contractData.setGouChgRate(gouChgRate + "%");
                contractData.setExercisePrice(exercisePrice);
                contractData.setGuName(guName);
                contractData.setCurGuPrice(curGuPrice);
                contractData.setGuRealValue(guRealValue);
                contractData.setGuTimeValue(guTimeValue);
                contractData.setGuChg(guChg);
                contractData.setGuChgRate(guChgRate + "%");
                contractDataRepository.insert(contractData);
            }else{
                contractData.setGouName(gouName);
                contractData.setCurGouPrice(curGouPrice);
                contractData.setGouRealValue(gouRealValue);
                contractData.setGouTimeValue(gouTimeValue);
                contractData.setGouChg(gouChg);
                contractData.setGouChgRate(gouChgRate + "%");
                contractData.setExercisePrice(exercisePrice);
                contractData.setGuName(guName);
                contractData.setCurGuPrice(curGuPrice);
                contractData.setGuRealValue(guRealValue);
                contractData.setGuTimeValue(guTimeValue);
                contractData.setGuChg(guChg);
                contractData.setGuChgRate(guChgRate + "%");
                contractDataRepository.update(contractData);
            }
        }
    }


    /**
     * 更新合约列表
     * @param etf
     */
    private List<EtfContract> buildOrUpdateDCEtfContract(Etf etf){
        List<EtfContract> etfContracts = new ArrayList<>();
        String url = "https://7.push2.eastmoney.com/api/qt/stock/get?mspt=1&secid="+ etf.getSecId() +"&ut=bd1d9ddb04089700cf9c27f6f7426281&_=" + System.currentTimeMillis();
        HttpRequest httpRequest = HttpRequest.get(url);
        HttpResponse httpResponse = httpRequest.execute();
        String json = httpResponse.body();
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray optionExpireInfo = data.getJSONArray("optionExpireInfo");
        for (int i = 0; i < optionExpireInfo.size(); i++) {
            JSONObject jsonObject1 = optionExpireInfo.getJSONObject(i);
            String expiredDay = jsonObject1.getString("date");
            String days = jsonObject1.getString("days");

            EtfContract etfContract = etfContractRepository.findByBaseCodeAndExpiredMonth(etf.getCode(), Integer.parseInt(expiredDay));
            if(etfContract == null){
                etfContract = new EtfContract();
                etfContract.setSecid(etf.getSecId());
                etfContract.setName(etf.getName());
                etfContract.setBaseCode(etf.getCode());
                etfContract.setExpiredDay(Integer.parseInt(expiredDay));
                etfContract.setDays(Integer.parseInt(days));
                etfContractRepository.insert(etfContract);
            }else{
                etfContract.setDays(Integer.parseInt(days));
                etfContractRepository.update(etfContract);
            }
            etfContracts.add(etfContract);
        }
        return etfContracts;
    }


    /**
     * 更新etf报价
     * @param secid
     * @param dateStr
     * @return
     */
    private Etf buildOrUpdateDCEtf(String secid, String dateStr){
        Etf etf = etfRepository.findByCodeAndDateStr(secid, dateStr);
        if(etf == null){
            etf = new Etf();
            etf.setSecId(secid);
            etf.setDateStr(dateStr);
            etfRepository.insert(etf);
        }

        String url = "http://push2.eastmoney.com/api/qt/stock/get?invt=2&fltt=1&secid="+ secid +"&fields=f58%2Cf734%2Cf107%2Cf57%2Cf43%2Cf59%2Cf169%2Cf170%2Cf152%2Cf46%2Cf60%2Cf44%2Cf45%2Cf47%2Cf48%2Cf19%2Cf17%2Cf531%2Cf15%2Cf13%2Cf11%2Cf20%2Cf18%2Cf16%2Cf14%2Cf12%2Cf39%2Cf37%2Cf35%2Cf33%2Cf31%2Cf40%2Cf38%2Cf36%2Cf34%2Cf32%2Cf211%2Cf212%2Cf213%2Cf214%2Cf215%2Cf210%2Cf209%2Cf208%2Cf207%2Cf206%2Cf161%2Cf49%2Cf171%2Cf50%2Cf86%2Cf168%2Cf108%2Cf167%2Cf71%2Cf292%2Cf51%2Cf52%2Cf191%2Cf192%2Cf452%2Cf177&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=8394365578044180%7C0%7C1%7C0%7Cweb&_=" + System.currentTimeMillis();
        HttpRequest httpRequest = HttpRequest.get(url);
        HttpResponse httpResponse = httpRequest.execute();
        String json = httpResponse.body();
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject data = jsonObject.getJSONObject("data");

        //代码
        String code = data.getString("f57");
        //名称
        String name = data.getString("f58");
        //当前价
        BigDecimal curPrice = new BigDecimal(data.getString("f19")).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP);
        //今天开盘价
        String todayOpen = new BigDecimal(data.getString("f46")).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP).toString();
        //昨日收盘价
        String yestodayClose = new BigDecimal(data.getString("f60")).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP).toString();
        //今日涨跌
        String change = new BigDecimal(data.getString("f169")).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP).toString();
        //今日涨跌
        String changeRate = new BigDecimal(data.getString("f170")).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP).toString();
        //成交量(万)
        String tradeHand = new BigDecimal(data.getString("f47")).divide(new BigDecimal(10000), 3, BigDecimal.ROUND_HALF_UP).toString();
        //成交额(亿)
        String tradeVolume = new BigDecimal(data.getString("f48")).divide(new BigDecimal(100000000), 3, BigDecimal.ROUND_HALF_UP).toString();

        etf.setCode(code);
        etf.setName(name);
        etf.setCurPrice(curPrice);
        etf.setChg(change);
        etf.setChgRate(changeRate + "%");
        etf.setTradeHand(tradeHand);
        etf.setTradeVolume(tradeVolume);
        etf.setTodayOpen(todayOpen);
        etf.setYestodayClose(yestodayClose);
        etfRepository.update(etf);
        return etf;
    }
}

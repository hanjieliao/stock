//package com.org.stock.service;
//
//import cn.hutool.http.HttpRequest;
//import cn.hutool.http.HttpResponse;
//import com.alibaba.fastjson2.JSON;
//import com.alibaba.fastjson2.JSONArray;
//import com.alibaba.fastjson2.JSONObject;
//import com.org.stock.repository.DaletouOpenResultRepository;
//import com.org.stock.repository.entity.DaletouOpenResult;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//
//@Slf4j
//@Component
//public class DaletouService {
//
//    public static void main(String[] args) {
//
//        List<Integer> exculeStart = Arrays.asList(1, 2, 4, 35);
//        List<Integer> exculeEnd = Arrays.asList(1, 9, 11);
//
//        for (int i = 0; i < 5; i++) {
//            List<Integer> start = new ArrayList<>();
//            List<Integer> end = new ArrayList<>();
//            while (start.size() < 5){
//                int result = new Random().nextInt(35) + 1;
//                if(!start.contains(result) && !exculeStart.contains(result)){
//                    start.add(result);
//                }
//            }
//
//            while (end.size() < 2){
//                int result = new Random().nextInt(12)  + 1;
//                if(!end.contains(result)  && !exculeEnd.contains(result)){
//                    end.add(result);
//                }
//            }
//
//            Collections.sort(start);
//            Collections.sort(end);
//            System.out.println("前:" + Arrays.toString(start.toArray()) + "\t后:" + Arrays.toString(end.toArray()));
//        }
//    }
//
//    @Autowired
//    private DaletouOpenResultRepository daletouOpenResultRepository;
//
//    /**
//     * 抓etf数据
//     * 30秒执行一次
//     */
//    @Scheduled(cron = "0/30 * * * * ?")
//    public void loadEtfContract(){
//
//        int pageNo = 1;
//
//        int lastNum;
//
//        while (true){
//            log.debug("正在抓取第{}页", pageNo);
//            String url = "https://webapi.sporttery.cn/gateway/lottery/getHistoryPageListV1.qry?gameNo=85&provinceId=0&pageSize=30&isVerify=1&pageNo=" + pageNo;
//            HttpRequest httpRequest = HttpRequest.get(url);
//            HttpResponse httpResponse = httpRequest.execute();
//            String json = httpResponse.body();
//            JSONObject jsonObject = JSON.parseObject(json);
//            JSONObject data = jsonObject.getJSONObject("value");
//            lastNum = data.getJSONObject("lastPoolDraw").getInteger("lotteryDrawNum");
//
//
//            JSONArray list = data.getJSONArray("list");
//            for (int i = 0; i < list.size(); i++) {
//                JSONObject jsonObject1 = list.getJSONObject(i);
//                String lotteryDrawNum = jsonObject1.getString("lotteryDrawNum");
//                String lotteryDrawTime = jsonObject1.getString("lotteryDrawTime");
//                String lotteryDrawResult = jsonObject1.getString("lotteryDrawResult");
//                String[] results = lotteryDrawResult.split("\\s+");
//
//                List<Integer> startResult = new ArrayList<>();
//                List<Integer> endResult = new ArrayList<>();
//                for (int j = 0; j < 7; j++) {
//                    String result = results[j];
//                    if(j < 5){
//                        startResult.add(Integer.parseInt(result));
//                    }else {
//                        endResult.add(Integer.parseInt(result));
//                    }
//                }
//
//                DaletouOpenResult daletouOpenResult = daletouOpenResultRepository.getByLotteryDrawNum(Integer.parseInt(lotteryDrawNum));
//                if(daletouOpenResult == null){
//                    daletouOpenResult = new DaletouOpenResult();
//                    daletouOpenResult.setLotteryDrawNum(Integer.parseInt(lotteryDrawNum));
//                    daletouOpenResult.setLotteryDrawTime(lotteryDrawTime);
//                    daletouOpenResult.setStartResult(startResult);
//                    daletouOpenResult.setEndResult(endResult);
//                    daletouOpenResultRepository.insert(daletouOpenResult);
//                }
//            }
//
//            break;
////            if (pageNo < data.getInteger("pages")){
////                pageNo++;
////            }else {
////                break;
////            }
//        }
//
//
//        lastNum = 21150;
//        while(true){
//            //当期
//            DaletouOpenResult current = daletouOpenResultRepository.getByLotteryDrawNum(lastNum);
//            //前期
//            DaletouOpenResult pre = daletouOpenResultRepository.getByLotteryDrawNum(lastNum - 1);
//
//            if(pre == null){
//                break;
//            }
//
//            int startCount = 0;
//            int endCount = 0;
//            for (Integer start : current.getStartResult()) {
//                if(pre.getStartResult().contains(start)){
//                    startCount++;
//                }
//            }
//
//            for (Integer start : current.getEndResult()) {
//                if(pre.getEndResult().contains(start)){
//                    endCount++;
//                }
//            }
//
//            current.setStartCount(startCount);
//            current.setEndCount(endCount);
//            daletouOpenResultRepository.update(current);
//
//            lastNum--;
//        }
//
//
//    }
//}

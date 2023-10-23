package com.org.stock.service;

import com.org.stock.model.Match;
import com.org.stock.repository.ContractDataRepository;
import com.org.stock.repository.EtfContractRepository;
import com.org.stock.repository.EtfRepository;
import com.org.stock.repository.RecommendMatchRepository;
import com.org.stock.repository.entity.ContractData;
import com.org.stock.repository.entity.Etf;
import com.org.stock.repository.entity.EtfContract;
import com.org.stock.repository.entity.RecommendMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class AnalysisService {

    @Autowired
    private EtfRepository etfRepository;
    @Autowired
    private EtfContractRepository etfContractRepository;
    @Autowired
    private ContractDataRepository contractDataRepository;
    @Autowired
    private RecommendMatchRepository recommendMatchRepository;

    /**
     * 分析组合合约
     * @param id
     */
    public void analysisEtfContract(Long id){
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        
        EtfContract etfContract = etfContractRepository.get(id);
        Etf etf = etfRepository.findByCodeAndDateStr(etfContract.getSecid(), dateStr);
        List<ContractData> contractDataList = contractDataRepository.listByContractId(etfContract.getId());

        //只把交割价波动在4%以内的交割价加入组合
        BigDecimal bodong = etf.getCurPrice().multiply(new BigDecimal("0.04")).setScale(4, BigDecimal.ROUND_HALF_UP);
        //购组合
        List<ContractData> gouGroup = new ArrayList<>();
        //沽组合
        List<ContractData> guGroup = new ArrayList<>();

        for (ContractData contractData : contractDataList) {
            if(Math.abs(contractData.getExercisePrice().doubleValue() - etf.getCurPrice().doubleValue()) <= bodong.doubleValue()){
                gouGroup.add(contractData);
                guGroup.add(contractData);
            }
        }

        List<Match> matches = new ArrayList<>();
        List<String> bodongRates = Arrays.asList("0.01", "0.015", "0.02", "0.025");
        for (String bodongRate : bodongRates) {
            for (ContractData gou : gouGroup) {
                for (ContractData gu : guGroup) {
                    if(gou.getCurGouPrice().equals("-") || gu.getCurGouPrice().equals("-") || gu.getCurGuPrice().equals("-")){
                        continue;
                    }

                    Match match = Match.valueOf(id, 3000, gou, gu, etf.getCurPrice().doubleValue(), bodongRate);
                    if(match.getGouCount() <=0 || match.getGuCount() <= 0){
                        continue;
                    }
                    matches.add(match);
                }
            }
        }



        for (Match match : matches) {
            RecommendMatch recommendMatch = recommendMatchRepository.getByGouAndGu(id,match.getBodongRate(), match.getGou().getGouName(), match.getGu().getGuName(), dateStr);
            if(recommendMatch != null){
                continue;
            }

            recommendMatchRepository.insert(RecommendMatch.valueOf(match, dateStr));
        }

    }


    /**
     * TODO
     * 持续更新推荐列表的盈亏情况
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void updateRecommendMatch(){

    }
}

package com.org.stock.model;

import com.org.stock.repository.entity.ContractData;
import com.org.stock.repository.entity.EtfContract;

import java.math.BigDecimal;

public class Match {
    /**
     * 构建组合
     * @param expectedCost 期望投入金额
     * @param gou 购合约
     * @param gu 沽合约
     * @param etfPrice etf当前价格
     * @param bodongRate 波动率
     * @return
     */
    public static Match valueOf(Long etfContractId, int expectedCost, ContractData gou, ContractData gu, double etfPrice, String bodongRate){
        Match match = new Match();
        match.bodongRate = bodongRate;
        match.bodong = new BigDecimal(etfPrice).multiply(new BigDecimal(bodongRate)).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();

        match.gou = gou;
        match.gu = gu;
        match.etfContractId = etfContractId;
        match.expectedCost = expectedCost;

        //买购1手花费
        int oneGouCost = (int)(new BigDecimal(gou.getCurGouPrice()).doubleValue() * 10000);
        //买沽1手花费
        int oneGuCost = (int)(new BigDecimal(gu.getCurGuPrice()).doubleValue() * 10000);

        //能买几手购
        match.gouCount = (expectedCost/2)/oneGouCost;
        //能买几手沽
        match.guCount = (expectedCost/2)/oneGuCost;
        //实际花费(每张要花3元手续费购入)
        match.realCost = oneGouCost*match.gouCount + oneGuCost*match.guCount + (match.gouCount + match.guCount)*3;

        //最大可能亏损
        match.maxLoss = (int)(match.gouCount*(gou.getGouTimeValue().doubleValue() * 10000) + match.guCount*(gu.getGuTimeValue().doubleValue() * 10000)) + (match.gouCount + match.guCount)*3;
        match.maxLoss = - Math.min(match.maxLoss, match.realCost);
        match.maxLossRate = match.maxLoss*1.0/ match.realCost;

        //大涨完购的内在价值
        int zhangGouRealValue = (int)(((etfPrice + match.bodong) - gou.getExercisePrice().doubleValue())*10000);
        zhangGouRealValue = Math.max(zhangGouRealValue, 0);
        //大涨完沽的内在价值
        int zhangGuRealValue = (int)((gu.getExercisePrice().doubleValue() - (etfPrice + match.bodong))*10000);
        zhangGuRealValue = Math.max(zhangGuRealValue, 0);
        //大涨后最终剩余价值
        int zhangRetainReal = zhangGouRealValue * match.gouCount + zhangGuRealValue * match.guCount - (match.gouCount*3);

        //大跌完购的内在价值
        int dieGouRealValue = (int)(((etfPrice - match.bodong) - gou.getExercisePrice().doubleValue())*10000);
        dieGouRealValue = Math.max(dieGouRealValue, 0);
        //大跌完沽的内在价值
        int dieGuRealValue = (int)((gu.getExercisePrice().doubleValue() - (etfPrice - match.bodong))*10000);
        dieGuRealValue = Math.max(dieGuRealValue, 0);
        //大涨后最终剩余价值
        int dieRetainReal = dieGouRealValue * match.gouCount + dieGuRealValue * match.guCount - (match.guCount*3);

        match.zhangMaxProfit = zhangRetainReal - match.realCost;
        match.zhangMaxProfitRate = match.zhangMaxProfit*1.0/match.realCost;

        match.dieMaxProfit = dieRetainReal - match.realCost;
        match.dieMaxProfitRate = match.dieMaxProfit*1.0/match.realCost;
        return match;
    }


    /**
     * 合约id
     * {@link EtfContract#getId()}
     */
    private Long etfContractId;

    /**
     * 波动
     */
    private double bodong;

    /**
     * 波动率
     */
    private String bodongRate;

    /**
     * 期望花费
     */
    private int expectedCost;

    /**
     * 实际花费
     */
    private int realCost;

    /**
     * 可以购入几单
     */
    private int gouCount;

    /**
     * 可以沽入几单
     */
    private int guCount;

    /**
     * 最大亏损
     */
    private int maxLoss;
    /**
     * 最大亏损率
     */
    private double maxLossRate;


    /**
     * 涨最大盈利
     */
    private int zhangMaxProfit;
    /**
     * 涨最大盈利率
     */
    private double zhangMaxProfitRate;


    /**
     * 跌最大盈利
     */
    private int dieMaxProfit;
    /**
     * 跌最大盈利率
     */
    private double dieMaxProfitRate;

    /**
     * 购合同
     */
    private ContractData gou;

    /**
     * 沽合同
     */
    private ContractData gu;

    public Long getEtfContractId() {
        return etfContractId;
    }

    public void setEtfContractId(Long etfContractId) {
        this.etfContractId = etfContractId;
    }

    public int getExpectedCost() {
        return expectedCost;
    }

    public void setExpectedCost(int expectedCost) {
        this.expectedCost = expectedCost;
    }

    public int getRealCost() {
        return realCost;
    }

    public void setRealCost(int realCost) {
        this.realCost = realCost;
    }

    public int getGouCount() {
        return gouCount;
    }

    public void setGouCount(int gouCount) {
        this.gouCount = gouCount;
    }

    public int getGuCount() {
        return guCount;
    }

    public void setGuCount(int guCount) {
        this.guCount = guCount;
    }

    public ContractData getGou() {
        return gou;
    }

    public void setGou(ContractData gou) {
        this.gou = gou;
    }

    public ContractData getGu() {
        return gu;
    }

    public void setGu(ContractData gu) {
        this.gu = gu;
    }

    public int getMaxLoss() {
        return maxLoss;
    }

    public void setMaxLoss(int maxLoss) {
        this.maxLoss = maxLoss;
    }

    public double getMaxLossRate() {
        return maxLossRate;
    }

    public void setMaxLossRate(double maxLossRate) {
        this.maxLossRate = maxLossRate;
    }

    public int getZhangMaxProfit() {
        return zhangMaxProfit;
    }

    public void setZhangMaxProfit(int zhangMaxProfit) {
        this.zhangMaxProfit = zhangMaxProfit;
    }

    public double getZhangMaxProfitRate() {
        return zhangMaxProfitRate;
    }

    public void setZhangMaxProfitRate(double zhangMaxProfitRate) {
        this.zhangMaxProfitRate = zhangMaxProfitRate;
    }

    public int getDieMaxProfit() {
        return dieMaxProfit;
    }

    public void setDieMaxProfit(int dieMaxProfit) {
        this.dieMaxProfit = dieMaxProfit;
    }

    public double getDieMaxProfitRate() {
        return dieMaxProfitRate;
    }

    public void setDieMaxProfitRate(double dieMaxProfitRate) {
        this.dieMaxProfitRate = dieMaxProfitRate;
    }

    public double getBodong() {
        return bodong;
    }

    public void setBodong(double bodong) {
        this.bodong = bodong;
    }

    public String getBodongRate() {
        return bodongRate;
    }

    public void setBodongRate(String bodongRate) {
        this.bodongRate = bodongRate;
    }
}

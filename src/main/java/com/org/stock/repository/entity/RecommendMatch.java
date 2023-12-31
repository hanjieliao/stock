package com.org.stock.repository.entity;

import com.org.stock.model.Match;
import com.org.stock.repository.base.IEntity;
import lombok.experimental.FieldNameConstants;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import java.util.Date;

/**
 * 推荐组合
 */
@FieldNameConstants
@Table("recommend_match")
public class RecommendMatch implements IEntity<Long> {

    @Id
    private Long id;

    /**
     * 合约id
     * {@link EtfContract#getId()}
     */
    @Column
    private Long etfContractId;

    /**
     * 波动
     */
    @Column
    private double bodong;

    /**
     * 波动率
     */
    @Column
    private String bodongRate;

    /**
     * 推荐日期(每个组合一天最多推荐一次)
     */
    @Column
    private String dateStr;

    /**
     * 购合约
     */
    @Column
    private String gou;
    /**
     * 购合约id
     */
    @Column
    private Long gouId;
    /**
     * 购几手
     */
    @Column
    private int goutCount;
    /**
     * 沽合约
     */
    @Column
    private String gu;
    /**
     * 沽合约id
     */
    @Column
    private Long guId;
    /**
     * 沽几手
     */
    @Column
    private int gutCount;

    /**
     * 实际花费
     */
    @Column
    private int realCost;

    /**
     * 最大亏损
     */
    @Column
    private int maxLoss;
    /**
     * 最大亏损率
     */
    @Column
    private double maxLossRate;


    /**
     * 涨最大盈利
     */
    @Column
    private int zhangMaxProfit;
    /**
     * 涨最大盈利率
     */
    @Column
    private double zhangMaxProfitRate;


    /**
     * 跌最大盈利
     */
    @Column
    private int dieMaxProfit;
    /**
     * 跌最大盈利率
     */
    @Column
    private double dieMaxProfitRate;
    /**
     * 实际盈亏
     */
    @Column
    private int realChange;

    /**
     * 创建时间
     */
    @Column
    private Date createTime = new Date();


    public static RecommendMatch valueOf(Match match, String dateStr){
        RecommendMatch recommendMatch = new RecommendMatch();
        recommendMatch.setEtfContractId(match.getEtfContractId());
        recommendMatch.setBodongRate(match.getBodongRate());
        recommendMatch.setBodong(match.getBodong());
        recommendMatch.setGou(match.getGou().getGouName());
        recommendMatch.setGouId(match.getGou().getId());
        recommendMatch.setGu(match.getGu().getGuName());
        recommendMatch.setGuId(match.getGu().getId());
        recommendMatch.setGoutCount(match.getGouCount());
        recommendMatch.setGutCount(match.getGuCount());
        recommendMatch.setRealCost(match.getRealCost());
        recommendMatch.setMaxLoss(match.getMaxLoss());
        recommendMatch.setMaxLossRate(match.getMaxLossRate());
        recommendMatch.setZhangMaxProfit(match.getZhangMaxProfit());
        recommendMatch.setZhangMaxProfitRate(match.getZhangMaxProfitRate());
        recommendMatch.setDieMaxProfit(match.getDieMaxProfit());
        recommendMatch.setDieMaxProfitRate(match.getDieMaxProfitRate());
        recommendMatch.setDateStr(dateStr);
        return recommendMatch;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEtfContractId() {
        return etfContractId;
    }

    public void setEtfContractId(Long etfContractId) {
        this.etfContractId = etfContractId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getGou() {
        return gou;
    }

    public void setGou(String gou) {
        this.gou = gou;
    }

    public Long getGouId() {
        return gouId;
    }

    public void setGouId(Long gouId) {
        this.gouId = gouId;
    }

    public Long getGuId() {
        return guId;
    }

    public void setGuId(Long guId) {
        this.guId = guId;
    }

    public int getRealChange() {
        return realChange;
    }

    public void setRealChange(int realChange) {
        this.realChange = realChange;
    }

    public int getGoutCount() {
        return goutCount;
    }

    public void setGoutCount(int goutCount) {
        this.goutCount = goutCount;
    }

    public String getGu() {
        return gu;
    }

    public void setGu(String gu) {
        this.gu = gu;
    }

    public int getGutCount() {
        return gutCount;
    }

    public void setGutCount(int gutCount) {
        this.gutCount = gutCount;
    }

    public int getRealCost() {
        return realCost;
    }

    public void setRealCost(int realCost) {
        this.realCost = realCost;
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

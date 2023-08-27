package com.org.stock.repository.entity;

import com.org.stock.repository.base.IEntity;
import lombok.experimental.FieldNameConstants;
import org.nutz.dao.entity.annotation.*;

import java.math.BigDecimal;

/**
 * etf行情
 */
@FieldNameConstants
@Table("etf")
public class Etf implements IEntity<Long> {

    @Id
    private Long id;

    /**
     * 接口查询用到的code
     */
    @Column
    private String secId;

    /**
     * 指数名
     */
    @Column
    private String name;
    /**
     * 代码
     */
    @Column
    private String code;

    /**
     * 当前价
     */
    @ColDefine(type= ColType.DOUBLE, width=32, precision=4)
    @Column
    private BigDecimal curPrice;

    /**
     * 涨跌值
     */
    @Column
    private String chg;

    /**
     * 涨跌百分比
     */
    @Column
    private String chgRate;

    /**
     * 成交量(手)
     */
    @Column
    private String tradeHand;

    /**
     * 成交额(万元)
     */
    @Column
    private String tradeVolume;

    /**
     * 今天开盘价
     */
    @Column
    private String todayOpen;

    /**
     * 昨日收盘价
     */
    @Column
    private String yestodayClose;


    /**
     * 日期(每天都记录下行情)
     */
    @Column
    private String dateStr;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public String getTodayOpen() {
        return todayOpen;
    }

    public void setTodayOpen(String todayOpen) {
        this.todayOpen = todayOpen;
    }

    public String getYestodayClose() {
        return yestodayClose;
    }

    public void setYestodayClose(String yestodayClose) {
        this.yestodayClose = yestodayClose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(BigDecimal curPrice) {
        this.curPrice = curPrice;
    }

    public String getChg() {
        return chg;
    }

    public void setChg(String chg) {
        this.chg = chg;
    }

    public String getChgRate() {
        return chgRate;
    }

    public void setChgRate(String chgRate) {
        this.chgRate = chgRate;
    }

    public String getTradeHand() {
        return tradeHand;
    }

    public void setTradeHand(String tradeHand) {
        this.tradeHand = tradeHand;
    }

    public String getTradeVolume() {
        return tradeVolume;
    }

    public void setTradeVolume(String tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
}

package com.org.stock.repository.entity;

import com.org.stock.repository.base.IEntity;
import lombok.experimental.FieldNameConstants;
import org.nutz.dao.entity.annotation.*;

import java.math.BigDecimal;

/**
 * 合约数据
 */
@FieldNameConstants
@Table("contract_data")
public class ContractData implements IEntity<Long> {

    @Id
    private Long id;

    /**
     * 日期(每天的行情都记录一下)
     */
    @Column
    private String dateStr;

    /**
     * 合约id
     * {@link EtfContract#getId()}
     */
    @Column
    private Long etfContractId;

    /**购合约代码*/
    @Column
    private String gouCode;

    /**
     * 认购名
     */
    @Column
    private String gouName;

    /**
     * 当前认购价
     */
    @Column
    private String curGouPrice;

    /**
     * 购绝对价值
     */
    @ColDefine(type= ColType.DOUBLE, width=32, precision=4)
    @Column
    private BigDecimal gouRealValue;

    /**
     * 购时间价值
     */
    @ColDefine(type= ColType.DOUBLE, width=32, precision=4)
    @Column
    private BigDecimal gouTimeValue;

    /**
     * 认购涨跌额
     */
    @Column
    private String gouChg;

    /**
     * 认购涨跌比
     */
    @Column
    private String gouChgRate;


    /**行权价*/
    @ColDefine(type= ColType.DOUBLE, width=32, precision=4)
    @Column
    private BigDecimal exercisePrice;

    /**沽合约代码*/
    @Column
    private String guCode;

    /**
     * 认沽名
     */
    @Column
    private String guName;

    /**
     * 当前认沽价
     */
    @Column
    private String curGuPrice;

    /**
     * 沽绝对价值
     */
    @ColDefine(type= ColType.DOUBLE, width=32, precision=4)
    @Column
    private BigDecimal guRealValue;

    /**
     * 沽时间价值
     */
    @ColDefine(type= ColType.DOUBLE, width=32, precision=4)
    @Column
    private BigDecimal guTimeValue;

    /**
     * 认沽涨跌额
     */
    @Column
    private String guChg;

    /**
     * 认沽涨跌比
     */
    @Column
    private String guChgRate;


    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Long getEtfContractId() {
        return etfContractId;
    }

    public void setEtfContractId(Long etfContractId) {
        this.etfContractId = etfContractId;
    }

    public String getGouCode() {
        return gouCode;
    }

    public void setGouCode(String gouCode) {
        this.gouCode = gouCode;
    }

    public String getGouName() {
        return gouName;
    }

    public void setGouName(String gouName) {
        this.gouName = gouName;
    }

    public String getCurGouPrice() {
        return curGouPrice;
    }

    public void setCurGouPrice(String curGouPrice) {
        this.curGouPrice = curGouPrice;
    }

    public BigDecimal getGouRealValue() {
        return gouRealValue;
    }

    public void setGouRealValue(BigDecimal gouRealValue) {
        this.gouRealValue = gouRealValue;
    }

    public BigDecimal getGouTimeValue() {
        return gouTimeValue;
    }

    public void setGouTimeValue(BigDecimal gouTimeValue) {
        this.gouTimeValue = gouTimeValue;
    }

    public String getGouChg() {
        return gouChg;
    }

    public void setGouChg(String gouChg) {
        this.gouChg = gouChg;
    }

    public String getGouChgRate() {
        return gouChgRate;
    }

    public void setGouChgRate(String gouChgRate) {
        this.gouChgRate = gouChgRate;
    }

    public BigDecimal getExercisePrice() {
        return exercisePrice;
    }

    public void setExercisePrice(BigDecimal exercisePrice) {
        this.exercisePrice = exercisePrice;
    }

    public String getGuCode() {
        return guCode;
    }

    public void setGuCode(String guCode) {
        this.guCode = guCode;
    }

    public String getGuName() {
        return guName;
    }

    public void setGuName(String guName) {
        this.guName = guName;
    }

    public String getCurGuPrice() {
        return curGuPrice;
    }

    public void setCurGuPrice(String curGuPrice) {
        this.curGuPrice = curGuPrice;
    }

    public BigDecimal getGuRealValue() {
        return guRealValue;
    }

    public void setGuRealValue(BigDecimal guRealValue) {
        this.guRealValue = guRealValue;
    }

    public BigDecimal getGuTimeValue() {
        return guTimeValue;
    }

    public void setGuTimeValue(BigDecimal guTimeValue) {
        this.guTimeValue = guTimeValue;
    }

    public String getGuChg() {
        return guChg;
    }

    public void setGuChg(String guChg) {
        this.guChg = guChg;
    }

    public String getGuChgRate() {
        return guChgRate;
    }

    public void setGuChgRate(String guChgRate) {
        this.guChgRate = guChgRate;
    }
}

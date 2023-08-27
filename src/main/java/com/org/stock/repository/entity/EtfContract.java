package com.org.stock.repository.entity;

import com.org.stock.repository.base.IEntity;
import lombok.experimental.FieldNameConstants;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * etf合约
 */
@FieldNameConstants
@Table("etf_contract")
public class EtfContract implements IEntity<Long> {

    @Id
    private Long id;
    /**
     * 接口查询用到的code
     */
    @Column
    private String secid;
    /**
     * 指数名
     */
    @Column
    private String name;
    /**
     * 代码
     */
    @Column
    private String baseCode;
    /**
     * 合约到期月份
     */
    @Column
    private int expiredDay;

    /**
     * 还有几天到期
     */
    @Column
    private int days;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecid() {
        return secid;
    }

    public void setSecid(String secid) {
        this.secid = secid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public int getExpiredDay() {
        return expiredDay;
    }

    public void setExpiredDay(int expiredDay) {
        this.expiredDay = expiredDay;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}

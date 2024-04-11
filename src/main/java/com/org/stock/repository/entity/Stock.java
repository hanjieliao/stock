package com.org.stock.repository.entity;

import com.org.stock.repository.base.IEntity;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Table("stock")
public class Stock implements IEntity<String> {

    @Name
    private String id;

    @Column
    private String name;

    /**
     * 总持仓占比
     */
    @Column
    private double totalSharesRatio;

    /**
     * 总市值(亿)
     */
    @Column
    private long totalValue;

    /**
     * 股东数
     */
    @Column
    private long num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalSharesRatio() {
        return totalSharesRatio;
    }

    public void setTotalSharesRatio(double totalSharesRatio) {
        this.totalSharesRatio = totalSharesRatio;
    }

    public long getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(long totalValue) {
        this.totalValue = totalValue;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }
}

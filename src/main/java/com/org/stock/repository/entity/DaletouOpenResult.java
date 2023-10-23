package com.org.stock.repository.entity;

import com.org.stock.repository.base.IEntity;
import lombok.experimental.FieldNameConstants;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import java.util.List;

@FieldNameConstants
@Table("daletou_open_result")
public class DaletouOpenResult implements IEntity<Long> {


    @Id
    private Long id;

    /**
     * 第几期
     */
    @Column
    private int lotteryDrawNum;

    /**
     * 开奖日期
     */
    @Column
    private String lotteryDrawTime;

    /**
     * 前区结果
     */
    @Column
    private List<Integer> startResult;

    /**
     * 后区结果
     */
    @Column
    private List<Integer> endResult;

    /**
     * 前区结果有几个前期的
     */
    @Column
    private int startCount;

    /**
     * 后区结果有几个前期的
     */
    @Column
    private int endCount;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLotteryDrawNum() {
        return lotteryDrawNum;
    }

    public void setLotteryDrawNum(int lotteryDrawNum) {
        this.lotteryDrawNum = lotteryDrawNum;
    }

    public String getLotteryDrawTime() {
        return lotteryDrawTime;
    }

    public void setLotteryDrawTime(String lotteryDrawTime) {
        this.lotteryDrawTime = lotteryDrawTime;
    }

    public List<Integer> getStartResult() {
        return startResult;
    }

    public void setStartResult(List<Integer> startResult) {
        this.startResult = startResult;
    }

    public List<Integer> getEndResult() {
        return endResult;
    }

    public void setEndResult(List<Integer> endResult) {
        this.endResult = endResult;
    }

    public int getStartCount() {
        return startCount;
    }

    public void setStartCount(int startCount) {
        this.startCount = startCount;
    }

    public int getEndCount() {
        return endCount;
    }

    public void setEndCount(int endCount) {
        this.endCount = endCount;
    }
}

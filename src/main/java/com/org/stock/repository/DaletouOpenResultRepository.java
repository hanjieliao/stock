package com.org.stock.repository;

import com.org.stock.repository.base.AbstractEntityRepository;
import com.org.stock.repository.entity.DaletouOpenResult;
import org.nutz.dao.Cnd;
import org.springframework.stereotype.Component;

@Component
public class DaletouOpenResultRepository extends AbstractEntityRepository<DaletouOpenResult, Long> {

    public DaletouOpenResult getByLotteryDrawNum(int lotteryDrawNum){
        return getByCnd(Cnd.where(DaletouOpenResult.Fields.lotteryDrawNum, "=", lotteryDrawNum));
    }
}

package com.org.stock.repository;

import com.org.stock.repository.base.AbstractEntityRepository;
import com.org.stock.repository.entity.RecommendMatch;
import org.nutz.dao.Cnd;
import org.springframework.stereotype.Component;

@Component
public class RecommendMatchRepository extends AbstractEntityRepository<RecommendMatch, Long> {

    public void deleteAll(){
        deleteByCnd(Cnd.NEW());
    }


    public RecommendMatch getByGouAndGu( Long etfContractId, String gou, String gu, String dateStr){
        Cnd cnd = Cnd.where(RecommendMatch.Fields.etfContractId, "=", etfContractId);
        cnd.and(RecommendMatch.Fields.gou, "=", gou);
        cnd.and(RecommendMatch.Fields.gu, "=", gu);
        cnd.and(RecommendMatch.Fields.dateStr, "=", dateStr);
        return getByCnd(cnd);
    }
}

package com.org.stock.repository;

import com.org.stock.repository.base.AbstractEntityRepository;
import com.org.stock.repository.entity.Etf;
import org.nutz.dao.Cnd;
import org.springframework.stereotype.Component;

@Component
public class EtfRepository extends AbstractEntityRepository<Etf, Long> {

    public Etf findByCodeAndDateStr(String secId, String dateStr){
        return getByCnd(Cnd.where(Etf.Fields.secId, "=", secId).and(Etf.Fields.dateStr,"=", dateStr));
    }
}

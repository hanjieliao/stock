package com.org.stock.repository;

import com.org.stock.repository.base.AbstractEntityRepository;
import com.org.stock.repository.entity.EtfContract;
import org.nutz.dao.Cnd;
import org.springframework.stereotype.Component;

@Component
public class EtfContractRepository extends AbstractEntityRepository<EtfContract, Long> {

    public EtfContract findByBaseCodeAndExpiredMonth(String baseCode, int expiredDay){
        return getByCnd(Cnd.where(EtfContract.Fields.baseCode, "=", baseCode).and(EtfContract.Fields.expiredDay,"=", expiredDay));
    }
}

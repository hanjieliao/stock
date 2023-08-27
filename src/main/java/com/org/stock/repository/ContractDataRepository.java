package com.org.stock.repository;

import com.org.stock.repository.base.AbstractEntityRepository;
import com.org.stock.repository.entity.ContractData;
import org.nutz.dao.Cnd;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContractDataRepository extends AbstractEntityRepository<ContractData, Long> {

    public ContractData findByEtfContractIdAndCode(Long etfContractId, String gouCode, String guCode, String date){
        Cnd cnd = Cnd.where(ContractData.Fields.etfContractId, "=", etfContractId);
        cnd.and(ContractData.Fields.gouCode,"=", gouCode);
        cnd.and(ContractData.Fields.guCode,"=", guCode);
        cnd.and(ContractData.Fields.dateStr,"=", date);
        return getByCnd(cnd);
    }


    public List<ContractData> listByContractId(long etfContractId){
        return list(Cnd.where(ContractData.Fields.etfContractId, "=", etfContractId));
    }
}

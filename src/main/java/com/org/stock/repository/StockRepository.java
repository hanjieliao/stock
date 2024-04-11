package com.org.stock.repository;

import com.org.stock.repository.base.AbstractEntityRepository;
import com.org.stock.repository.entity.RecommendMatch;
import com.org.stock.repository.entity.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockRepository extends AbstractEntityRepository<Stock, String> {
}

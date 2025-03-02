package org.advance.glass.stock.service;

import lombok.RequiredArgsConstructor;
import org.advance.glass.stock.model.db.Stock;
import org.advance.glass.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }
}

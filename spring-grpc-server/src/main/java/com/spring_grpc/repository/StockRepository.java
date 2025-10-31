package com.spring_grpc.repository;

import com.spring_grpc.entity.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockRepository extends MongoRepository<Stock, String> {
    Stock findByStockSymbol(String stockSymbol);
}

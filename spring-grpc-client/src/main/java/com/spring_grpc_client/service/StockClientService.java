package com.spring_grpc_client.service;

import com.spring_grpc.grpc.StockRequest;
import com.spring_grpc.grpc.StockResponse;
import com.spring_grpc.grpc.StockTradingServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class StockClientService {

    @GrpcClient("stockService")
    private StockTradingServiceGrpc.StockTradingServiceBlockingStub stockTradingServiceBlockingStub;

    public StockResponse getStockPrice(String stockSymbol) {
        StockRequest request = StockRequest.newBuilder().setStockSymbol(stockSymbol).build();

      return    stockTradingServiceBlockingStub.getStock(request);
    }
}

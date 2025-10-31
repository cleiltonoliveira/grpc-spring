package com.spring_grpc.service;

import com.spring_grpc.entity.Stock;
import com.spring_grpc.grpc.StockRequest;
import com.spring_grpc.grpc.StockResponse;
import com.spring_grpc.grpc.StockTradingServiceGrpc;
import com.spring_grpc.repository.StockRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class StockTradingServiceImpl extends StockTradingServiceGrpc.StockTradingServiceImplBase {

    private final StockRepository stockRepository;

    public StockTradingServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void getStock(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        //stockName -> DB -> map response -> return
        String stockSymbol = request.getStockSymbol();
        Stock stock = stockRepository.findByStockSymbol(stockSymbol);

        StockResponse stockResponse = StockResponse.newBuilder()
                .setStockSymbol(stock.getStockSymbol())
                .setPrice(stock.getPrice())
                .setTimestamp(stock.getLastUpdated().toString())
                .build();

        responseObserver.onNext(stockResponse);
        responseObserver.onCompleted();
    }
}

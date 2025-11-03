package com.spring_grpc_client.service;

import com.spring_grpc.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class StockClientService {

    @GrpcClient("stockService")
    private StockTradingServiceGrpc.StockTradingServiceBlockingStub stockTradingServiceBlockingStub;

    @GrpcClient("stockService")
    private StockTradingServiceGrpc.StockTradingServiceStub stockTradingServiceStub;

    public StockResponse getStockPrice(String stockSymbol) {
        StockRequest request = StockRequest.newBuilder().setStockSymbol(stockSymbol).build();

        return stockTradingServiceBlockingStub.getStock(request);
    }

    public void subscribeStockPrice(String symbol) {
        StockRequest request = StockRequest.newBuilder().setStockSymbol(symbol).build();

        stockTradingServiceStub.subscribeStockPrice(request, new StreamObserver<StockResponse>() {
            @Override
            public void onNext(StockResponse stockResponse) {
                System.out.println("Symbol: " + stockResponse.getStockSymbol() +
                        " Price: " + stockResponse.getPrice() +
                        " Time: " + stockResponse.getTimestamp());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Stock price stream live update completed");
            }
        });
    }

    public void placeBulkOrders() {
        StreamObserver<OrderSummary> streamObserver = new StreamObserver<>() {
            @Override
            public void onNext(OrderSummary orderSummary) {
                System.out.println("Order summary: ");
                System.out.println("Total orders: " + orderSummary.getTotalOrders());
                System.out.println("Successful orders: " + orderSummary.getSuccessCount());
                System.out.println("Total amount: " + orderSummary.getSuccessCount());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Order summary error from server: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Order summary completed");
            }
        };


        StreamObserver<StockOrder> requestObserver = stockTradingServiceStub.bulkStockOrder(streamObserver);

        // send multiple stock order

        try {
            requestObserver.onNext(StockOrder.newBuilder()
                    .setOrderId("1")
                    .setStockSymbol("BBSA2")
                    .setPrice(15)
                    .setOrderType("1")
                    .setQuantity(2)
                    .build());

            requestObserver.onNext(StockOrder.newBuilder()
                    .setOrderId("1")
                    .setStockSymbol("GGBR4")
                    .setPrice(11)
                    .setOrderType("1")
                    .setQuantity(3)
                    .build());

            requestObserver.onNext(StockOrder.newBuilder()
                    .setOrderId("1")
                    .setStockSymbol("KLB11")
                    .setPrice(12)
                    .setOrderType("1")
                    .setQuantity(1)
                    .build());

            // done sending orders
            requestObserver.onCompleted();

        } catch (Exception e) {
            requestObserver.onError(e);
        }
    }

    public void startLiveTrading() throws InterruptedException {
        StreamObserver<StockOrder> streamObserver = stockTradingServiceStub.liveTrading(new StreamObserver<>() {
            @Override
            public void onNext(TradeStatus tradeStatus) {
                System.out.println("Server response: " + tradeStatus);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Server error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Server completed: ");
            }
        });

        for (int i = 1; i <= 10; i++) {
            StockOrder stockOrder = StockOrder.newBuilder()
                    .setOrderId("ORDER-" + i)
                    .setStockSymbol("APPL")
                    .setPrice(15 + i)
                    .setOrderType("BUY")
                    .setQuantity(2 * i)
                    .build();
            streamObserver.onNext(stockOrder);
            Thread.sleep(500);
        }

        streamObserver.onCompleted();
    }

}

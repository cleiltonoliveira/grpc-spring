package com.spring_grpc_client;

import com.spring_grpc_client.service.StockClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringGrpcClientApplication implements CommandLineRunner {

    private final StockClientService stockClientService;

    public SpringGrpcClientApplication(StockClientService stockClientService) {
        this.stockClientService = stockClientService;
    }


    public static void main(String[] args) {
        SpringApplication.run(SpringGrpcClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Unary call
//        var stock = stockClientService.getStockPrice("GGBR4");
//        System.out.println("GRPC result: " + stock);
//
//        // server stream
//        stockClientService.subscribeStockPrice("GGBR4");
//
//        // client stream
//        stockClientService.placeBulkOrders();

        // bidirectional streaming
        stockClientService.startLiveTrading();
    }
}

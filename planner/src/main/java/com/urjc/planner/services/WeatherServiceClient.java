package com.urjc.planner.services;

import com.urjc.planner.WeatherServiceGrpc;
import com.urjc.planner.WeatherServiceOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class WeatherServiceClient {

    @GrpcClient("weatherServer")
    private WeatherServiceGrpc.WeatherServiceBlockingStub client;

    public WeatherServiceClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        client = WeatherServiceGrpc.newBlockingStub(channel);

    }

    @Async
    public CompletableFuture<WeatherServiceOuterClass.Weather> getWeather(WeatherServiceOuterClass.GetWeatherRequest request) {
        return CompletableFuture.completedFuture(client.weather(request));
    }

}

package com.urjc.planner;

import dtos.GetTopographicInfoRequest;
import dtos.GetTopographicInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Component;
import services.TopoServiceClient;
import services.WeatherServiceClient;

import java.util.concurrent.CompletableFuture;

@Component
public class CommandRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CommandRunner.class);

    private final TopoServiceClient topoServiceClient;
    private final WeatherServiceClient weatherServiceClient;

    public CommandRunner(TopoServiceClient topoServiceClient, WeatherServiceClient weatherServiceClient) {
        this.topoServiceClient = topoServiceClient;
        this.weatherServiceClient = weatherServiceClient;
    }

    @Override
    public void run(String... args) throws Exception {
        // Start the clock
        long start = System.currentTimeMillis();

        // Kick of multiple, asynchronous lookups
        CompletableFuture<GetTopographicInfoResponse> topography = topoServiceClient.getTopography(new GetTopographicInfoRequest("Leon"));
        //CompletableFuture<WeatherServiceOuterClass.Weather> page2 = weatherServiceClient.getWeather(WeatherServiceOuterClass.GetWeatherRequest.newBuilder().setCity("Leon").build());
        WeatherServiceOuterClass.Weather weather = weatherServiceClient.getWeather(WeatherServiceOuterClass.GetWeatherRequest.newBuilder().setCity("Leon").build());

        // Wait until they are all done
        CompletableFuture.allOf(topography).join();

        topography.whenCompleteAsync((response, error) -> logger.info("Result topography: {}", response.getLandscape()));
        logger.info("Result weather: {}", weather.getWeather());
    }

}
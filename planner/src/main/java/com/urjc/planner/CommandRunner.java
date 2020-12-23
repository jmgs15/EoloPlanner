package com.urjc.planner;

import com.urjc.planner.dtos.GetTopographicInfoRequest;
import com.urjc.planner.dtos.GetTopographicInfoResponse;
import com.urjc.planner.services.TopoServiceClient;
import com.urjc.planner.services.WeatherServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        logger.info("Run start");
        // Kick of multiple, asynchronous lookups
        CompletableFuture<GetTopographicInfoResponse> topography = topoServiceClient.getTopography(new GetTopographicInfoRequest("Leon"));

        WeatherServiceOuterClass.GetWeatherRequest weatherRequest = WeatherServiceOuterClass.GetWeatherRequest.newBuilder().setCity("Leon").build();
        CompletableFuture<WeatherServiceOuterClass.Weather> weather = weatherServiceClient.getWeather(weatherRequest);
        //WeatherServiceOuterClass.Weather weather = weatherServiceClient.getWeather(weatherRequest);

        final Boolean weatherFinished = false;
        final Boolean topographyFinished = false;
        //logger.info("Result weather: {}", weather.getWeather());
        weather.whenCompleteAsync((response, error) -> {
            logger.info("Weather: Result weather: {}", response.getWeather());
            logger.info("Weather: topography.isDone: {}", topography.isDone());
        });

        topography.whenCompleteAsync((response, error) -> {
            logger.info("Topography: Result topography: {}", response.getLandscape());
            logger.info("Topography: weather.isDone: {}", weather.isDone());
        });

        //CompletableFuture.allOf(weather, topography).whenCompleteAsync((response, error) -> logger.info("Both completed"));
        logger.info("Run end");
    }

//        weatherServiceClient.weather(weatherRequest, new StreamObserver<WeatherServiceOuterClass.Weather>() {
//            @Override
//            public void onNext(WeatherServiceOuterClass.Weather response) {
//                logger.info("Result weather: {}", response.getWeather());
//            }
//            @Override
//            public void onError(Throwable throwable) {
//                logger.info("Error");
//            }
//            @Override
//            public void onCompleted() {
//                logger.info("WeatherService completed");
//            }
//        });

}
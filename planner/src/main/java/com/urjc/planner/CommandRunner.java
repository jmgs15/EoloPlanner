package com.urjc.planner;

import com.urjc.planner.dtos.GetTopographicInfoRequest;
import com.urjc.planner.dtos.GetTopographicInfoResponse;
import com.urjc.planner.models.PlantCreation;
import com.urjc.planner.models.State;
import com.urjc.planner.queues.EoloplantCreationProgressNotificationsProducer;
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
    private final EoloplantCreationProgressNotificationsProducer eoloplantProducer;

    public CommandRunner(TopoServiceClient topoServiceClient, WeatherServiceClient weatherServiceClient, EoloplantCreationProgressNotificationsProducer eoloplantProducer) {
        this.topoServiceClient = topoServiceClient;
        this.weatherServiceClient = weatherServiceClient;
        this.eoloplantProducer = eoloplantProducer;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Run start");

        String city = "Zaragoza";
        PlantCreation plantCreation = PlantCreation.builder()
                .city(city)
                .state(State.INITIAL)
                .planning("")
                .completed(false)
                .build();

        CompletableFuture<GetTopographicInfoResponse> topography = topoServiceClient.getTopography(new GetTopographicInfoRequest(city));
        WeatherServiceOuterClass.GetWeatherRequest weatherRequest = WeatherServiceOuterClass.GetWeatherRequest.newBuilder().setCity(city).build();
        CompletableFuture<WeatherServiceOuterClass.Weather> weather = weatherServiceClient.getWeather(weatherRequest);

        eoloplantProducer.sendStateChange(plantCreation, city);

        weather.whenCompleteAsync((response, error) -> {
            logger.info("Weather: Result weather: {}", response.getWeather());
            logger.info("Weather: topography.isDone: {}", topography.isDone());
            eoloplantProducer.sendStateChange(plantCreation, response.getWeather());
        });

        topography.whenCompleteAsync((response, error) -> {
            logger.info("Topography: Result topography: {}", response.getLandscape());
            logger.info("Topography: weather.isDone: {}", weather.isDone());
            eoloplantProducer.sendStateChange(plantCreation, response.getLandscape());
        });

        //CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(weather, topography);
        //combinedFuture.get();
        //logger.info("Both completed");
        logger.info("Run end");
    }
}
